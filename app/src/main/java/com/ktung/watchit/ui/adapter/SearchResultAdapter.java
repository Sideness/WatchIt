package com.ktung.watchit.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.entities.BaseEntity;
import com.ktung.watchit.api.trakt.entities.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {
    protected static class ViewHolder {
        @Bind(R.id.poster)
        public ImageView poster;
        @Bind(R.id.title)
        public TextView title;
        @Bind(R.id.overview)
        public TextView overview;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public SearchResultAdapter(Context context, List<SearchResult> searchResults) {
        super(context, 0, searchResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        SearchResult result = getItem(position);
        BaseEntity item = getItem(result);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.it_search_result, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.title.setText(item.title);
        vh.overview.setText(item.overview);
        Picasso.with(getContext()).load(item.images.poster.thumb).into(vh.poster);

        return convertView;
    }

    private BaseEntity getItem(SearchResult result) {
        if (result.movie != null) {
            return result.movie;
        }

        if (result.show != null) {
            return result.show;
        }

        return null;
    }
}

