package com.ktung.watchit.ui.shows.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Ratings;
import com.ktung.watchit.api.trakt.entities.SyncItems;
import com.ktung.watchit.api.trakt.entities.SyncResponse;
import com.ktung.watchit.api.trakt.entities.SyncShow;
import com.ktung.watchit.api.trakt.entities.WatchlistShow;
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

public class WatchlistAdapter extends BaseAdapter implements View.OnClickListener {

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
        @Bind(R.id.removeButton)
        public Button removeButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private Trakt trakt;
    private Context context;
    private List<WatchlistShow> watchlistShows;
    private Map<Integer, Ratings> showRatings;
    private LayoutInflater inflater;

    public WatchlistAdapter(Context context, List<WatchlistShow> watchlistShows) {
        trakt = Trakt.getInstance();
        this.context = context;
        this.watchlistShows = watchlistShows;
        showRatings = new HashMap<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.it_shows_watchlist, viewGroup, false);
            viewHolder = new ViewHolder(view);
            viewHolder.removeButton.setTag(i);
            viewHolder.removeButton.setOnClickListener(this);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        WatchlistShow response = watchlistShows.get(i);
        loadView(viewHolder, response);
        loadShowRatings(i, viewHolder);

        return view;
    }

    private void loadView(ViewHolder vh, WatchlistShow show) {
        vh.showTitle.setText(show.show.title);
        vh.showCertification.setText(show.show.certification);

        int nbMaxGenres = (show.show.genres.size() > NB_MAX_GENRES) ? NB_MAX_GENRES : show.show.genres.size();
        List<String> genres = show.show.genres.subList(0, nbMaxGenres);
        for (int i = 0; i < genres.size(); ++i) {
            genres.set(i, genres.get(i).substring(0, 1).toUpperCase() + genres.get(i).substring(1));
        }
        vh.showGenres.setText(android.text.TextUtils.join(", ", genres));

        DateTimeZone dtz = DateTimeZone.forID(show.show.airs.timezone);

        if(show.show.airs.day != null) {
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

    private void loadShowRatings(int i, ViewHolder viewHolder) {
        WatchlistShow show = watchlistShows.get(i);

        if (showRatings.isEmpty() || showRatings.get(i) == null) {
            trakt.shows().ratings(String.valueOf(show.show.ids.trakt))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<Ratings>() {
                @Override
                public void onNext(Ratings ratings) {
                    showRatings.put(i, ratings);
                    setTraktRating(i, viewHolder);
                }
            });
        } else {
            setTraktRating(i, viewHolder);
        }
    }

    private void setTraktRating(int i, ViewHolder viewHolder) {
        viewHolder.traktRating.setText(String.format("%.1f", showRatings.get(i).rating));
    }

    private void removeItem(int i) {
        watchlistShows.remove(i);
        showRatings.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Integer position = (Integer) view.getTag();
        SyncItems item = new SyncItems().shows(new SyncShow(watchlistShows.get(position).show.ids));

        trakt.sync().removeItemsFromWatchlist(item)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<SyncResponse>() {
            @Override
            public void onNext(SyncResponse syncResponse) {
                if (1 == syncResponse.deleted.shows) {
                    removeItem(position);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return watchlistShows.size();
    }

    @Override
    public Object getItem(int i) {
        return watchlistShows.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
