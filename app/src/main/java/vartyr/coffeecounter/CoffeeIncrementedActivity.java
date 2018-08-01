package vartyr.coffeecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServInterstitial;
import com.aerserv.sdk.AerServVirtualCurrency;
import com.amazon.device.ads.*;


import java.util.ArrayList;
import java.util.List;

public class CoffeeIncrementedActivity extends AppCompatActivity {

    private AerServInterstitial interstitial;   // AS Interstitial
    private List<DTBAdResponse> responses;      // A9 AD responses


    private int INCREMENT_AMT = 0;
    private boolean interstitialLoaded = false;
    private static String LOG_TAG;
    private GlobalClass globalVariable;


    // Set up a listener to listen to incoming events.
    protected AerServEventListener listener = new AerServEventListener(){
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args){
            CoffeeIncrementedActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "[coffeeIncrement]";
                    switch (event) {
                        case PRELOAD_READY:
                            findViewById(R.id.button_coffee_showInterstitial).setVisibility(View.VISIBLE);
                            interstitialLoaded = true;
                            Log.d(LOG_TAG, "Listener heard preload ready for interstitial");
                            break;
                        case VC_REWARDED:
                            AerServVirtualCurrency vc = (AerServVirtualCurrency) args.get(0);
                            // do something here with your virtual currency!
                            Log.d(LOG_TAG, "VC rewarded: " + vc.getAmount() + " " + vc.getName());
                            INCREMENT_AMT = vc.getAmount().intValueExact();
                            setMessageOfCounter(INCREMENT_AMT);
                            msg = "You've obtained beans (" + INCREMENT_AMT+ ")";
                            Toast.makeText(CoffeeIncrementedActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        globalVariable = (GlobalClass) getApplicationContext();
        LOG_TAG = globalVariable.LOG_TAG;


        // If the save file is empty
        if (globalVariable.readSaveFile() == "") {
            Log.d(LOG_TAG, "File not found in Coffee incremented");
        }

        // Begin routine to load Interstitial.

        if (globalVariable.getSupportA9()) {
            Log.d(LOG_TAG, "Loading A9 as support A9 is set to true");
            preloadA9Interstitial();
        } else {
            preloadInterstitial();
        }

    }

    // TODO: Remove setDebug / Verbose to optimize performance
    public void preloadInterstitial() {

        Log.d(LOG_TAG, "Preloading Interstitial on CoffeeCounter");

        final AerServConfig config = new AerServConfig(this, globalVariable.DEFAULT_INTERSTITIAL_PLC)
                .setDebug(true)
                .setA9AdResponses(null)
                .setEventListener(listener)
                .setPreload(true)
                .setVerbose(true);

        interstitial = new AerServInterstitial(config);
    }

    public void preloadA9Interstitial() {

        Log.d(LOG_TAG, "Preloading (A9) Interstitial on CoffeeCounter");


        final DTBAdRequest loader = new DTBAdRequest();
        loader.setSizes(new DTBAdSize.DTBInterstitialAdSize(globalVariable.A9_SLOT_INTERSTITIAL));
        loader.loadAd(new DTBAdCallback() {
            @Override

            // If A9 fails to fill, call preloadInterstitial
            public void onFailure(AdError adError) {
                Log.e(LOG_TAG, "A9 - Failed to get interstitial ad from Amazon");
                preloadInterstitial();
            }

            @Override
            public void onSuccess(DTBAdResponse dtbAdResponse) {
                responses = new ArrayList<DTBAdResponse>();
                responses.add(dtbAdResponse);

                Log.d(LOG_TAG, "A9 - Successfully got " + dtbAdResponse.getDTBAds().size()
                        + " interstitial ad from Amazon");


                final AerServConfig config = new AerServConfig(CoffeeIncrementedActivity.this, globalVariable.DEFAULT_INTERSTITIAL_PLC)
                        .setA9AdResponses(responses)
                        .setEventListener(listener)
                        .setPreload(true)
                        .setPubKeys(globalVariable.getPubKeys());
                interstitial = new AerServInterstitial(config);

            }
        });

    }



    // Show the interstitial only if the flag is set to true
    public void showInterstitial(View view) {
        if (interstitialLoaded) {
            interstitial.show();
            findViewById(R.id.button_coffee_showInterstitial).setVisibility(View.INVISIBLE);
            Log.d(LOG_TAG, "Interstitial shown in Coffee Incremented");
        }
        else
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

                Log.i("DATA", "Hit Actionbar Back Button");
                Log.d(LOG_TAG, "Back button(nav bar) pressed");
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
        if(interstitial != null){
            interstitial.kill();
        }

        // Save the current coffee count
        globalVariable.saveCoffeeCount();

    }

}
