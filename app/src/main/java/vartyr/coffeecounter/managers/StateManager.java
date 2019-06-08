package vartyr.coffeecounter.managers;
import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StateManager {

    private StateManager() { }

    private static final StateManager ourInstance = new StateManager();

    public static StateManager getInstance() {
        return ourInstance;
    }

    public String LOG_TAG = "StateManager";
    private int CoffeeCount = 0;

    // FILE I/O METHODS

    public void writeSaveFile (Context ctx, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (ctx.openFileOutput("db.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void createSaveFile(Context ctx){
        File directory = ctx.getFilesDir();
        File file = new File(directory, "db.txt");
    }

    public void initSaveFile(Context ctx){
        // If the save file is empty
        if (readSaveFile(ctx).equals("")) {
            Log.d(LOG_TAG, "File not found, making new one");
            createSaveFile(ctx);
            writeSaveFile(ctx,"0");     // For now, we'll just store the counter there and parse out the string / convert it
        } else {
            String output = readSaveFile(ctx);
            Log.d(LOG_TAG, "Contained in file: " + output);
        }
    }

    public String readSaveFile(Context ctx) {

        String ret = "";

        try {
            InputStream inputStream = ctx.openFileInput("db.txt");

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
        } catch (NullPointerException e) {
            Log.e("NPE", "File not found: " + e.toString());
        }
        catch (FileNotFoundException e) {
            Log.e("Read amt activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Read ant activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    public int getCoffeeCount(){
        return CoffeeCount;
    }

    public void setCoffeeCount(int amt) {
        CoffeeCount = amt;
    }


    public void saveCoffeeCount(Context ctx) {
        writeSaveFile(ctx, String.valueOf(CoffeeCount));
    }


    public void incrementCOFFEE_COUNT(int amt){
        CoffeeCount += amt;
    }



}
