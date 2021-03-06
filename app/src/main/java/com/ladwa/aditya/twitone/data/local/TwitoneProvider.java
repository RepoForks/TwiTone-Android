package com.ladwa.aditya.twitone.data.local;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * A Content Provider for the App
 * Created by Aditya on 06-Jul-16.
 */
public class TwitoneProvider extends ContentProvider {

    private static final int USER_ITEM = 100;
    private static final int USER_DIR = 101;

    private static final int TWEET_ITEM = 200;
    private static final int TWEET_DIR = 201;

    private static final int INTERACTION_ITEM = 300;
    private static final int INTERACTION_DIR = 301;

    public static final int DIRECT_MESSAGE_ITEM = 400;
    public static final int DIRECT_MESSAGE_DIR = 401;

    public static final int TRENDS_ITEM = 500;
    public static final int TRENDS_DIR = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TwitterDbHelper mTwitterDbHelper;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TwitterContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, TwitterContract.PATH_USER + "/#", USER_ITEM);
        matcher.addURI(authority, TwitterContract.PATH_USER, USER_DIR);

        matcher.addURI(authority, TwitterContract.PATH_TWEET + "/#", TWEET_ITEM);
        matcher.addURI(authority, TwitterContract.PATH_TWEET, TWEET_DIR);

        matcher.addURI(authority, TwitterContract.PATH_INTERACTION + "/#", INTERACTION_ITEM);
        matcher.addURI(authority, TwitterContract.PATH_INTERACTION, INTERACTION_DIR);

        matcher.addURI(authority, TwitterContract.PATH_DIRECT_MESSAGE + "/#", DIRECT_MESSAGE_ITEM);
        matcher.addURI(authority, TwitterContract.PATH_DIRECT_MESSAGE, DIRECT_MESSAGE_DIR);

        matcher.addURI(authority, TwitterContract.PATH_TRENDS + "/#", TRENDS_ITEM);
        matcher.addURI(authority, TwitterContract.PATH_TRENDS, TRENDS_DIR);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mTwitterDbHelper = new TwitterDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case USER_ITEM:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.User.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USER_DIR:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.User.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            // Case for Tweets
            case TWEET_ITEM:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.Tweet.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TWEET_DIR:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.Tweet.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            //Case for Interaction
            case INTERACTION_ITEM:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.Interaction.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case INTERACTION_DIR:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.Interaction.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            //Case for Direct message
            case DIRECT_MESSAGE_ITEM:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.DirectMessage.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case DIRECT_MESSAGE_DIR:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.DirectMessage.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            //Case for Trends
            case TRENDS_ITEM:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.Trends.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TRENDS_DIR:
                retCursor = mTwitterDbHelper.getReadableDatabase().query(
                        TwitterContract.Trends.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            //Case for user
            case USER_ITEM:
                return TwitterContract.User.CONTENT_USER_ITEM_TYPE;
            case USER_DIR:
                return TwitterContract.User.CONTENT_USER_TYPE;

            //Case for Tweet
            case TWEET_ITEM:
                return TwitterContract.Tweet.CONTENT_TWEET_ITEM_TYPE;
            case TWEET_DIR:
                return TwitterContract.Tweet.CONTENT_TWEET_TYPE;

            //Case for Interaction
            case INTERACTION_ITEM:
                return TwitterContract.Interaction.CONTENT_INTERACTION_ITEM_TYPE;
            case INTERACTION_DIR:
                return TwitterContract.Interaction.CONTENT_INTERACTION_TYPE;

            //Case for Direct message
            case DIRECT_MESSAGE_ITEM:
                return TwitterContract.DirectMessage.CONTENT_INTERACTION_ITEM_TYPE;
            case DIRECT_MESSAGE_DIR:
                return TwitterContract.DirectMessage.CONTENT_INTERACTION_TYPE;

            //Case for Trends
            case TRENDS_ITEM:
                return TwitterContract.Trends.CONTENT_INTERACTION_ITEM_TYPE;
            case TRENDS_DIR:
                return TwitterContract.Trends.CONTENT_INTERACTION_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mTwitterDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            //Case for User
            case USER_DIR:
                long _id = db.insert(TwitterContract.User.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TwitterContract.User.buildUserUri(_id);
                else
                    throw new SQLException("Failed to insert row " + uri);
                break;

            //Case for Tweet
            case TWEET_DIR:
                _id = db.insert(TwitterContract.Tweet.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TwitterContract.Tweet.buildTweetUri(_id);
                else
                    throw new SQLException("Failed to insert row " + uri);
                break;

            //Case for Interaction
            case INTERACTION_DIR:
                _id = db.insert(TwitterContract.Interaction.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TwitterContract.Interaction.buildTweetUri(_id);
                else
                    throw new SQLException("Failed to insert row " + uri);
                break;

            //Case for Direct message
            case DIRECT_MESSAGE_DIR:
                _id = db.insert(TwitterContract.DirectMessage.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TwitterContract.DirectMessage.buildTweetUri(_id);
                else
                    throw new SQLException("Failed to insert row " + uri);
                break;

            //Case for Trends
            case TRENDS_DIR:
                _id = db.insert(TwitterContract.Trends.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TwitterContract.Trends.buildTrendUri(_id);
                else
                    throw new SQLException("Failed to insert row " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mTwitterDbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case USER_DIR:
                rowsDeleted = db.delete(TwitterContract.User.TABLE_NAME, selection, selectionArgs);
                break;
            case INTERACTION_DIR:
                rowsDeleted = db.delete(TwitterContract.Interaction.TABLE_NAME, selection, selectionArgs);
                break;
            case TWEET_DIR:
                rowsDeleted = db.delete(TwitterContract.Tweet.TABLE_NAME, selection, selectionArgs);
                break;
            case DIRECT_MESSAGE_DIR:
                rowsDeleted = db.delete(TwitterContract.DirectMessage.TABLE_NAME, selection, selectionArgs);
                break;
            case TRENDS_DIR:
                rowsDeleted = db.delete(TwitterContract.Trends.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }

        if (selection == null || 0 != rowsDeleted)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mTwitterDbHelper.getWritableDatabase();
        int update;
        switch (sUriMatcher.match(uri)) {
            //Case for User
            case USER_DIR:
                update = db.update(TwitterContract.User.TABLE_NAME, values, selection, selectionArgs);
                break;

            //Case for Tweet
            case TWEET_DIR:
                update = db.update(TwitterContract.Tweet.TABLE_NAME, values, selection, selectionArgs);
                break;

            //Case for Interaction
            case INTERACTION_DIR:
                update = db.update(TwitterContract.Interaction.TABLE_NAME, values, selection, selectionArgs);
                break;

            //Case for Direct message
            case DIRECT_MESSAGE_DIR:
                update = db.update(TwitterContract.DirectMessage.TABLE_NAME, values, selection, selectionArgs);
                break;
            //Case for Trends message
            case TRENDS_DIR:
                update = db.update(TwitterContract.Trends.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
        if (update > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return update;

    }
}
