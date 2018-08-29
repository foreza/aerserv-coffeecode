package vartyr.coffeecounter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aerserv.sdk.AerServSdk;

public class GDPRConsent extends Fragment {

    private String LOG_TAG;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_gdprconsent, container, false);
    }

    public void acceptConsent(View view) {
//        AerServSdk.setGdprConsentFlag(this.getContext(), true);
        Log.d(LOG_TAG, "GDPR Consent indicated");
//        Intent returnIntent = this.getIntent();
//        setResult(RESULT_OK, returnIntent);
//        finish();
    }

    public void denyConsent(View view){
        Log.d(LOG_TAG, "GDPR Consent not given");
//        Intent returnIntent = this.getIntent();
//        setResult(RESULT_CANCELED, returnIntent);
//        finish();
    }

//    @Override
//    public void onBackPressed() {
//        Log.d(LOG_TAG, "Back button(hardware) pressed");
//        // do nothing
//    }
}
