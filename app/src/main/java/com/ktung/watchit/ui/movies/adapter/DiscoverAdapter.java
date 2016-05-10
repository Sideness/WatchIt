package com.ktung.watchit.ui.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.entities.Movie;
import com.ktung.watchit.api.trakt.entities.MovieResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DiscoverAdapter extends ArrayAdapter {
    public static final int NB_MAX_GENRES = 3;

    protected static class ViewHolder {
        @Bind(R.id.poster)
        public ImageView poster;
        @Bind(R.id.title)
        public TextView title;
        @Bind(R.id.genres)
        public TextView genres;
        @Bind(R.id.released)
        public TextView released;
        @Bind(R.id.icTrakt)
        public ImageView icTrakt;
        @Bind(R.id.traktRating)
        public TextView traktRating;
        @Bind(R.id.certification)
        public TextView certification;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private Context context;

    public DiscoverAdapter(Context context, List<MovieResponse> movies) {
        super(context, 0, movies);
        this.context = context;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.it_movie_discover, viewGroup, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MovieResponse movie = (MovieResponse) getItem(i);
        loadView(vh, movie);

        return convertView;
    }

    private void loadView(ViewHolder vh, MovieResponse movieResponse) {
        Movie movie = movieResponse.movie;

        vh.title.setText(movie.title);
        vh.certification.setText(movie.certification);
        vh.traktRating.setText(String.format("%.1f", movie.rating));
        vh.released.setText(movie.released.toString("YYYY-MM-dd"));

        // Quick recycle for search query
        if (movie.genres != null) {
            int nbMaxGenres = (movie.genres.size() > NB_MAX_GENRES) ? NB_MAX_GENRES : movie.genres.size();
            List<String> genres = movie.genres.subList(0, nbMaxGenres);
            for (int i = 0; i < genres.size(); ++i) {
                genres.set(i, genres.get(i).substring(0, 1).toUpperCase() + genres.get(i).substring(1));
            }
            vh.genres.setText(android.text.TextUtils.join(", ", genres));
        }

        vh.icTrakt.setImageResource(R.drawable.ic_trakt);
        Picasso.with(context).load(movie.images.poster.thumb).into(vh.poster);
    }
}
