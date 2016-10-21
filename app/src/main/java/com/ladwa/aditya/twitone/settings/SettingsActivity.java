package com.ladwa.aditya.twitone.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;

import com.ladwa.aditya.twitone.BaseActivity;
import com.ladwa.aditya.twitone.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setupWindowAnimations();

    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }
}
