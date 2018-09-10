package vartyr.coffeecounter;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aerserv.sdk.*;

public class BackGroundBanner extends AppCompatActivity {

    private GlobalClass globalVariable;         // To grab VC or anything we need

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_ground_banner);
        globalVariable = (GlobalClass) getApplicationContext();     // Get an instance of the singleton class before anything else is done



        //  TODO: Configure the layout params so that we position the banner below
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);

        Resources r = getResources();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, r.getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Math.round(width), Math.round(height)); // Note: doc says layoutparam, missing the 's'


        lp.addRule(RelativeLayout.BELOW, findViewById(R.id.bgbanner).getId());


        ((ViewGroup) findViewById(android.R.id.content)).addView(globalVariable.injectBanner(),lp);
        globalVariable.attemptShowAd();
    }


}
