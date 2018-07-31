package vartyr.coffeecounter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aerserv.sdk.AerServSdk;

public class GDPRConsent extends AppCompatActivity {

    private String LOG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdprconsent);

        // Get needed values from singleton class
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        LOG_TAG = globalVariable.LOG_TAG;


    }

    public void acceptConsent(View view) {
        AerServSdk.setGdprConsentFlag(this, true);
        Log.d(LOG_TAG, "GDPR Consent indicated");
        Intent returnIntent = this.getIntent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void denyConsent(View view){
        Log.d(LOG_TAG, "GDPR Consent not given");
        Intent returnIntent = this.getIntent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "Back button(hardware) pressed");
        // do nothing
    }
}
