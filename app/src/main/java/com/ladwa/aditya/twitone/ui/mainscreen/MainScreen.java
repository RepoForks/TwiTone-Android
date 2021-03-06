package com.ladwa.aditya.twitone.ui.mainscreen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ladwa.aditya.twitone.BaseActivity;
import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.sync.SyncAdapter;
import com.ladwa.aditya.twitone.ui.interactions.Interactions;
import com.ladwa.aditya.twitone.ui.message.Message;
import com.ladwa.aditya.twitone.ui.settings.SettingsActivity;
import com.ladwa.aditya.twitone.ui.settings.SettingsRepository;
import com.ladwa.aditya.twitone.ui.trends.Trends;
import com.ladwa.aditya.twitone.ui.tweet.Tweet;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This is The Launcher Activity of the App
 */
public class MainScreen extends BaseActivity implements
        Drawer.OnDrawerItemClickListener, MainScreenFragment.DrawerCallback {

    @BindView(R.id.tweet_fab) FloatingActionButton mTweetButton;


    private Toolbar toolbar;
    private AccountHeader headerResult;
    private ProfileDrawerItem profileDrawerItem;
    private Drawer result;
    private PrimaryDrawerItem timeline;
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTweetButton.setImageDrawable(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_twitter)
                .color(getResources().getColor(R.color.md_blue_700)));

        //Setup google Analytics Tracking
        mTracker = TwitoneApp.getDefaultTracker();

        //Setup Account for Sync
        SyncAdapter.initializeSyncAdapter(this);

        animateFab();
    }

    private void animateFab() {
        mTweetButton.setScaleX(0);
        mTweetButton.setScaleY(0);
        mTweetButton.animate().scaleX(1).scaleY(1).start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.tweet_fab)
    public void onClickTweetButton() {
        //On click tweet button
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, mTweetButton, mTweetButton.getTransitionName());
        startActivity(new Intent(this, Tweet.class), options.toBundle());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName(MainScreen.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void setupDrawer(String screenName) {

        timeline = new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_reorder).
                withIdentifier(1).withName(R.string.drawer_timeline).withSetSelected(true);
        final PrimaryDrawerItem interaction = new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_people)
                .withIdentifier(2).withName(R.string.drawer_interaction);
        final PrimaryDrawerItem message = new PrimaryDrawerItem()
                .withIcon(GoogleMaterial.Icon.gmd_question_answer).withIdentifier(3).withName(R.string.drawer_message);
        final PrimaryDrawerItem trends = new PrimaryDrawerItem()
                .withIcon(GoogleMaterial.Icon.gmd_trending_up).withIdentifier(4).withName(R.string.drawer_trends);
        final PrimaryDrawerItem settings = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_setting);
        final PrimaryDrawerItem logOut = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.drawer_logout);

        profileDrawerItem = new ProfileDrawerItem()
                .withName(screenName)
                .withEmail("Email");

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg_message_to_dark)
                .addProfiles(profileDrawerItem).build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        timeline,
                        interaction,
                        message,
                        trends,
                        new DividerDrawerItem(),
                        settings,
                        logOut
                )
                .withOnDrawerItemClickListener(this)
                .build();


    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        switch (position) {

            case 2:
                startActivity(new Intent(MainScreen.this, Interactions.class));
                break;
            case 3:
                startActivity(new Intent(MainScreen.this, Message.class));
                break;
            case 4:
                startActivity(new Intent(MainScreen.this, Trends.class));
                break;
            case 6:
                startActivity(new Intent(MainScreen.this, SettingsActivity.class));
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 7:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
                builder.setTitle(R.string.logout_dialog_title);
                builder.setMessage(R.string.logout_dialog_message);

                builder.setPositiveButton(R.string.logout_dialog_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainScreenFragment mainScreenFragment = (MainScreenFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_mainscreen);
                        mainScreenFragment.logout();

                    }
                });

                builder.setNegativeButton(R.string.logout_dialog_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
        }
        return false;
    }


    @Override
    public void setProfile(String screenName) {
        setupDrawer(screenName);
    }

    @Override
    public void updateProfile(User user) {
        profileDrawerItem.withName(user.getName()).withEmail(user.getScreenName()).withIcon(user.getProfileUrl());
        ImageView headerBackgroundView = headerResult.getHeaderBackgroundView();
        Glide.with(this).load(user.getBannerUrl()).into(headerBackgroundView);
        headerResult.updateProfile(profileDrawerItem);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SettingsRepository.destroyInstance();
    }
}
