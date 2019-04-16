package vartyr.coffeecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServInterstitial;
import com.aerserv.sdk.AerServTransactionInformation;

import java.util.List;

public class BackgroundInterstitial extends AppCompatActivity {


    private GlobalClass globalVariable;                                                 // We'll be accessing the background banner here
    private static String LOG_TAG;                                                      // Log tag
    private AerServInterstitial interstitial;

    public AerServEventListener testInterstitialListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_interstitial);

        globalVariable = (GlobalClass) getApplicationContext();                         // Get an instance of the singleton class before anything else is done
        LOG_TAG = globalVariable.LOG_TAG;

        if (globalVariable.checkInterstitialAdPreloadReady()){
            Log.d(LOG_TAG, "checkInterstitialAdPreloadReady - found ad was ready");
            setNewListenerTest();
            globalVariable.attemptShowPreloadedInterstitialAdFromBG();
        } else {
            Log.d(LOG_TAG, "checkInterstitialAdPreloadReady - found ad was not ready. do nothing");
        }


    }


    // On back, we want to send the incremented amount back to the MainActivity
    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "Back button(hardware) pressed");
        Intent returnIntent = this.getIntent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = this.getIntent();
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This function will set a new listener
    public void setNewListenerTest(){

        Log.d(LOG_TAG, "setNewListenerTest invoked");

        // Make a new test listener
        testInterstitialListener = new AerServEventListener() {
            @Override
            public void onAerServEvent(final AerServEvent event, final List<Object> args) {
                BackgroundInterstitial.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (event) {
                            case AD_IMPRESSION:
                                Log.d(LOG_TAG, "testInterstitialListener - AD IMPRESSION! It worked!");
                                break;
                            case AD_LOADED:
                                Log.d(LOG_TAG, "testInterstitialListener - AD loaded! It worked!");
                                break;
                        }
                    }
                });
            }
        };


        globalVariable.setInterstitialListener(testInterstitialListener);
    }

}
