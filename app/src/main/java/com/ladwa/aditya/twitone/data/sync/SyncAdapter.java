package com.ladwa.aditya.twitone.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.ladwa.aditya.twitone.R;

import timber.log.Timber;

/**
 * A {@link SyncAdapter} class to Sync data
 * Created by Aditya on 23-Jul-16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String ACCOUNT_TYPE = "com.ladwa.aditya.twitone";
    public static final String ACCOUNT = "Twitone";

    public static final int SYNC_INTERVAL = 20;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Timber.d("Sync is performed");
    }

    public static Account getSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);


        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "pass", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }


    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().syncPeriodic(syncInterval, flexTime)
                    .setExtras(new Bundle())
                    .setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);

        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account account, Context context) {
        ContentResolver.setIsSyncable(account, context.getString(R.string.content_authority), 1);

        ContentResolver.setSyncAutomatically(account, context.getString(R.string.content_authority), true);

        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);


        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}