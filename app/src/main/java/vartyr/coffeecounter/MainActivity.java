package vartyr.coffeecounter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServSdk;
import com.aerserv.sdk.AerServTransactionInformation;
import com.aerserv.sdk.utils.UrlBuilder;


//import com.amazon.device.ads.*;


public class MainActivity extends AppCompatActivity implements GDPR_Fragment.OnFragmentInteractionListener{

    private AerServBanner banner;               // AS Banner
    public FragmentManager fragmentManager;     // For any fragments we need to call / add
    private GlobalClass globalVariable;         // To grab VC or anything we need
    private static String LOG_TAG;              // Log tag

    //    private List<DTBAdResponse> responses;      // A9 AD responses


    // Set up a listener to listen to incoming AS events
    protected AerServEventListener listener = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case PRELOAD_READY: // This should never be invoked.
                            banner.show();
                            Log.d(LOG_TAG, "Preload Ready for banner!");
                            break;
                        case AD_FAILED:
                            if (args.size() > 0) {
                                Log.d(LOG_TAG, "AD FAILED"
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED, no other info");
                            }
                            break;
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
                            Log.d(LOG_TAG, "AD LOADED");
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

        LOG_TAG = globalVariable.LOG_TAG;                           // Save LOG_TAG since we frequently access

        initializeTextView();
        globalVariable.initSaveFile();                              // Will create a save file if not yet created.


        // TODO: Re-implement GDPR consent activity

//        // Check if consent has been given.
//        if (!AerServSdk.getGdprConsentFlag(this)) {
//            if (savedInstanceState == null) {       // TODO: Is this check necessary?
//                fragmentManager
//                        .beginTransaction()
//                        .add(R.id.gdpr_fragment, GDPR_Fragment.newInstance())
//                        .commit();
//                Log.d("[CC", "Created frag");
//
//                // Clean up view (TODO: why is the button bleeding over?)
//                hideDrinkButton();
//
//            }
//        }
//        // Otherwise, if consent has been given, display the rest
//        else {
//            handleGDPRDisplay();
//            showDrinkButton();
//        }

        // Call the init function only once and toggle it to false after it has been called.
        if (!globalVariable.getHasInit()) {
            initializeSDK();
        }

        // For automated testing, change the value of the globalVariable
        if (globalVariable.sipAndSwipeMode) {
            Intent intent = new Intent(this, SipAndSwipe.class);
            startActivity(intent);
        }

        Log.d(LOG_TAG, "Reached end of onCreate for MainActivity");

        loadBanner();
    }


    // Call this function to update the GDPR state
    public void handleGDPRDisplay() {

        // Get the GDPR consent flag and save it to the singleton class
        // Show the status of the consent above
//        globalVariable.setGDPRConsent(AerServSdk.getGdprConsentFlag(this));

//        TextView GDPRConsentView = findViewById(R.id.gdprStatus);
//        if (!globalVariable.getGDPRConsent()) {
//            GDPRConsentView.setText(R.string.gdprconsentview_notConsent);
//            GDPRConsentView.setTextColor(Color.parseColor("#C40824"));
//        } else {
//            GDPRConsentView.setText(R.string.gdprconsentview_didConsent);
//            GDPRConsentView.setTextColor(Color.parseColor("#5BB55E"));
//        }

    }




    // Call the init function only once for each SDK and toggle this to false after it has been called.
    private void initializeSDK() {

        // Initialize primary SDK (AerServ)
         AerServSdk.init(this, globalVariable.APP_ID);


//        JSONObject consentObject = new JSONObject();
//        try {
//            // Provide correct consent value to sdk which is obtained by User
//            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
//            // Provide 0 if GDPR is not applicable and 1 if applicable
//            consentObject.put("gdpr", "1");
//        } catch (JSONException e) {
//            e.printStackTrace();
       //  }

//        InMobiSdk.init(this,"b2562f781e3948ba86b439d581b07b1d");



        // Initialize DTB (A9) SDK
//        AdRegistration.getInstance(globalVariable.A9_APP_KEY, this);
//        AdRegistration.enableLogging(true);
//        AdRegistration.enableTesting(true);
//        AdRegistration.useGeoLocation(true);


        // Any sort of init log messages should be printed here
        Log.d(LOG_TAG, "Running init with site app ID: " + globalVariable.APP_ID);
        Log.d(LOG_TAG, "Currently running SDK version: " + UrlBuilder.VERSION);
        Log.d(LOG_TAG, "GDPR consent has been given to AerServ SDK: " + Boolean.toString(globalVariable.getGDPRConsent()));


        globalVariable.setInit();               // Indicate that we've initialized all SDKs

    }


    // Handle generic text updates on this view
    public void initializeTextView() {
        TextView coffeeAmt = findViewById(R.id.coffeeCounterView_Main);
        coffeeAmt.setText(getString(R.string.coffee_bean_count, globalVariable.getCoffeeCount()));

    }

    public void hideDrinkButton(){
        Button drink = findViewById(R.id.button2);
        drink.setVisibility(View.INVISIBLE);
    }

    public void showDrinkButton(){
        Button drink = findViewById(R.id.button2);
        drink.setVisibility(View.VISIBLE);
    }


    // Loads a banner into a defined spot
    public void loadBanner() {

        Log.d(LOG_TAG, "CONFIGURING A BANNER NOW");

        if (banner != null){
            Log.d(LOG_TAG, "KILLING PREVIOUS BANNER");
            banner.kill();
            banner = null;
        }

        final AerServConfig config = new AerServConfig(this, globalVariable.DEFAULT_AD_PLC)
                .setEventListener(listener)
                .setDebug(true)
                .setPubKeys(globalVariable.getPubKeys());
        banner = findViewById(R.id.banner);
        banner.configure(config);
        banner.show();
    }




    // TODO: TEST AND VALIDATE THIS FUNCTION

     // Load an A9 banner, which will call loadBanner after the DTBAdloader returns a response
    public void loadA9Banner() {

//        final DTBAdRequest loader = new DTBAdRequest();
//        loader.setSizes(new DTBAdSize(300, 250, globalVariable.A9_SLOT_300x250));
//        loader.loadAd(new DTBAdCallback() {
//            @Override
//
//            // If A9 fails to fill, call loadBanner
//            public void onFailure(AdError adError) {
//                Log.e(LOG_TAG, "A9 - Failed to get ad from Amazon");
//                loadBanner();
//            }
//
//            @Override
//            public void onSuccess(DTBAdResponse dtbAdResponse) {
//                responses = new ArrayList<DTBAdResponse>();
//                responses.add(dtbAdResponse);
//
//
//                Log.d(LOG_TAG, "A9 - Successfully got " + dtbAdResponse.getDTBAds().size()
//                        + " banner ad from Amazon");
//
//
//                final AerServConfig config = new AerServConfig(MainActivity.this, globalVariable.getDefaultPlc(0))
//                        .setA9AdResponses(responses)
//                         .setEventListener(listener)
//                        .setPreload(true)
//                        .setPubKeys(globalVariable.getPubKeys());
//                banner = findViewById(R.id.banner);
//                banner.configure(config);
//
//            }
//        });
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


    // When we click 'get dessert menu', we should be taken to the activity that lets us test performance with recycler view
    public void getDessertMenu(View view) {
        Intent intent = new Intent(this, DessertMenuRecycler.class);
        startActivityForResult(intent, 3);  // Request code 3 will be returned (may not be used) from finishing the dessert menu.
    }

    // When we click 'getDetailedStats', load the view for detailed stats - for testing loading with application context testing for banners
    public void getDetailedStats(View view) {
        if (globalVariable.checkBannerAdPreloadReady()){
            Toast.makeText(this, (String)"Background thread has loaded a banner ad.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, (String)"Background thread has not loaded a banner ad.",
                    Toast.LENGTH_LONG).show();
            globalVariable.beginPreloadBannerInBG(globalVariable.DEFAULT_300X250TEST_PLC);
        }
        Intent intent = new Intent(this, BackGroundBanner.class);
        startActivityForResult(intent, 2);
    }

    // When we click 'view settings', show the various sdk versions and (future) allow me to modify other various settings
    public void viewSettings(View view) {
        Intent intent = new Intent(this, ApplicationSettings.class);
        startActivityForResult(intent, 4);
    }


    // This should load a test activity which will have 2 banners with the same refresh timer to test potential adapter conflicts
    public void viewAndListenToRadio(View view) {

        Intent intent = new Intent(this, CoffeeRadio.class);
        startActivityForResult(intent, 5);

    }

    // When we click 'getShocked', load a shocking view - for testing loading with application context testing for interstitials
    public void getShocked(View view) {
        if (globalVariable.checkInterstitialAdPreloadReady()){
            Toast.makeText(this, (String)"Background thread has loaded a interstitial ad.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, (String)"Background thread has not loaded a interstitial ad.",
                    Toast.LENGTH_LONG).show();
            globalVariable.beginPreloadInterstitialInBG(globalVariable.DEFAULT_INTERSTITIAL_PLC);
        }
        Intent intent = new Intent(this, BackgroundInterstitial.class);
        startActivityForResult(intent, 6);
    }



    // Update the GDPR status view
    public void modifyGDPRStatus(View view) {

            fragmentManager
                    .beginTransaction()
                    .add(R.id.gdpr_fragment, GDPR_Fragment.newInstance())
                    .commit();
            Log.d("[CC", "Created frag");

            hideDrinkButton();

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // If the request code is 0, the GDPRConsent Activity is providing a result.
        if (requestCode == 0) {

            // TODO: Use request code for something else, now that we are using fragments instead of activity passback
//
//            if (resultCode == RESULT_CANCELED) {
//                Log.d(LOG_TAG, "Consent not given");
//            } else if (resultCode == RESULT_OK) {
//                Log.d(LOG_TAG, "Consent given!");
//            }
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


        // NOTE: Enable this if you are testing banner injection from app context
        else if (requestCode == 2) {
            Log.d(LOG_TAG, "Returned from background banner, begin loading new ad");
            if (!globalVariable.checkBannerAdPreloadReady()){
                // Handle case that banner has already played; load new one
                // globalVariable.beginPreloadBannerInBG();
            } else {
                Log.d(LOG_TAG, "Banner has not yet played");
            }
        }

        // NOTE: Enable this if you are testing interstitial preloading from bg
        else if (requestCode == 6) {
            Log.d(LOG_TAG, "Returned from background interstitial, begin loading new ad");
            if (!globalVariable.checkInterstitialAdPreloadReady()){
                // Handle case that int has already played; load new one?
            } else {
                Log.d(LOG_TAG, "Interstitial has not yet played");
            }
        }

    }


    @Override
    protected void onDestroy() {

        Log.d(LOG_TAG, "MAIN ACTIVITY CLEANUP");
        globalVariable.saveCoffeeCount();                     // Save the current coffee count
        super.onDestroy();

        if (banner != null) {
            banner.kill();
        }
    }




    // Required interface method for GDPR fragment
    public void onSelection(Boolean sel) {

        // Start the GDPR fragment
        fragmentManager
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.gdpr_fragment))
                .commit();

        Log.d(LOG_TAG, "MAIN onSelection GDPR Consent status: " + sel.toString());
        handleGDPRDisplay();
        showDrinkButton();
    }

}
