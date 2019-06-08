package vartyr.coffeecounter.managers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServSdk;
import com.aerserv.sdk.AerServTransactionInformation;
import com.inmobi.sdk.InMobiSdk;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdManager {

    private static final AdManager instance = new AdManager();

    public static AdManager getInstance() {
        return instance;
    }

    private AdManager() {
    }


//    public final String LOG_TAG = "CoffeeCounter";
//    public final String APP_ID = "1011139";
//    public final String DEFAULT_AD_PLC = "1042117";
//    public String DEFAULT_INTERSTITIAL_PLC = "1042115";
//    public String DEFAULT_300X250TEST_PLC = "1042114";

    public final String LOG_TAG = "CoffeeCounter";
    public final String APP_ID = "380000";
    public final String DEFAULT_AD_PLC = "380000";         // CoffeeBanner
    public String DEFAULT_INTERSTITIAL_PLC = "1048445";     // CAFFEINE TRIGGER
    public String DEFAULT_300X250TEST_PLC = "1063612";      // CoffeeMREC

//        public final String APP_ID = "1019630";
//    public final String DEFAULT_AD_PLC = "1062630";         // CoffeeBanner
//    public String DEFAULT_INTERSTITIAL_PLC = "1062630";     // CAFFEINE TRIGGER
//    public String DEFAULT_300X250TEST_PLC = "1062630";      // CoffeeMREC


    public boolean CoffeeIncrementedInterstitialPreloaded = false;

    private boolean hasInit = false;
    private boolean hasGDPRConsent = false;
    private static Map<String, String> pubKeys = new HashMap<String, String>();


    private AerServBanner banner;                              // AS Banner, which we will load in the background
    private Boolean bannerPreloadReady = false;


    // Public Test params / datasets
    public String [] dessertDataSet = new String [] {"Raspberry","Mint","Cherry Vanilla","Butter Pecan","Peanut Butter Cup","Chocolate Chip","Chocolate Chip Cookie Dough","Chocolate Almond","Chocolate","Mint Chocolate Chip","Caramel","Moose Tracks","Fudge Brownie","Pistachio","M&M's","Vanilla","Cherry","Lemon","Cookie Dough","Coffee","Banana","Praline Pecan","Chocolate Marshmallow","Neopolitan","Cookies N' Cream","Rocky Road","Strawberry","Birthday Cake","French Vanilla", "Raspberry","Mint","Cherry Vanilla","Butter Pecan","Peanut Butter Cup","Chocolate Chip","Chocolate Chip Cookie Dough","Chocolate Almond","Chocolate","Mint Chocolate Chip","Caramel","Moose Tracks","Fudge Brownie","Pistachio","M&M's","Vanilla","Cherry","Lemon","Cookie Dough","Coffee","Banana","Praline Pecan","Chocolate Marshmallow","Neopolitan","Cookies N' Cream","Rocky Road","Strawberry","Birthday Cake","French Vanilla", "Raspberry","Mint","Cherry Vanilla","Butter Pecan","Peanut Butter Cup","Chocolate Chip","Chocolate Chip Cookie Dough","Chocolate Almond","Chocolate","Mint Chocolate Chip","Caramel","Moose Tracks","Fudge Brownie","Pistachio","M&M's","Vanilla","Cherry","Lemon","Cookie Dough","Coffee","Banana","Praline Pecan","Chocolate Marshmallow","Neopolitan","Cookies N' Cream","Rocky Road","Strawberry","Birthday Cake","French Vanilla"};
    public int [] colorDataSet = new int [] {Color.GRAY, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.WHITE, Color.BLACK};


    protected AerServEventListener bannerListener = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {

            // Make a new runnable since we're using the application context
            Runnable bannerRunnable = new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case PRELOAD_READY:
                            Log.d(LOG_TAG, "Preload Ready for banner!");
                            bannerPreloadReady = true;
                            break;
                        case AD_FAILED:
                            if (args.size() > 0) {
                                Log.d(LOG_TAG, "AD FAILED / not loaded. Error code: " + AerServEventListener.AD_FAILED_CODE + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED, no other info");
                            }
                            break;
                        case LOAD_TRANSACTION:
                            if (args.size() >= 1) {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has:" + args.get(0));

                                AerServTransactionInformation vc = (AerServTransactionInformation) args.get(0);
                                String buyerName = vc.getBuyerName();
                                BigDecimal buyerPrice = vc.getBuyerPrice();
                            }
                            else {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has no information");
                            }
                            break;
                        case AD_IMPRESSION:
                            Log.d(LOG_TAG, "bannerListener - AD IMPRESSION");
                            break;
                        case AD_LOADED:
                            Log.d(LOG_TAG, "bannerListener - AD loaded");
                            break;
                    }
                }
            };

            // Run the .. runnable.
            bannerRunnable.run();
        }
    };


    public void initializeAdSDKWithContext(Context ctx){

        AerServSdk.init(ctx, APP_ID);
        hasInit = true;

        Log.d(LOG_TAG, "Running init with site app ID: " + AerServSdk.getSiteId());
        Log.d(LOG_TAG, "Currently running SDK version: " + InMobiSdk.getVersion());

    }


    // Preload the banner ad using the backgroundPLC
    public void beginPreloadBannerInBG(Context ctx, String plc){

        final AerServConfig config = new AerServConfig(ctx, plc)
                .setEventListener(bannerListener)        // Use the bannerListener declared above
                .setRefreshInterval(0)              // Do not allow refresh
                .setPreload(true);                  // Support preloading

        banner = new AerServBanner(ctx);
        banner.configure(config);
    }



    // Provide the banner for injection into the view
    public View getBackgroundBannerToInject() {
        return banner;
    }


    // Attempt to show the ad.
    public void attemptShowPreloadedBannerAdFromBG(){

        if (bannerPreloadReady){
            banner.show();
            bannerPreloadReady = false;
            Log.d(LOG_TAG, "PreloadReady is true, showing banner");
        }
        else {
            Log.d(LOG_TAG, "PreloadReady is false, NOT showing banner");
        }

    }

    public boolean checkBannerAdPreloadReady(){
        return bannerPreloadReady;
    }



    // GET METHODS to access private variables


    public boolean getHasInit(){
        return hasInit;
    }
    public boolean getGDPRConsent(){
        return hasGDPRConsent;
    }
    public Map<String, String> getPubKeys(){
        return pubKeys;
    }






}