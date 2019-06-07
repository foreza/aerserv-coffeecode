package vartyr.coffeecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServInterstitial;
import com.aerserv.sdk.AerServVirtualCurrency;


//import com.amazon.device.ads.*;

import java.util.List;

public class PreloadInterstitialActivity extends AppCompatActivity {

    private AerServInterstitial interstitial;   // AS Interstitial
//    private List<DTBAdResponse> responses;      // A9 AD responses

    private int INCREMENT_AMT = 0;
    private static String LOG_TAG;
    private AdManager globalVariable;


    // Set up a listener to listen to incoming events.
    private AerServEventListener listener = new AerServEventListener(){
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args){
            PreloadInterstitialActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "[coffeeIncrement]";
                    switch (event) {
                        case PRELOAD_READY:
                            globalVariable.CoffeeIncrementedInterstitialPreloaded = true;
                            setLoadedButtonVisible();
                            Log.d(LOG_TAG, "PreloadInterstitialActivity - Listener heard preload ready for interstitial");
                            break;
                        case VC_REWARDED:
                            AerServVirtualCurrency vc = (AerServVirtualCurrency) args.get(0);
                            Log.d(LOG_TAG, "VC rewarded: " + vc.getAmount() + " " + vc.getName());
                            INCREMENT_AMT = vc.getAmount().intValueExact();
                            setMessageOfCounter(INCREMENT_AMT);
                            msg = "You've obtained beans (" + INCREMENT_AMT+ ")";
                            Toast.makeText(PreloadInterstitialActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_incremented);


        // Save an instance of our singleton
        globalVariable = (AdManager) getApplicationContext();
        LOG_TAG = globalVariable.LOG_TAG;

        // Check to see if an interstitial is loaded already
        if (globalVariable.CoffeeIncrementedInterstitialPreloaded) {
            Log.d(LOG_TAG, "PreloadInterstitialActivity - Interstitial is already loaded do not load another");
            setLoadedButtonVisible();
        } else {
            Log.d(LOG_TAG, "PreloadInterstitialActivity - Interstitial is not loaded");
            setLoadedButtonInvisible();
            // Begin routine to load Interstitial.
            if (globalVariable.getSupportA9()) {
                preloadA9Interstitial();
            } else {
                preloadInterstitial();
            }
        }




    }


    // Stop the banner from doing anything
    @Override
    protected void onPause(){
        super.onPause();

        if (interstitial != null){
            interstitial.kill();
        }
        interstitial = null;
        globalVariable.CoffeeIncrementedInterstitialPreloaded = false;
        setLoadedButtonInvisible();

    }



    public void preloadInterstitial() {

        Log.d(LOG_TAG, "PreloadInterstitialActivity - preloadInterstitial called");


        final AerServConfig config = new AerServConfig(this, globalVariable.DEFAULT_INTERSTITIAL_PLC)
                .setDebug(true)
//                .setA9AdResponses(null)
                .setEventListener(listener)
                .setPreload(true)
                .setVerbose(true);

        interstitial = new AerServInterstitial(config);
    }

    public void setLoadedButtonVisible() {
        findViewById(R.id.button_coffee_showInterstitial).setVisibility(View.VISIBLE);
    }

    public void setLoadedButtonInvisible() {
        findViewById(R.id.button_coffee_showInterstitial).setVisibility(View.INVISIBLE);
    }



    public void preloadA9Interstitial() {
//
//        final DTBAdRequest loader = new DTBAdRequest();
//        loader.setSizes(new DTBAdSize.DTBInterstitialAdSize(globalVariable.A9_SLOT_INTERSTITIAL));
//        loader.loadAd(new DTBAdCallback() {
//            @Override
//
//            // If A9 fails to fill, call preloadInterstitial
//            public void onFailure(AdError adError) {
//                Log.e(LOG_TAG, "A9 - Failed to get interstitial ad from Amazon");
//                preloadInterstitial();
//            }
//
//            @Override
//            public void onSuccess(DTBAdResponse dtbAdResponse) {
//                responses = new ArrayList<DTBAdResponse>();
//                responses.add(dtbAdResponse);
//
//                Log.d(LOG_TAG, "A9 - Successfully got " + dtbAdResponse.getDTBAds().size()
//                        + " interstitial ad from Amazon");
//
//
//                final AerServConfig config = new AerServConfig(PreloadInterstitialActivity.this, globalVariable.DEFAULT_INTERSTITIAL_PLC)
//                        .setA9AdResponses(responses)
//                        .setEventListener(listener)
//                        .setPreload(true)
//                        .setPubKeys(globalVariable.getPubKeys());
//                interstitial = new AerServInterstitial(config);
//
//            }
//        });

    }



    // Show the interstitial only if the flag is set to true
    public void showInterstitial(View view) {
        if (globalVariable.CoffeeIncrementedInterstitialPreloaded) {
            if (interstitial != null) {
                interstitial.show();
                Log.d(LOG_TAG, "Interstitial shown in Coffee Incremented");
                globalVariable.CoffeeIncrementedInterstitialPreloaded = false;
                setLoadedButtonInvisible();
            }
            Log.d(LOG_TAG, "Interstitial is null");
        }  else
            Log.d(LOG_TAG, "Interstitial not ready / not shown in Coffee Incremented");
    }



    // This sets the amount of coffee beans just obtained
    public void setMessageOfCounter(int amount) {
        TextView textView = findViewById(R.id.coffeeCountView_Incremented);
        textView.setText(getString(R.string.coffee_bean_increment_amount, INCREMENT_AMT));
    }



    // On back, we want to send the incremented amount back to the MainActivity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitial != null && globalVariable.CoffeeIncrementedInterstitialPreloaded) {
            interstitial.show();
        }
        Log.d(LOG_TAG, "Back button(hardware) pressed");
        Intent returnIntent = this.getIntent();
        returnIntent.putExtra("INCREMENT_AMT", INCREMENT_AMT);
        setResult(RESULT_OK, returnIntent);
        finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (interstitial != null && globalVariable.CoffeeIncrementedInterstitialPreloaded) {
                    interstitial.show();
                }
                Intent returnIntent = this.getIntent();
                returnIntent.putExtra("INCREMENT_AMT", INCREMENT_AMT);
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();

        // Save the current coffee count
        globalVariable.saveCoffeeCount();

    }

}
