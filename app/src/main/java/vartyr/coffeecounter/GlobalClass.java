package vartyr.coffeecounter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.aerserv.sdk.AerServSdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class GlobalClass extends Application{

    // Define your global variables here for the singleton class
    private static final String LOG_TAG = "CoffeeCounter";
    private static String DEFAULT_AD_PLC = "380000";
    private static String DEFAULT_INTERSTITIAL_PLC = "380004";
    private static final String APP_ID = "380000";
    private List<String> keywords = Arrays.asList("coffee", "beans");
    private int COFFEE_COUNT = 0;
    private boolean hasInit = false;
    private boolean hasGDPRConsent = false;

    // File I/O
    FileOutputStream outputStream;


    //
    public boolean getGDPRConsent(){
        return hasGDPRConsent;
    }

    public void setGDPRConsent(boolean v) {
        hasGDPRConsent = v;
    }


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

    public void setCoffeeCount(int amt) {COFFEE_COUNT = amt;}

    public void saveCoffeeCount() {
        writeSaveFile(String.valueOf(COFFEE_COUNT));
    }

    // Setter for COFFEE_COUNT
    public void incrementCOFFEE_COUNT(int amt){
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

    public void writeSaveFile (String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("db.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void createSaveFile(){
        File directory = this.getFilesDir();
        File file = new File(directory, "db.txt");
    }

    public void initSaveFile(){
        // If the save file is empty
        if (readSaveFile() == "") {
            Log.d(LOG_TAG, "File not found, making new one");
            createSaveFile();
            writeSaveFile("0");     // For now, we'll just store the counter there and parse out the string / convert it
        } else {
            String output = readSaveFile();
            Log.d(LOG_TAG, "Contained in file: " + output);
            COFFEE_COUNT = Integer.parseInt(output);
        }
    }

    public String readSaveFile() {

        String ret = "";

        try {
            InputStream inputStream = this.openFileInput("db.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Read amt activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Read ant activity", "Can not read file: " + e.toString());
        }

        return ret;
    }






}