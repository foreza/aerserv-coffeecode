package vartyr.coffeecounter;

// Defaults imported by Android Studio

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;    // App compatability for minimum / target API versions
import android.os.Bundle;                           //
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import com.aerserv.sdk.*;
import com.aerserv.sdk.utils.UrlBuilder;
import com.amazon.device.ads.*;


// Main Activity is the entry point into our application.
public class MainActivity extends AppCompatActivity implements GDPR_Fragment.OnFragmentInteractionListener{

    private AerServBanner banner;               // AS Banner
    private List<DTBAdResponse> responses;      // A9 AD responses


    private GlobalClass globalVariable;
    private static String LOG_TAG;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    // Set up a listener to listen to incoming AS events
    protected AerServEventListener listener = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case PRELOAD_READY:
                            banner.show();
                            Log.d(LOG_TAG, "Preload Ready for banner! A9 supported here:" + globalVariable.getSupportA9());
                        case AD_FAILED:
                            if (args.size() > 0) {
                                Log.d(LOG_TAG, "AD FAILED / not loaded. A9 supported here?" + globalVariable.getSupportA9()
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED, no other info");
                            }
                        case LOAD_TRANSACTION:
                            if (args.size() >= 1) {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has:" + args.get(0));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalVariable = (GlobalClass) getApplicationContext();     // Get an instance of the singleton class before anything else is done

        fragmentManager = getSupportFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // First check if consent has been given.
        // If it has not been given, don't bother doing anything else in the view.
        // Short terminate and start the GDPR Consent activity instead.
        if (!AerServSdk.getGdprConsentFlag(this)) {
            // modifyGDPRStatus(null, savedInstanceState);
            if (savedInstanceState == null) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.gdpr_frame, GDPR_Fragment.newInstance("test", "test2"), "gdpr")
                        .commit();
                Log.d("[CC", "Created frag");
            }
        }
        // Otherwise, if consent has been given, display the rest
        else {
            handleGDPRDisplay();
        }

        LOG_TAG = globalVariable.LOG_TAG;                           // Save LOG_TAG since we frequently access
        initializeRecyclerView();                                   // Init the recycler view
        initializeTextView();                                       // Handle Text View loading
        globalVariable.initSaveFile();                              // Will create a save file if not yet created.


        // Call the init function only once and toggle it to false after it has been called.
        // Use this to print the debug state of the application and any other useful pieces of information
        if (!globalVariable.getHasInit()) {
            initializeSDK();

            // Start this activity for testing purposes
//            Intent intent = new Intent(this, SipAndSwipe.class);
//            startActivity(intent);
        }

        // Preload banner on this activity
        if (globalVariable.getSupportA9()) {
            Log.d(LOG_TAG, "Loading A9 as support A9 is set to true");
            loadA9Banner();
        } else {
            loadBanner();
        }


        Log.d(LOG_TAG, "Reached end of onCreate for MainActivity");

    }

    // Call this function to update the GDPR state
    public void handleGDPRDisplay() {

        // Get the GDPR consent flag and save it to the singleton class
        // Show the status of the consent above
        globalVariable.setGDPRConsent(AerServSdk.getGdprConsentFlag(this));

        TextView GDPRConsentView = findViewById(R.id.gdprStatus);
        if (!globalVariable.getGDPRConsent()) {
            GDPRConsentView.setText(R.string.gdprconsentview_notConsent);
            GDPRConsentView.setTextColor(Color.parseColor("#C40824"));
        } else {
            GDPRConsentView.setText(R.string.gdprconsentview_didConsent);
            GDPRConsentView.setTextColor(Color.parseColor("#5BB55E"));
        }

    }

    // TODO: COMMENT
    private void initializeRecyclerView() {

        // Set up the recycler view, use a linear layoutmanager, feed data sets
        RecyclerView mRecyclerView = findViewById(R.id.dessert_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final RecyclerView.Adapter mAdapter = new CustomViewAdapter(globalVariable.dessertDataSet, globalVariable.colorDataSet);
        mRecyclerView.setAdapter(mAdapter);
        runOnUiThread(new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    // Call the init function only once for each SDK and toggle this to false after it has been called.
    private void initializeSDK() {

        // Initialize primary SDK (AerServ)
        AerServSdk.init(this, globalVariable.APP_ID);


        // Initialize DTB (A9) SDK
//        globalVariable.setSupportA9(true);
        AdRegistration.getInstance(globalVariable.A9_APP_KEY, this);
        AdRegistration.enableLogging(true);
        AdRegistration.enableTesting(true);
        AdRegistration.useGeoLocation(true);


        // Any sort of init log messages should be printed here
        Log.d(LOG_TAG, "Running init with site app ID: " + globalVariable.APP_ID);
        Log.d(LOG_TAG, "Currently running SDK version: " + UrlBuilder.VERSION);
        Log.d(LOG_TAG, "GDPR consent has been given to AerServ SDK: " + Boolean.toString(globalVariable.getGDPRConsent()));


        globalVariable.setInit();               // Indicate that we've initialized all SDKs
    }


    // Handle generic text updates
    public void initializeTextView() {

        TextView version = findViewById(R.id.sdkVersion);
        version.setText(getString(R.string.aerserv_sdk_version, UrlBuilder.VERSION));

        TextView coffeeAmt = findViewById(R.id.coffeeCounterView_Main);
        coffeeAmt.setText(getString(R.string.coffee_bean_count, globalVariable.getCoffeeCount()));

    }


    // Loads a banner into a defined spot
    public void loadBanner() {
        final AerServConfig config = new AerServConfig(this, globalVariable.DEFAULT_AD_PLC)
                .setEventListener(listener)
                .setRefreshInterval(10)
                .setA9AdResponses(null)
                .setPreload(true)
                .setPubKeys(globalVariable.getPubKeys());
        banner = findViewById(R.id.banner);
        banner.configure(config);
    }


    public void loadA9Banner() {

        final DTBAdRequest loader = new DTBAdRequest();
        loader.setSizes(new DTBAdSize(300, 250, globalVariable.A9_SLOT_300x250));
        loader.loadAd(new DTBAdCallback() {
            @Override

            // If A9 fails to fill, call loadBanner
            public void onFailure(AdError adError) {
                Log.e(LOG_TAG, "A9 - Failed to get ad from Amazon");
                loadBanner();
            }

            @Override
            public void onSuccess(DTBAdResponse dtbAdResponse) {
                responses = new ArrayList<DTBAdResponse>();
                responses.add(dtbAdResponse);


                Log.d(LOG_TAG, "A9 - Successfully got " + dtbAdResponse.getDTBAds().size()
                        + " banner ad from Amazon");


                final AerServConfig config = new AerServConfig(MainActivity.this, globalVariable.getDefaultPlc(0))
                        .setA9AdResponses(responses)
                        .setEventListener(listener)
                        .setPreload(true)
                        .setPubKeys(globalVariable.getPubKeys());
                banner = findViewById(R.id.banner);
                banner.configure(config);

            }
        });
    }


    public void updateCoffeeCountInView(int counter) {

        String message = Integer.toString(counter, 0) + " Beans!";

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.coffeeCounterView_Main);
        textView.setText(message);

    }


    // When we click 'increment coffee count, it should increment the value' and start a new activity to illustrate that, along with a back button.
    public void incrementCoffeeCount(View view) {

        Intent intent = new Intent(this, CoffeeIncrementedActivity.class);
        startActivityForResult(intent, 1);

    }

    public void modifyGDPRStatus(View view) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.gdpr_frame, GDPR_Fragment.newInstance("test", "test2"), "gdpr")
                    .commit();
            Log.d("[CC", "Created frag");
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        // If the request code is 0, the GDPRConsent Activity is providing a result.
        if (requestCode == 0) {
            if (resultCode == RESULT_CANCELED) {
                Log.d(LOG_TAG, "Consent not given");
            } else if (resultCode == RESULT_OK) {
                Log.d(LOG_TAG, "Consent given!");
            }
            handleGDPRDisplay();
        }


        // If the request code is 1, the CoffeeIncrementedActivity is providing a result.
        else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int amt = data.getIntExtra("INCREMENT_AMT", 0);
                Log.d(LOG_TAG, "Attempt to increment amount by" + Integer.toString(amt));
                globalVariable.incrementCOFFEE_COUNT(amt);
                Log.d(LOG_TAG, " UPDATED TOTAL =" + globalVariable.getCoffeeCount());
                updateCoffeeCountInView(globalVariable.getCoffeeCount());

            }
        }
    }


    @Override
    protected void onDestroy() {

        Log.d(LOG_TAG, "MAIN ACTIVITY CLEANUP");
        globalVariable.saveCoffeeCount();                     // Save the current coffee count
        super.onDestroy();

        // TODO: Do any sort of cleanup methods here

        if (banner != null) {
            banner.kill();
        }
    }

    // Required for GDPR fragment
    public void onFragmentInteraction(Uri uri){

        Log.d("[CC", "MAIN onFragmentInteraction");

        // leaving this empty, this was 'required'

    }

    public void onSelection(Boolean sel) {


        fragmentManager
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.gdpr_frame))
                .commit();

        Log.d("[CC", "MAIN onSelection GDPR Consent status: " + sel.toString());
        handleGDPRDisplay();
    }


}
