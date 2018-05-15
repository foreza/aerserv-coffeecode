package vartyr.coffeecounter;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import com.aerserv.sdk.AerServSdk;

import java.util.Arrays;
import java.util.List;

public class GlobalClass extends Application{

    private static final String LOG_TAG = "CoffeeCounter_SampleApp";
    public static String DEFAULT_PLC = "380004";
    public static final String APP_ID = "380000";
    private List<String> keywords = Arrays.asList("keyword1", "keyword2");
    public int COFFEE_COUNT = 0;
    public boolean hasInit = false;



    // Temp getter for PLC
    public String getDefaultPlc(){
        return DEFAULT_PLC;
    }

    // Temp setter for PLC
    public void setDefaultPlc(String aPLC){
        DEFAULT_PLC = aPLC;
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