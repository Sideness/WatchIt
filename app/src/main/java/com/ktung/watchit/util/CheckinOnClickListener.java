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
import com.ktung.watchit.api.trakt.entities.EpisodeCheckinRequest;
import com.ktung.watchit.api.trakt.entities.EpisodeCheckinResponse;

import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckinOnClickListener implements View.OnClickListener {
    private Context context;
    private Episode episode;
    private boolean isCheckin = false;
    private BaseObserver<Response> delResponse;
    private BaseObserver<EpisodeCheckinResponse> epResponse;

    public CheckinOnClickListener(Context context, Episode episode) {
        this.context = context;
        this.episode = episode;

        loadObserver();
    }

    private void loadObserver() {
        delResponse = new BaseObserver<Response>() {
            @Override
            public void onNext(Response response) {
                if (204 == response.getStatus()) {
                    isCheckin = false;
                    Handler handler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            Utils.toaster(context, R.string.uncheckin, Toast.LENGTH_SHORT);
                        }
                    };
                    Message msg = handler.obtainMessage();
                    msg.sendToTarget();
                }
            }
        };

        epResponse = new BaseObserver<EpisodeCheckinResponse>() {
            @Override
            public void onNext(EpisodeCheckinResponse response) {
                isCheckin = true;
                String str = String.format("%s %s S%dE%d",
                        context.getString(R.string.checkin),
                        response.show.title,
                        response.episode.season,
                        response.episode.number);

                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Utils.toaster(context, str, Toast.LENGTH_SHORT);
                    }
                };
                Message message = handler.obtainMessage();
                message.sendToTarget();
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (isCheckin) {
            Trakt.getInstance().checkin().deleteActiveCheckin()
            .observeOn(Schedulers.newThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(delResponse);
        } else {
            EpisodeCheckinRequest request = new EpisodeCheckinRequest.Builder(episode).build();

            Trakt.getInstance().checkin().checkin(request)
            .observeOn(Schedulers.newThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(epResponse);
        }
    }
}
