package vartyr.coffeecounter;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class BackGroundBanner extends AppCompatActivity {

    private GlobalClass globalVariable;                                                 // We'll be accessing the background banner here
    private static String LOG_TAG;                                                      // Log tag


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_ground_banner);

        globalVariable = (GlobalClass) getApplicationContext();                         // Get an instance of the singleton class before anything else is done
        LOG_TAG = globalVariable.LOG_TAG;
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.bgbannerholder);     // Get a reference to the relative layout

        Resources r = getResources();
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Math.round(height)); // Note: doc says layoutparam, missing the 's'
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.bottomMargin = 50;

        parent.addView(globalVariable.injectBanner(), lp);                              // Inject our banner into the view
        globalVariable.attemptShowAd();                                                 // Show the ad
    }


    // On back, we want to send the incremented amount back to the MainActivity
    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "Back button(hardware) pressed");
        Intent returnIntent = this.getIntent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = this.getIntent();
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
