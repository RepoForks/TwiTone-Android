package com.ladwa.aditya.twitone.mainscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Button;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.login.LoginActivity;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import twitter4j.AsyncTwitter;


/**
 * This is the Main Fragment that users will see once they open the app
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements MainScreenContract.View {

    @Inject
    SharedPreferences preferences;

    @BindView(R.id.twitter_logout_button)
    Button logoutbutton;
    @Inject
    AsyncTwitter mTwitter;

    private boolean mLogin;
    private MainScreenContract.Presenter mPresenter;

    public MainScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);
        mLogin = preferences.getBoolean(getString(R.string.pref_login), false);
        new MainScreenPresenter(this, mLogin);


        return view;
    }

    @OnClick(R.id.twitter_logout_button)
    void onClickLogout() {
        logout();

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TwitoneApp.getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.pref_login), false);
        editor.apply();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            CookieManager.getInstance().removeAllCookie();
        } else {
            CookieManager.getInstance().removeAllCookies(null);
        }
        mTwitter.setOAuthAccessToken(null);
        mTwitter.shutdown();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void setPresenter(MainScreenContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
