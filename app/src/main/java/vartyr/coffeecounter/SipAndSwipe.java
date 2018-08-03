package vartyr.coffeecounter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aerserv.sdk.*;
import com.aerserv.sdk.utils.UrlBuilder;
import com.amazon.device.ads.DTBAdResponse;

import java.util.List;

public class SipAndSwipe extends Activity {

    private AerServBanner banner;               // AS Banner
//    private List<DTBAdResponse> responses;      // A9 AD responses


    private GlobalClass globalVariable;
    private static String LOG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_and_swipe);

        globalVariable = (GlobalClass) getApplicationContext();     // Get app context for GV

//        LOG_TAG = globalVariable.LOG_TAG;
        LOG_TAG = "SIP";


        loadBanner();
    }



    protected AerServEventListener listener = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            SipAndSwipe.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case AD_FAILED:
                            if (args.size() > 1) {
                                Log.d(LOG_TAG, "AD FAILED / not loaded." + globalVariable.getSupportA9()
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED, no other info");
                            }
                        case LOAD_TRANSACTION:
                            if (args.size() >= 1) {
//                                ti = (AerServTransactionInformation) args.get(0);
                                Log.d(LOG_TAG, "Load Transaction Information PLC has:" + args.get(0));
//                                        + "\n buyerName=" + ti.getBuyerName()
//                                        + "\n buyerPrice=" + ti.getBuyerPrice());
                            }
                            else {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has no information");
                            }
                            break;
                        case AD_IMPRESSION:
                            Log.d(LOG_TAG, "AD IMPRESSION");
                            break;
                        case AD_LOADED:
                            Log.d(LOG_TAG, "AD loaded");

                            break;
                    }
                }
            });
        }
    };


    public void loadBanner() {
        final AerServConfig config = new AerServConfig(this, globalVariable.DEFAULT_300X250TEST_PLC)
                .setEventListener(listener)
                // .setA9AdResponses(null)
                .setPubKeys(globalVariable.getPubKeys());
        banner = findViewById(R.id.bannerSwipe);
        banner.configure(config);
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



