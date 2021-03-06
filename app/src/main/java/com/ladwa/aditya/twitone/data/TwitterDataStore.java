package com.ladwa.aditya.twitone.data;

import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.data.local.models.Trend;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.data.local.models.User;

import java.util.List;

import rx.Observable;

/**
 * An interface that Encapsulates all the Operations which will be implemented by Local Database and Remote Twitter Service
 * Created by Aditya on 25-Jun-16.
 */
public interface TwitterDataStore {

    Observable<User> getUserInfo(long userID);

    Observable<List<Tweet>> getTimeLine(long sinceId);

    Observable<List<Interaction>> getInteraction(long sinceId);

    Observable<List<DirectMessage>> getDirectMessage(long sinceId);

    Observable<List<Trend>> getTrends();

    Observable<List<Trend>> getLocalTrends(double latitude, double longitude);

}
