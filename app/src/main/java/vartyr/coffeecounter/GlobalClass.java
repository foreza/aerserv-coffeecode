package vartyr.coffeecounter;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import com.aerserv.sdk.AerServSdk;

import java.util.Arrays;
import java.util.List;

public class GlobalClass extends Application{

    // Define your global variables here
    private static final String LOG_TAG = "CoffeeCounter";
    private static String DEFAULT_AD_PLC = "380000";
    private static String DEFAULT_INTERSTITIAL_PLC = "380004";
    private static final String APP_ID = "380000";
    private List<String> keywords = Arrays.asList("coffee", "beans");
    private int COFFEE_COUNT = 0;
    private boolean hasInit = false;


    // Temp getter for PLC
    public String getDefaultPlc(int i){
        if (i == 0){
            return DEFAULT_AD_PLC;
        } else if (i == 1){
            return DEFAULT_INTERSTITIAL_PLC;
        }
        return DEFAULT_AD_PLC;
    }

    // Getter for App ID (must remain unchanged)
    public String getAppId(){
        return APP_ID;
    }

    // Getter for get keywords
    public List<String> getKeywords(){
        return keywords;
    }

    // Getter for COFFEE_COUNT (todo - should be hitting some backend DB in v2)
    public int getCOFFEE_COUNT(){
        return COFFEE_COUNT;
    }

    // Setter for COFFEE_COUNT
    public void setCOFFEE_COUNT(int amt){
        COFFEE_COUNT += amt;
    }

    // Getter for log tag (must remain unchanged)
    public String getLogTag(){
        return LOG_TAG;
    }

    // Getter for hasInit()
    public boolean getHasInit(){
        return hasInit;
    }

    public void setInit(){
        hasInit = true;
    }





}