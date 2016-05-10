package com.ktung.watchit.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.ktung.watchit.R;
import com.ktung.watchit.api.trakt.Trakt;
import com.ktung.watchit.api.trakt.entities.Episode;
import com.ktung.watchit.api.trakt.entities.SyncEpisode;
import com.ktung.watchit.api.trakt.entities.SyncItems;
import com.ktung.watchit.api.trakt.entities.SyncResponse;

import org.joda.time.DateTime;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WatchedOnClickListener implements View.OnClickListener {
    private Context context;
    private Episode episode;
    private boolean isWatched = false;

    public WatchedOnClickListener(Context context, Episode episode) {
        this.context = context;
        this.episode = episode;
    }

    @Override
    public void onClick(View view) {
        SyncItems epItem = new SyncItems().episodes(new SyncEpisode(episode.ids).watchedAt(DateTime.now()));
        Observable<SyncResponse> request;

        if (isWatched) {
            request = Trakt.getInstance().sync().removeItemsFromHistory(epItem);
        } else {
            request = Trakt.getInstance().sync().addToWatchedHistory(epItem);
        }

        request
        .observeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<SyncResponse>() {
            @Override
            public void onNext(SyncResponse syncResponse) {
                String str;
                if (syncResponse.added != null) {
                    isWatched = true;
                    str = context.getString(R.string.watched);
                } else {
                    isWatched = false;
                    str = context.getString(R.string.unwatched);
                }

                String toastStr = String.format("%s S%dE%d - %s",
                    str,
                    episode.season,
                    episode.number,
                    episode.title);

                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Toast.makeText(context, toastStr, Toast.LENGTH_SHORT).show();
                    }
                };
                Message message = handler.obtainMessage();
                message.sendToTarget();
            }
        });
    }
}
