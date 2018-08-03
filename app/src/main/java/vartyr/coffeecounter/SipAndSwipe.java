package vartyr.coffeecounter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aerserv.sdk.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SipAndSwipe extends Activity {

    private AerServBanner banner;               // AS Banner

    private GlobalClass globalVariable;
    private static String LOG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_and_swipe);
        globalVariable = (GlobalClass) getApplicationContext();     // Get app context for GV
        LOG_TAG = globalVariable.LOG_TAG;
        configureLooper();
    }



    protected AerServEventListener listener = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            SipAndSwipe.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    String msg = "AerServEventListener: ";
                    switch (event) {
                        case AD_FAILED:
                            msg = msg + "AD_FAILED";
                            Toast.makeText(SipAndSwipe.this, msg, Toast.LENGTH_SHORT).show();
                            if (args.size() > 1) {
                                Log.d(LOG_TAG, "AD FAILED / not loaded."
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED, no other info");
                            }
                        case LOAD_TRANSACTION:
                            msg = msg + "LOAD_TRANSACTION";
                            Toast.makeText(SipAndSwipe.this, msg, Toast.LENGTH_SHORT).show();
                            if (args.size() >= 1) {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has:" + args.get(0));
                            }
                            else {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has no information");
                            }
                            break;
                        case AD_IMPRESSION:
                            msg = msg + "AD_IMPRESSION";
                            Toast.makeText(SipAndSwipe.this, msg, Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "AD IMPRESSION");
                            break;
                        case AD_LOADED:
                            msg = msg + "AD_LOADED";
                            Toast.makeText(SipAndSwipe.this, msg, Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "AD loaded");

                            break;
                    }
                }
            });
        }
    };



    public void configureLooper() {
        final long period = 30000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(LOG_TAG, " - Looper now calling load banner");
                TimerMethod();
            }
        }, 0, period);
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            loadBanner();
        }
    };



    public void loadBanner() {

        if (banner != null){
            banner.kill();      // kill the previous instance of the banner
        }

        String plc = globalVariable.getNextPLCForTest();

        final AerServConfig config = new AerServConfig(this, plc )
                .setEventListener(listener)
                .setPubKeys(globalVariable.getPubKeys());
        banner = findViewById(R.id.bannerSwipe);
        banner.configure(config);
        String msg = "[TEST] Now showing plc: " +  plc;
        Toast.makeText(SipAndSwipe.this, msg, Toast.LENGTH_SHORT).show();
        banner.show();
    }

    public void startMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    protected void onDestroy() {
        Log.d(LOG_TAG, "Sip & Swipe ACTIVITY CLEANUP");
        globalVariable.setCoffeeCount(100);
        super.onDestroy();

        // TODO: Do any sort of cleanup methods here

        if (banner != null) {
            banner.kill();
        }
    }
}



