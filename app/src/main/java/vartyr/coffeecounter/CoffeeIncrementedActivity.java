package vartyr.coffeecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
    public int INCREMENT_AMT = 0;

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
                        case VC_REWARDED:
                            AerServVirtualCurrency vc = (AerServVirtualCurrency) args.get(0);
                            // do something here with your virtual currency!
                            Log.d(LOG_TAG, "VC rewarded: " + vc.getAmount() + " " + vc.getName());
                            INCREMENT_AMT = vc.getAmount().intValueExact();
                            msg = "You've obtained beans (" + INCREMENT_AMT+ ")";
                            setMessageOfCounter(INCREMENT_AMT);
                            Toast.makeText(CoffeeIncrementedActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        LOG_TAG = globalVariable.getLogTag();
        DEFAULT_PLC = globalVariable.getDefaultPlc(1);
        APP_ID = globalVariable.getAppId();
        keywords = globalVariable.getKeywords();
        COFFEE_COUNT = globalVariable.getCOFFEE_COUNT();


        setContentView(R.layout.activity_coffee_incremented);

        loadInterstitial();


    }

    public void loadInterstitial() {

        Log.d(LOG_TAG, "Loading Interstitial on Coffee Increment ");

        final AerServConfig config = new AerServConfig(this, DEFAULT_PLC)
                .setDebug(true)
                .setEventListener(listener)
                .setVerbose(true);
        interstitial = new AerServInterstitial(config);
        interstitial.show();
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
