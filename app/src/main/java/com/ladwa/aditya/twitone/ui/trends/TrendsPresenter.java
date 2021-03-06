package com.ladwa.aditya.twitone.ui.trends;

import android.content.Context;
import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.models.Trend;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A Presenter for Trends
 * Created by Aditya on 22-Jul-16.
 */
public class TrendsPresenter implements TrendsContract.Presenter {


    @Inject SharedPreferences preferences;
    @Inject TwitterRemoteDataSource mTwitterRemoteDataSource;

    private TrendsContract.View mView;
    private TwitterRepository mTwitterRepository;
    private String mTAG;
    private Context mContext;

    public TrendsPresenter(TrendsContract.View mView, TwitterRepository mTwitterRepository, String TAG, Context context) {
        this.mView = mView;
        this.mTwitterRepository = mTwitterRepository;
        this.mTAG = TAG;
        this.mContext = context;
        mView.setPresenter(this);

        TwitoneApp.getTwitterComponent().inject(TrendsPresenter.this);
    }

    @Override
    public void subscribe() {
        if (Objects.equals(mTAG, "Global"))
            loadTrends();
        if (Objects.equals(mTAG, "Local"))
            loadLocalTrends();
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadTrends() {
        mTwitterRepository.getTrends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Trend>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onNext(List<Trend> trendList) {
                        mView.loadedTrends(trendList);
                    }
                });
    }

    @Override
    public void loadLocalTrends() {
        double latitude = Double.valueOf(preferences.getString(mContext.getString(R.string.pref_user_location_latitude), "0"));
        double longitude = Double.valueOf(preferences.getString(mContext.getString(R.string.pref_user_location_longitude), "0"));
        mTwitterRepository.getLocalTrends(latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Trend>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onNext(List<Trend> trendList) {
                        mView.loadedTrends(trendList);
                    }
                });

    }

    @Override
    public void refreshRemoteTrends() {
        mTwitterRemoteDataSource.getTrends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Trend>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onNext(List<Trend> trendList) {
                        mView.loadedTrends(trendList);
                    }
                });
    }

    @Override
    public void refreshRemoteLocalTrends() {
        double latitude = Double.valueOf(preferences.getString(mContext.getString(R.string.pref_user_location_latitude), "0"));
        double longitude = Double.valueOf(preferences.getString(mContext.getString(R.string.pref_user_location_longitude), "0"));
        mTwitterRemoteDataSource.getLocalTrends(latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Trend>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onNext(List<Trend> trendList) {
                        mView.loadedTrends(trendList);
                    }
                });
    }
}
