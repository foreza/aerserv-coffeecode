package vartyr.coffeecounter;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/*

This activity allows us to test out banners in a recycler view.
There are often some implementations by publishers where they will include banners in the view.

 */

public class DessertMenuRecycler extends AppCompatActivity {

    private GlobalClass globalVariable;         // To grab VC or anything we need

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessert_menu_recycler);

        globalVariable = (GlobalClass) getApplicationContext();
        initializeRecyclerView();                                   // Init the recycler view
    }


    // TODO: COMMENT
    private void initializeRecyclerView() {

        // Set up the recycler view, use a linear layoutmanager, feed data sets
        RecyclerView mRecyclerView = findViewById(R.id.coffee_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final RecyclerView.Adapter mAdapter = new CustomViewAdapter(globalVariable.dessertDataSet, globalVariable.colorDataSet, globalVariable.DEFAULT_AD_PLC);
        mRecyclerView.setAdapter(mAdapter);
        runOnUiThread(new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}
