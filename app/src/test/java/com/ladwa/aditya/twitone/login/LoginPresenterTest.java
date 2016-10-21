package com.ladwa.aditya.twitone.login;

import com.ladwa.aditya.twitone.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import twitter4j.AsyncTwitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Aditya on 20-Oct-16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Observable.class, AndroidSchedulers.class, RequestToken.class})
public class LoginPresenterTest {
    private LoginPresenter mLoginPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() throws Exception {
        mLoginPresenter = spy(new LoginPresenter(mock(LoginContract.View.class), mock(AsyncTwitter.class)));

    }

    @Test
    public void testShouldScheduleRequestTokenFromBackgroundThread() {
        Observable<RequestToken> requestTokenObservable = (Observable<RequestToken>) mock(Observable.class);

        Subscription subscription = mock(Subscription.class);

        when(mLoginPresenter.getRequestTokenObservable()).thenReturn(requestTokenObservable);
        when(requestTokenObservable.subscribeOn(Schedulers.newThread())).thenReturn(requestTokenObservable);
        when(requestTokenObservable.observeOn(AndroidSchedulers.mainThread())).thenReturn(requestTokenObservable);
        when(requestTokenObservable.subscribe()).thenReturn(subscription);

        mLoginPresenter.login();

        verify(mLoginPresenter).getRequestTokenObservable();
        verify(requestTokenObservable).subscribeOn(Schedulers.io());
        verify(requestTokenObservable).observeOn(AndroidSchedulers.mainThread());
        verify(requestTokenObservable).subscribe(Matchers.<Subscriber<RequestToken>>any());
        verify(subscription);

    }

    @Test
    public void testShouldScheduleAccessTokenFromBackgroundThread() {
        Observable<AccessToken> accessTokenObservable = (Observable<AccessToken>) mock(Observable.class);

        when(mLoginPresenter.getAccessTokenObservable(mock(String.class))).thenReturn(accessTokenObservable);
        when(accessTokenObservable.subscribeOn(Schedulers.newThread())).thenReturn(accessTokenObservable);
        when(accessTokenObservable.observeOn(AndroidSchedulers.mainThread())).thenReturn(accessTokenObservable);

        mLoginPresenter.getAccessToken(mock(String.class));

        verify(mLoginPresenter).getAccessTokenObservable(mock(String.class));
        verify(accessTokenObservable).subscribeOn(Schedulers.io());
        verify(accessTokenObservable).observeOn(AndroidSchedulers.mainThread());
        verify(accessTokenObservable).subscribe(Matchers.<Subscriber<AccessToken>>any());

    }


}
