package com.ktung.watchit.ui.shows.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Episode;
import com.ktung.watchit.api.trakt.entities.ShowResponse;
import com.ktung.watchit.api.trakt.enums.Extended;
import com.ktung.watchit.util.BaseObserver;
import com.ktung.watchit.util.CheckinOnClickListener;
import com.ktung.watchit.util.WatchedOnClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProgressAdapter extends BaseAdapter {
    protected static class ViewHolder {
        @Bind(R.id.showPoster)
        public ImageView showPoster;
        @Bind(R.id.showTitle)
        public TextView showTitle;
        @Bind(R.id.watchNext)
        public TextView watchNext;
        @Bind(R.id.progressBar)
        public ProgressBar progressBar;
        @Bind(R.id.checkinButton)
        public Button checkinButton;
        @Bind(R.id.watchedButton)
        public Button watchedButton;
        @Bind(R.id.showCertification)
        public TextView showCertification;
        @Bind(R.id.percentage)
        public TextView percentage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private Trakt trakt;
    private Context context;
    private List<ShowResponse> shows;
    private LayoutInflater inflater;

    public ProgressAdapter(Context context, List<ShowResponse> showResponseList) {
        trakt = Trakt.getInstance();
        this.context = context;
        this.shows = showResponseList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.it_shows_progress, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        ShowResponse show = shows.get(position);
        loadView(viewHolder, show);
        loadProgress(position, viewHolder);

        return view;
    }

    private void loadView(ViewHolder vh, ShowResponse show) {
        vh.showTitle.setText(show.show.title);
        vh.showCertification.setText(show.show.certification);

        Picasso.with(context).load(show.show.images.poster.thumb).into(vh.showPoster);
    }

    private void loadProgress(int position, ViewHolder vh) {
        ShowResponse show = shows.get(position);
        if (null == show.aired || null == show.completed) {
            trakt.shows().watchedProgress(show.show.ids.trakt.toString(), null, null, Extended.FULLIMAGES)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseObserver<ShowResponse>() {
                @Override
                public void onNext(ShowResponse showResponse) {
                    show.aired = showResponse.aired;
                    show.completed = showResponse.completed;
                    show.next_episode = showResponse.next_episode;
                    loadProgressView(vh, position);
                }
            });
        } else {
            loadProgressView(vh, position);
        }
    }

    private void loadProgressView(ViewHolder vh, int position) {
        ShowResponse show = shows.get(position);
        vh.progressBar.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        vh.progressBar.setMax(show.aired);
        vh.progressBar.setProgress(show.completed);
        int percentage = show.completed*100/show.aired ;
        vh.percentage.setText(String.format("%d%%", percentage));

        Episode nextEp = show.next_episode;
        if (nextEp != null) {
            String watchNextStr = String.format("S%dE%d - %s", nextEp.season, nextEp.number, nextEp.title);
            vh.watchNext.setText(watchNextStr);

            vh.checkinButton.setEnabled(true);
            vh.watchedButton.setEnabled(true);

            vh.checkinButton.setOnClickListener(new CheckinOnClickListener(context, nextEp));
            vh.watchedButton.setOnClickListener(new WatchedOnClickListener(context, nextEp));
        } else {
            vh.watchNext.setText("");
            vh.checkinButton.setEnabled(false);
            vh.watchedButton.setEnabled(false);
        }
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
