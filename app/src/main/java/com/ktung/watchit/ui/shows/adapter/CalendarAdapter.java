package com.ktung.watchit.ui.shows.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.entities.CalendarShowEntry;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalendarAdapter extends BaseAdapter {
    protected static class ViewHolder {
        @Bind(R.id.showPoster)
        public ImageView showPoster;
        @Bind(R.id.showTitle)
        public TextView showTitle;
        @Bind(R.id.showDate)
        public TextView showDate;
        @Bind(R.id.showEpisode)
        public TextView showEpisode;
        @Bind(R.id.showAirs)
        public TextView showAirs;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private Context context;
    private List<CalendarShowEntry> shows;
    private LayoutInflater inflater;

    public CalendarAdapter(Context context, List<CalendarShowEntry> shows) {
        this.context = context;
        this.shows = shows;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.it_shows_calendar, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        CalendarShowEntry show = shows.get(position);
        loadView(viewHolder, show);

        return view;
    }

    private void loadView(ViewHolder viewHolder, CalendarShowEntry show) {
        String episodeStr = String.format("S%dE%d - %s", show.episode.season, show.episode.number, show.episode.title);
        DateTimeZone dtz = DateTimeZone.forID(show.show.airs.timezone);

        String airs = String.format("%s | %s@%s %s",
                show.show.network,
                show.show.airs.day.substring(0, 3),
                show.show.airs.time,
                dtz.getShortName(DateTimeUtils.currentTimeMillis()));

        viewHolder.showTitle.setText(show.show.title);
        viewHolder.showDate.setText(String.format(
            "%s %s : %s",
            show.first_aired.dayOfMonth().getAsText(),
            show.first_aired.monthOfYear().getAsText(),
            show.first_aired.dayOfWeek().getAsText())
        );
        viewHolder.showEpisode.setText(episodeStr);
        viewHolder.showAirs.setText(airs);

        Picasso.with(context).load(show.show.images.poster.thumb).into(viewHolder.showPoster);

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
