package vartyr.coffeecounter;

// Defaults imported by Android Studio
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;    // App compatability for minimum / target API versions
import android.os.Bundle;                           //
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aerserv.sdk.*;
import com.aerserv.sdk.utils.UrlBuilder;


import java.util.Arrays;
import java.util.List;


// Main Activity is the entry point into our application.
public class MainActivity extends AppCompatActivity  {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String LOG_TAG = "CoffeeCounter_SampleApp";
    public static final String DEFAULT_PLC = "380000";
    public static final String APP_ID = "380000";
    private List<String> keywords = Arrays.asList("keyword1", "keyword2");


    public static int COFFEE_COUNT = 0;

    private AerServBanner banner;


    // Set up a listener to listen to incoming events
    protected AerServEventListener listener = new AerServEventListener(){
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = null;
                    switch (event) {
                        case AD_FAILED:
                            if (args.size() > 1) {
                                Integer adFailedCode =
                                        (Integer) args.get(AerServEventListener.AD_FAILED_CODE);
                                String adFailedReason =
                                        (String) args.get(AerServEventListener.AD_FAILED_REASON);
                                msg = "Ad failed with code=" + adFailedCode + ", reason=" + adFailedReason;
                            } else {
                                msg = "Ad Failed with message: " + args.get(0).toString();
                            }
                            break;

                        // JC's custom events to see what fires
                        case AD_CLICKED:
                            msg = "[JC] AD WAS CLICKED!";  // Intended behavior - fire off something indicating we know the ad was clicked
                            break;
                        case AD_LOADED:
                            msg = "[JC] AD WAS loaded!";  // Intended behavior - fire off something indicating we know the ad was clicked
                            break;


                        default:
                            msg = event.toString() + " event fired with args: " + args.toString();
                    }
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, msg);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log the SDK version
        TextView version = (TextView) findViewById(R.id.sdkVersion);
        version.setText("v" + UrlBuilder.VERSION);

        Log.d(LOG_TAG, "Running SDK version: " + UrlBuilder.VERSION);


        // Call the init function
        AerServSdk.init(this, APP_ID);
        Log.d(LOG_TAG, "Running init with site app ID: " + APP_ID);

        // Update the view each time it is being created.
        updateCoffeeCountInView();

        // Load this banner on the page.
        // loadBanner();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(banner != null){
            banner.kill();
        }
    }

    public void loadBanner() {
        final AerServConfig config = new AerServConfig(this, DEFAULT_PLC)
                .setEventListener(listener)
                .setRefreshInterval(10)
                .setPrecache(true)
                .setKeywords(keywords)
                .setDebug(true)
                .setVerbose(true);
        banner = (AerServBanner) findViewById(R.id.banner);
        banner.configure(config).show();
    }



    public void updateCoffeeCountInView() {

        String message = Integer.toString(COFFEE_COUNT, 0);

        Log.v("Updating counter: ", message);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.coffeeCounterView_Main);
        textView.setText(message);

    }


    /** Called when the user taps the Send button */

    public void sendMessage(View view) {
        // Do something in response to button

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);

        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    // When we click 'increment coffee count, it should increment the value' and start a new activity to illustrate that, along with a back button.
    public void incrementCoffeeCount(View view) {

        Intent intent = new Intent(this, CoffeeIncrementedActivity.class);

        // Increment the coffee counter in this activity ;
        COFFEE_COUNT++;

        intent.putExtra("COFFEE_COUNT", COFFEE_COUNT);
        intent.putExtra("PLC_ID", DEFAULT_PLC );

        // Start the activity.
        startActivity(intent);


    }


//    public void showInterstitialBanner() {
//
//        final AerServConfig config = new AerServConfig(this, DEFAULT_PLC)
//        .setEventListener(listener)
//                .setPrecache(true)
//                .setKeywords(keywords)
//                .setDebug(true)
//                .setVerbose(true);
//        interstitial = new AerServInterstitial(config);
//        interstitial.show();
//
//
//
//    }
}
