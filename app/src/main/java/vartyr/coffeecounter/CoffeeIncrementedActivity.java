package vartyr.coffeecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServInterstitial;

import static vartyr.coffeecounter.MainActivity.DEFAULT_PLC;

public class CoffeeIncrementedActivity extends AppCompatActivity {


    // This view will provide an interstitial
    private AerServInterstitial interstitial;

    private static final String LOG_TAG = "CoffeeCounter_SampleApp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_incremented);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        loadInterstitial();

        String message = Integer.toString(intent.getIntExtra("COFFEE_COUNT", 0));

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.coffeeCountView_Incremented);
        textView.setText(message);
    }

    public void loadInterstitial() {

        Log.d(LOG_TAG, "~~~ Loading Interstitial ");


        final AerServConfig config = new AerServConfig(this, DEFAULT_PLC)
                .setDebug(true)
                .setVerbose(true);
        interstitial = new AerServInterstitial(config);
        interstitial.show();
    }

}
