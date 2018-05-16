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

import java.util.List;

public class CoffeeIncrementedActivity extends AppCompatActivity {


    // This view will provide an interstitial
    private AerServInterstitial interstitial;
    private int INCREMENT_AMT = 0;
    private boolean interstitialLoaded = false;

    // Global variables TODO: Remove the ones we do not need
    private String LOG_TAG;
    public String DEFAULT_PLC;
    public String APP_ID;
    private List<String> keywords;
    public int COFFEE_COUNT;


    // Set up a listener to listen to incoming events.
    // TODO: Handle any fail cases gracefully
    protected AerServEventListener listener = new AerServEventListener(){
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args){
            CoffeeIncrementedActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "[coffeeIncrement]";
                    switch (event) {
                        case PRELOAD_READY:
                            interstitialLoaded = true;
                            findViewById(R.id.button_coffee_showInterstitial).setVisibility(View.VISIBLE);
                            Log.d(LOG_TAG, "Listener heard preload ready for interstitial");
                            break;
                        case VC_REWARDED:
                            AerServVirtualCurrency vc = (AerServVirtualCurrency) args.get(0);
                            // do something here with your virtual currency!
                            Log.d(LOG_TAG, "VC rewarded: " + vc.getAmount() + " " + vc.getName());
                            INCREMENT_AMT = vc.getAmount().intValueExact();
                            msg = "You've obtained beans (" + INCREMENT_AMT+ ")";
                            setMessageOfCounter(INCREMENT_AMT);
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

        // Access singleton and populate with values for this activity scope
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        LOG_TAG = globalVariable.getLogTag();
        DEFAULT_PLC = globalVariable.getDefaultPlc(1);
        APP_ID = globalVariable.getAppId();
        keywords = globalVariable.getKeywords();
        COFFEE_COUNT = globalVariable.getCOFFEE_COUNT();

        // Begin routine to load Interstitial.
        preloadInterstitial();
    }

    // TODO: Remove setDebug / Verbose to optimize performance
    public void preloadInterstitial() {

        Log.d(LOG_TAG, "Preloading Interstitial on CoffeeCounter");

        final AerServConfig config = new AerServConfig(this, DEFAULT_PLC)
                .setDebug(true)
                .setEventListener(listener)
                .setPreload(true)
                .setVerbose(true);

        interstitial = new AerServInterstitial(config);
    }



    // Show the interstitial only if the flag is set to true
    public void showInterstitial(View view) {
        if (interstitialLoaded) {
            findViewById(R.id.button_coffee_showInterstitial).setVisibility(View.INVISIBLE);
            interstitial.show();
            Log.d(LOG_TAG, "Interstitial shown in Coffee Incremented");
        }
        else
            Log.d(LOG_TAG, "Interstitial not ready / not shown in Coffee Incremented");
    }


    // This sets the amount of coffee beans just obtained
    public void setMessageOfCounter(int amount) {
        TextView textView = findViewById(R.id.coffeeCountView_Incremented);
        textView.setText("You have earned this many beans: " + INCREMENT_AMT);
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
    }

}
