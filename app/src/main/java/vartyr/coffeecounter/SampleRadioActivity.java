package vartyr.coffeecounter;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServTransactionInformation;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SampleRadioActivity extends AppCompatActivity {

    private AerServBanner bannerMREC;               // AS Banner
    private AerServBanner bannerBottom;               // AS Banner
    private AdManager globalVariable;         // To grab VC or anything we need
    private static String LOG_TAG;              // Log tag
    private Boolean isActivityCreated = false;          // to know whether we've just created the activity
    private Boolean isOnScreen;                 // to let us control the delay


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_radio);

        globalVariable = (AdManager) getApplicationContext();     // Get an instance of the singleton class before anything else is done
        LOG_TAG = globalVariable.LOG_TAG;                           // Save LOG_TAG since we frequently access

        loadMRECBanner();
        loadBottomBanner();

        isActivityCreated = true;
        isOnScreen = true;

    }

    // Set up a listener to listen to incoming AS events
    protected AerServEventListener listenerMREC = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            SampleRadioActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case AD_FAILED:
                            if (args.size() > 0) {
                                Log.d(LOG_TAG, "AD FAILED for MREC"
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED for MREC, no other info");
                            }
                            break;
                        case LOAD_TRANSACTION:
                            if (args.size() >= 1) {
                                Log.d(LOG_TAG, "Load Transaction Information PLC (MREC) has:" + args.get(0));
                            } else {
                                Log.d(LOG_TAG, "Load Transaction Information PLC (MREC) has no information");
                            }
                            break;
                        case AD_IMPRESSION:
                            Log.d(LOG_TAG, "AD IMPRESSION for MREC ");
                            break;
                        case AD_LOADED:
                            Log.d(LOG_TAG, "AD LOADED for MREC");
                            break;
                    }
                }
            });


        }
    };

    // Set up a listener to listen to incoming AS events
    protected AerServEventListener listenerBanner = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            SampleRadioActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case AD_FAILED:
                            if (args.size() > 0) {
                                Log.d(LOG_TAG, "AD FAILED for bottomBanner"
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED for bottomBanner, no other info");
                            }
                            break;
                        case LOAD_TRANSACTION:
                            if (args.size() >= 1) {
                                Log.d(LOG_TAG, "Load Transaction Information PLC (bottomBanner) has:" + args.get(0));
                            } else {
                                Log.d(LOG_TAG, "Load Transaction Information PLC (bottomBanner) has no information");
                            }
                            break;
                        case AD_IMPRESSION:
                            Log.d(LOG_TAG, "AD IMPRESSION for bottomBanner ");
                            break;
                        case AD_LOADED:
                            Log.d(LOG_TAG, "AD LOADED for bottomBanner");
                            break;
                    }
                }
            });


        }
    };


    public void loadMRECBanner() {
        Log.d(LOG_TAG, "CONFIGURING MREC NOW");

        if (bannerMREC != null) {
            Log.d(LOG_TAG, "KILLING PREVIOUS MREC BANNER");
            bannerMREC.kill();
            bannerMREC = null;
        }

        final AerServConfig config = new AerServConfig(this, globalVariable.DEFAULT_300X250TEST_PLC)
                .setEventListener(listenerMREC)
                .setDebug(true)
                .setRefreshInterval(15)
                .setPubKeys(globalVariable.getPubKeys());
        bannerMREC = findViewById(R.id.top_radio_banner);
        bannerMREC.configure(config);
        bannerMREC.show();
    }


    public void loadBottomBanner() {
        Log.d(LOG_TAG, "CONFIGURING Bottom Banner NOW");

        if (bannerBottom != null) {
            Log.d(LOG_TAG, "KILLING PREVIOUS Bottom BANNER");
            bannerBottom.kill();
            bannerBottom = null;
        }

        final AerServConfig config = new AerServConfig(this, globalVariable.DEFAULT_AD_PLC)
                .setEventListener(listenerBanner)
                .setDebug(true)
                .setPubKeys(globalVariable.getPubKeys());
        bannerBottom = findViewById(R.id.bottom_radio_banner);
        bannerBottom.configure(config);
        bannerBottom.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);

        if (isActivityCreated){
            if (hasFocus){
                startAllRefreshWithDelay(7000);
                isOnScreen = true;
            } else {
                isOnScreen = false;
                stopAllRefresh();
            }
        } else {
            Log.d(LOG_TAG, "onWindowFocusChanged - it is a trap, send no reply");
        }
    }

    public void stopAllRefresh() {
        bannerMREC.pause();
        bannerBottom.pause();
        bannerBottom.kill();
        Log.d(LOG_TAG, "STOP ALL REFRESH");
    }


    public void startAllRefreshWithDelay(final int delay) {

        // Start the MREC refresh. (this is technically taken care of by SDK)
        // bannerMREC.play();

        Log.d(LOG_TAG, "bannerMREC started refreshing, waiting for delay: " + delay);

        final Handler handler = new Handler();

        new Timer().schedule(new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {

                        if (isOnScreen){
                            loadBottomBanner();
                            Log.d(LOG_TAG, "bannerBottom started refreshing with delay: " + delay);
                        } else {
                            Log.d(LOG_TAG, "bannerBottom will not refresh, not on screen");
                            // bannerBottom.pause(); // you never know...
                        }

                    }

                });
            }


        }, delay);
    }

}
