package com.ktung.watchit.ui.shows.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Ratings;
import com.ktung.watchit.api.trakt.entities.ShowResponse;
import com.ktung.watchit.util.BaseObserver;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DiscoverAdapter extends BaseAdapter {

    public static final int NB_MAX_GENRES = 3;

    protected static class ViewHolder {
        @Bind(R.id.showPoster)
        public ImageView showPoster;
        @Bind(R.id.showTitle)
        public TextView showTitle;
        @Bind(R.id.showGenres)
        public TextView showGenres;
        @Bind(R.id.showAirs)
        public TextView showAirs;
        @Bind(R.id.icTrakt)
        public ImageView icTrakt;
        @Bind(R.id.traktRating)
        public TextView traktRating;
        @Bind(R.id.showCertification)
        public TextView showCertification;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private Trakt trakt;
    private Context context;
    private List<ShowResponse> shows;
    private Map<Integer, Ratings> showRatings;
    private LayoutInflater inflater;

    public DiscoverAdapter(Context context, List<ShowResponse> shows) {
        trakt = Trakt.getInstance();
        this.context = context;
        this.shows = shows;
        showRatings = new HashMap<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.it_shows_discover, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        ShowResponse show = shows.get(i);
        loadView(viewHolder, show);
        loadShowRatings(i, viewHolder);

        return view;
    }

    private void loadShowRatings(int position, ViewHolder vh) {
        if (showRatings.isEmpty() || showRatings.get(position) == null) {
            trakt.shows().ratings(String.valueOf(shows.get(position).show.ids.trakt))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<Ratings>() {
                @Override
                public void onNext(Ratings ratings) {
                    showRatings.put(position, ratings);
                    loadRatingsView(position, vh);
                }
            });
        } else {
            loadRatingsView(position, vh);
        }
    }

    private void loadView(ViewHolder vh, ShowResponse show) {
        vh.showTitle.setText(show.show.title);
        vh.showCertification.setText(show.show.certification);

        // Quick recycle for search query
        if (show.show.genres != null) {
            int nbMaxGenres = (show.show.genres.size() > NB_MAX_GENRES) ? NB_MAX_GENRES : show.show.genres.size();
            List<String> genres = show.show.genres.subList(0, nbMaxGenres);
            for (int i = 0; i < genres.size(); ++i) {
                genres.set(i, genres.get(i).substring(0, 1).toUpperCase() + genres.get(i).substring(1));
            }
            vh.showGenres.setText(android.text.TextUtils.join(", ", genres));
        }

        // Quick recycle for search query
        if (show.show.airs != null) {
            DateTimeZone dtz = DateTimeZone.forID(show.show.airs.timezone);
            String airs = String.format("%s | %s@%s %s",
                    show.show.network,
                    show.show.airs.day.substring(0, 3),
                    show.show.airs.time,
                    dtz.getShortName(DateTimeUtils.currentTimeMillis()));
            vh.showAirs.setText(airs);
        }

        vh.icTrakt.setImageResource(R.drawable.ic_trakt);
        Picasso.with(context).load(show.show.images.poster.thumb).into(vh.showPoster);
    }


    private void loadRatingsView(int i, ViewHolder vh) {
        vh.traktRating.setText(String.format("%.1f", showRatings.get(i).rating));
    }

    @Override
    public int getCount() {
        return shows.size();
    }

    @Override
    public Object getItem(int i) {
        return shows.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
