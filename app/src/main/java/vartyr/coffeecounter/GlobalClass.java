package vartyr.coffeecounter;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class GlobalClass extends Application {

    public final String LOG_TAG = "CoffeeCounter";
    public final String APP_ID = "1010277"; // Default TestPub: 380000 || CoffeeCounter: 1010277
    public final String DEFAULT_AD_PLC = "1047721"; // A9: 380883 || Test Rhythm One Perk Vast: 380883
    public String DEFAULT_INTERSTITIAL_PLC = "380889"; // Test Rhythm One Perk Vast: 380883 | 1046757 - coffeecounter interstitial
    public final String A9_APP_KEY = "a9_onboarding_app_id";
    public final String A9_SLOT_320x50 = "5ab6a4ae-4aa5-43f4-9da4-e30755f2b295";             // Price point(amznslots): o320x50p1
    public final String A9_SLOT_300x250 = "54fb2d08-c222-40b1-8bbe-4879322dc04b";            // Price point(amznslots): o300x250p1
    public final String A9_SLOT_INTERSTITIAL = "4e918ac0-5c68-4fe1-8d26-4e76e8f74831";       // ointerstitialp1

    private int CoffeeCount = 0;
    private boolean hasInit = false;
    private boolean hasGDPRConsent = false;
    private boolean supportA9 = false;
    private static Map<String, String> pubKeys = new HashMap<String, String>();


    // Public Test params / datasets


    public String [] dessertDataSet = new String [] {"Raspberry","Mint","Cherry Vanilla","Butter Pecan","Peanut Butter Cup","Chocolate Chip","Chocolate Chip Cookie Dough","Chocolate Almond","Chocolate","Mint Chocolate Chip","Caramel","Moose Tracks","Fudge Brownie","Pistachio","M&M's","Vanilla","Cherry","Lemon","Cookie Dough","Coffee","Banana","Praline Pecan","Chocolate Marshmallow","Neopolitan","Cookies N' Cream","Rocky Road","Strawberry","Birthday Cake","French Vanilla", "Raspberry","Mint","Cherry Vanilla","Butter Pecan","Peanut Butter Cup","Chocolate Chip","Chocolate Chip Cookie Dough","Chocolate Almond","Chocolate","Mint Chocolate Chip","Caramel","Moose Tracks","Fudge Brownie","Pistachio","M&M's","Vanilla","Cherry","Lemon","Cookie Dough","Coffee","Banana","Praline Pecan","Chocolate Marshmallow","Neopolitan","Cookies N' Cream","Rocky Road","Strawberry","Birthday Cake","French Vanilla", "Raspberry","Mint","Cherry Vanilla","Butter Pecan","Peanut Butter Cup","Chocolate Chip","Chocolate Chip Cookie Dough","Chocolate Almond","Chocolate","Mint Chocolate Chip","Caramel","Moose Tracks","Fudge Brownie","Pistachio","M&M's","Vanilla","Cherry","Lemon","Cookie Dough","Coffee","Banana","Praline Pecan","Chocolate Marshmallow","Neopolitan","Cookies N' Cream","Rocky Road","Strawberry","Birthday Cake","French Vanilla"};
    public int [] colorDataSet = new int [] {Color.GRAY, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.WHITE, Color.BLACK};





    // TODO: Extend this to hit an endpoint to get PLCs
    public String getDefaultPlc(int i){
        if (i == 0){
            return DEFAULT_AD_PLC;
        } else if (i == 1){
            return DEFAULT_INTERSTITIAL_PLC;
        }
        return DEFAULT_AD_PLC;
    }


    // GET METHODS to access private variables


    public int getCoffeeCount(){
        return CoffeeCount;
    }
    public boolean getHasInit(){
        return hasInit;
    }
    public boolean getGDPRConsent(){
        return hasGDPRConsent;
    }
    public boolean getSupportA9() {return supportA9; }
    public Map<String, String> getPubKeys(){
        return pubKeys;
    }




    // MUTATOR METHODS to access private variables


    public void setCoffeeCount(int amt) {CoffeeCount = amt;}
    public void setInit(){
        hasInit = true;
    }
    public void setGDPRConsent(boolean v) {
        hasGDPRConsent = v;
    }
    public void setSupportA9(boolean v) {supportA9 = v; }
    public void setPubKeys() {
        pubKeys.put("type",  "expresso");
        pubKeys.put("content_rating",  "5 stars");
    }

    public void saveCoffeeCount() {
        writeSaveFile(String.valueOf(CoffeeCount));
    }
    public void incrementCOFFEE_COUNT(int amt){
        CoffeeCount += amt;
    }




    // FILE I/O METHODS


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
        if (readSaveFile().equals("")) {
            Log.d(LOG_TAG, "File not found, making new one");
            createSaveFile();
            writeSaveFile("0");     // For now, we'll just store the counter there and parse out the string / convert it
        } else {
            String output = readSaveFile();
            Log.d(LOG_TAG, "Contained in file: " + output);
            CoffeeCount = Integer.parseInt(output);
        }
    }

    public String readSaveFile() {

        String ret = "";

        try {
            InputStream inputStream = this.openFileInput("db.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
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