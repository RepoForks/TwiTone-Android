package com.ladwa.aditya.twitone.ui.trends;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;
import com.ladwa.aditya.twitone.data.local.models.Trend;

import java.util.List;

/**
 * A Contract class for Trends
 * Created by Aditya on 22-Jul-16.
 */
public interface TrendsContract {

    interface View extends BaseView<Presenter> {
        void loadedTrends(List<Trend> trendList);

        void stopRefreshing();

        void showRefreshing();

        void showError();
    }

    interface Presenter extends BasePresenter {
        void loadTrends();

        void loadLocalTrends();

        void refreshRemoteTrends();

        void refreshRemoteLocalTrends();
    }
}
