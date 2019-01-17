package vartyr.coffeecounter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.aerserv.sdk.utils.UrlBuilder;

public class ApplicationSettings extends AppCompatActivity {

    private GlobalClass globalVariable;         // To grab VC or anything we need
    private static String LOG_TAG;              // Log tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);
        globalVariable = (GlobalClass) getApplicationContext();

        initializeTextViews();                                       // Handle Text View loading

    }

    // Method for grouping all the generic text updates
    public void initializeTextViews() {

        showCoreSDKVersionString();
        showGDPRComplianceStatus();
        // TODO: Do others
    }


    // Get the InMobi / AerServ unified SDK status and show it
    private void showCoreSDKVersionString(){

        TextView version = findViewById(R.id.sdkVersion);
        version.setText(getString(R.string.aerserv_sdk_version, UrlBuilder.VERSION));

    }


    // Get the GDPR status and form a compliance string from the global variable, then show it
    private void showGDPRComplianceStatus() {

        TextView GDPRConsentView = findViewById(R.id.gdprStatus);

        if (!globalVariable.getGDPRConsent()) {
            GDPRConsentView.setText(R.string.gdprconsentview_notConsent);
            GDPRConsentView.setTextColor(Color.parseColor("#C40824"));
        } else {
            GDPRConsentView.setText(R.string.gdprconsentview_didConsent);
            GDPRConsentView.setTextColor(Color.parseColor("#5BB55E"));

        }

    }

}