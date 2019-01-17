package vartyr.coffeecounter;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aerserv.sdk.utils.ReflectionUtils;
import com.aerserv.sdk.utils.UrlBuilder;

//ADMOB
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.google.android.gms.common.GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE;

public class ApplicationSettings extends AppCompatActivity {

    private GlobalClass globalVariable;         // To grab VC or anything we need
    private static String LOG_TAG;              // Log tag

    private Map<String,String> versionStrings;  // Store our version strings here.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);
        globalVariable = (GlobalClass) getApplicationContext();

        initializeTextViews();
        generateStringMapDisplay();

    }

    // Method for grouping all the generic text updates
    public void initializeTextViews() {

        versionStrings = new HashMap<String,String>();

        versionStrings.put("Core SDK:", getCoreSDKVersionString());
        versionStrings.put("GDPR Compliance:", getGDPRComplianceStatus());
        versionStrings.put("Google Play Services:", getGoogleSDKVersionString());

        // TODO: Do others

    }

    // Private method that iterates through the map and shows all the versions that are stored
    private void generateStringMapDisplay(){


        LinearLayout layout = (LinearLayout) this.findViewById(R.id.applicationSettingLayout);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, 5, 0, 0);

        Iterator it = versionStrings.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();

            Log.d(LOG_TAG, pair.getKey() + " = " + pair.getValue());

            TextView tv=new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText(pair.getKey() + " " + pair.getValue());
            layout.addView(tv);

            it.remove(); // avoids a ConcurrentModificationException
        }


    }


    // Retrieve the InMobi / AerServ unified SDK status string
    private String getCoreSDKVersionString(){

//        TextView version = findViewById(R.id.sdkVersion);
//        version.setText(getString(R.string.aerserv_sdk_version, UrlBuilder.VERSION));
        return getString(R.string.aerserv_sdk_version, UrlBuilder.VERSION);

    }


    // Get the GDPR status and form a compliance string from the global variable, then show it
    private String getGDPRComplianceStatus() {

        // TextView GDPRConsentView = findViewById(R.id.gdprStatus);

        if (!globalVariable.getGDPRConsent()) {
//            GDPRConsentView.setText(R.string.gdprconsentview_notConsent);
//            GDPRConsentView.setTextColor(Color.parseColor("#C40824"));
            return getString(R.string.gdprconsentview_notConsent);
        } else {
//            GDPRConsentView.setText(R.string.gdprconsentview_didConsent);
//            GDPRConsentView.setTextColor(Color.parseColor("#5BB55E"));
            return getString(R.string.gdprconsentview_didConsent);

        }

    }

    // Utility method use for checking mediated SDK dependencies
    protected static boolean util_checkIfDependency(String adClassName) {
        if (!ReflectionUtils.canFindClass(adClassName)) {

            Log.d(LOG_TAG, "ReflectionUtils failed, reflection did not find string name: " + adClassName);


            return false;
        } else return true;

    }


    private String getGoogleSDKVersionString(){

        String gmsClassName = "com.google.android.gms.ads.AdView";
        int test = GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE;

        return "boop";

//        TextView gmsVersion = findViewById(R.id.googleVersion);
//        gmsVersion.setText(Integer.toString(v2));
//
    }

}