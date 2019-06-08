package vartyr.coffeecounter;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import vartyr.coffeecounter.advancedtests.SampleRadioActivity;
import vartyr.coffeecounter.basictests.InjectedBannerActivity;
import vartyr.coffeecounter.basictests.PreloadInterstitialActivity;
import vartyr.coffeecounter.managers.AdManager;
import vartyr.coffeecounter.managers.StateManager;


public class MainActivity extends AppCompatActivity{

    public StateManager stateManager;
    private AdManager adManager;         // To grab VC or anything we need


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adManager = AdManager.getInstance();
        stateManager = StateManager.getInstance();

        if (!adManager.getHasInit()) {
            adManager.initializeAdSDKWithContext(getApplicationContext());
        }

//        stateManager.setStateManagerContext(getApplicationContext());
        stateManager.initSaveFile(this);                              // Will create a save file if not yet created.

        // Load the UI
        initializeUI();
    }



    // Handle generic text updates on this view
    public void initializeUI() {
        TextView coffeeAmt = findViewById(R.id.coffeeCounterView_Main);
        coffeeAmt.setText(getString(R.string.coffee_bean_count, stateManager.getCoffeeCount()));
    }

    public void view_hideDrinkButton(){
        Button drink = findViewById(R.id.button_interstitialTest);
        drink.setVisibility(View.INVISIBLE);
    }

    public void view_showDrinkButton(){
        Button drink = findViewById(R.id.button_interstitialTest);
        drink.setVisibility(View.VISIBLE);
    }


    public void view_updateCoffeeCountInView(int counter) {

        String message = Integer.toString(counter, 0) + " Beans!";

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.coffeeCounterView_Main);
        textView.setText(message);

    }



    public void nav_viewSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void nav_preloadInterstitialTest(View view) {
        Intent intent = new Intent(this, PreloadInterstitialActivity.class);
        startActivityForResult(intent, 1);
    }


    // When we click 'nav_injectBannerTest', load the view for detailed stats - for testing loading with application context testing for banners
    public void nav_injectBannerTest(View view) {
        if (adManager.checkBannerAdPreloadReady()){
            Toast.makeText(this, (String)"Background thread has loaded a banner ad.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, (String)"Background thread has not loaded a banner ad. Will begin preloading now.",
                    Toast.LENGTH_LONG).show();
            adManager.beginPreloadBannerInBG(adManager.DEFAULT_300X250TEST_PLC);
        }
        Intent intent = new Intent(this, InjectedBannerActivity.class);
        startActivity(intent);
    }



    // This should load a test  activity which will have 2 banners with the same refresh timer to test potential adapter conflicts
    public void nav_radioViewTest(View view) {
        Intent intent = new Intent(this, SampleRadioActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // If the request code is 1, the PreloadInterstitialActivity is providing a result.
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int amt = data.getIntExtra("INCREMENT_AMT", 0);
                Log.d(stateManager.LOG_TAG, "Attempt to increment amount by" + Integer.toString(amt));
                stateManager.incrementCOFFEE_COUNT(amt);
                Log.d(stateManager.LOG_TAG, " UPDATED TOTAL =" + stateManager.getCoffeeCount());
                view_updateCoffeeCountInView(stateManager.getCoffeeCount());

            }
        }

        else if (requestCode == 2) {
            Log.d(stateManager.LOG_TAG, "Returned from background banner, begin loading new ad");
            if (!adManager.checkBannerAdPreloadReady()){
                // Handle case that banner has already played; load new one
                // adManager.beginPreloadBannerInBG();
            } else {
                Log.d(stateManager.LOG_TAG, "Banner has not yet played");
            }
        }

    }


    @Override
    protected void onDestroy() {

        Log.d(stateManager.LOG_TAG, "MAIN ACTIVITY CLEANUP");
        stateManager.saveCoffeeCount(this);
        super.onDestroy();
    }


}
