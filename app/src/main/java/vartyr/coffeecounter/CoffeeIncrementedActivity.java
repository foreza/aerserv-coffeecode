package vartyr.coffeecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


    private String LOG_TAG;
    public String DEFAULT_PLC;
    public String APP_ID;
    private List<String> keywords;
    public int COFFEE_COUNT;


    // Set up a listener to listen to incoming events
    protected AerServEventListener listener = new AerServEventListener(){
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args){
            CoffeeIncrementedActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "HMM~ ";
                    switch (event) {
                        case VC_REWARDED:
                            AerServVirtualCurrency vc = (AerServVirtualCurrency) args.get(0);
                            // do something here with your virtual currency!
                            Log.d(LOG_TAG + "AVC", "VC rewarded: " + vc.getAmount() + " " + vc.getName());
                            INCREMENT_AMT = vc.getAmount().intValueExact();
                            msg = "HEYO! " + INCREMENT_AMT;
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
        DEFAULT_PLC = globalVariable.getDefaultPlc();
        APP_ID = globalVariable.getAppId();
        keywords = globalVariable.getKeywords();
        COFFEE_COUNT = globalVariable.getCOFFEE_COUNT();


        setContentView(R.layout.activity_coffee_incremented);
        loadInterstitial();
    }

    public void loadInterstitial() {

        Log.d(LOG_TAG, "~~~ Loading Interstitial ");

        final AerServConfig config = new AerServConfig(this, DEFAULT_PLC)
                .setDebug(true)
                .setEventListener(listener)
                .setVerbose(true);
        interstitial = new AerServInterstitial(config);
        interstitial.show();
    }

    public void setMessageOfCounter(int amount) {

        // Get the Intent that started this activity and extract the original amount from the string
        Intent intent = getIntent();
        int original_amt = intent.getIntExtra("COFFEE_COUNT", 0);
        int total = original_amt + amount;


        // Get a ref to the layout's TextView
        TextView textView = findViewById(R.id.coffeeCountView_Incremented);

        textView.setText("You now have: " + original_amt + " + " + amount + " = " + total + " cups of coffee in your system!");



    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("INCREMENT_AMT", INCREMENT_AMT);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onDestroy(){
        Log.d(LOG_TAG, "~~~ Destroying Interstitial View ~~~ " + INCREMENT_AMT);
        super.onDestroy();
        if(interstitial != null){
            interstitial.kill();
        }
    }

}
