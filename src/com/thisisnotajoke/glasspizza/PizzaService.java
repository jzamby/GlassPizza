package com.thisisnotajoke.glasspizza;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.util.Log;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import java.util.ArrayList;

public class PizzaService extends Service {
    private static final String LIVE_CARD_ID = "pizza";

    public class PizzaBinder extends Binder {
    }

    private TimelineManager mTimelineManager;
    private LiveCard mLiveCard;
    private LocationManager mLocationManager;

    @Override
    public int onStartCommand(Intent workIntent, int flags, int startId) {
        ArrayList<String> voiceResults = workIntent.getExtras()
                .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        if (mLiveCard == null) {
            Log.d("pizza", "service started");
            Log.d("pizza", voiceResults.toString());
            //mLocationManager.requestSingleUpdate(Criteria.ACCURACY_FINE, null);
            
            mLiveCard = mTimelineManager.getLiveCard(LIVE_CARD_ID);
            mLiveCard.setNonSilent(true);
            mLiveCard.publish();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimelineManager = TimelineManager.from(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d("pizza", "created");
    }
    
    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }

        mLocationManager = null;

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new PizzaBinder();

}