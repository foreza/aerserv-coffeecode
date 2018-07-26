package vartyr.coffeecounter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;
import java.util.Random;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.ViewHolder> {

    private static final String TAG = "CustomViewAdapter";
    private int[] placementDataSet;
    private String[] mDataSet;
    private int[] mColorSet;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Button buttonView;
        private final TextView placeholderView;
        private final Context context;
        private AerServBanner banner = null;
        public int color = Color.TRANSPARENT;
        public String plc = "380000";
        public Boolean bannerDisplaying = false;

        public ViewHolder(View v) {
            super(v);


            Log.d(TAG, "ViewHolder extends RecyclerView.ViewHolder create ");

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                }
            });
            placeholderView = (TextView) v.findViewById(R.id.banner_placeholder);
            buttonView = (Button) v.findViewById(R.id.button_send);
            banner = (AerServBanner) v.findViewById(R.id.banner_recycle);
            context = (Context) v.getContext();
        }

        /** Called when the user touches the button */
        public void doSomething(View view) {
            Log.d(TAG, "Detected a button press");
        }


        public TextView getPlaceholderView() {
            return placeholderView;
        }

        public Button getButtonView(){ return buttonView;}

        public AerServBanner getBanner() {
            Log.d(TAG, "getBanner getBanner");
            return banner;
        }


        public Context getContext() {
            Log.d(TAG, "getContext");
            return context;
        }

    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomViewAdapter(String[] dataSet, int[] colorSet) {
        mDataSet = dataSet;
        placementDataSet = new int[dataSet.length];
        mColorSet = colorSet;
        Log.d(TAG, "DataSet created of size: " + placementDataSet.length);

    }


    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Log.d(TAG, "ViewHolder onCreateViewHolder");

        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    public void showBanner(ViewHolder viewHolder) {
        AerServConfig config = new AerServConfig(viewHolder.getContext(), viewHolder.plc);
        AerServBanner banner = (AerServBanner) viewHolder.getBanner();
        viewHolder.bannerDisplaying = true;
//        banner.configure(config).show();
    }

    public void killBanner(ViewHolder viewHolder) {
        AerServBanner banner = (AerServBanner) viewHolder.getBanner();
        viewHolder.bannerDisplaying = false;
//        banner.kill();
    }






    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }


    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set to - " + mDataSet[position]);

        // Get element from your dataset at this position, and replace
        if (position % 10 == 0)
        {
            Log.d(TAG, "This spot should have an ad and no color");
            viewHolder.getPlaceholderView().setText("");
            viewHolder.getButtonView().setVisibility(View.INVISIBLE);

            if (!viewHolder.bannerDisplaying){
                Log.d(TAG, "ADDING AD");
                placementDataSet[position] = -1;
                showBanner(viewHolder);
            }
        } else {

            Log.d(TAG, "This spot should have no ad and no data");

            if (viewHolder.bannerDisplaying) {
                Log.d(TAG, "KILLING AD");
                placementDataSet[position] = 0;
                killBanner(viewHolder);
            }


            viewHolder.getPlaceholderView().setText(mDataSet[position]);
            viewHolder.getButtonView().setVisibility(View.VISIBLE);
            viewHolder.getPlaceholderView().setBackgroundColor(placementDataSet[position]);
            viewHolder.getButtonView().setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(TAG, "Clicked on: " + position + " -> " + mDataSet[position]);
                    String msg = "Clicked on: " + position + " -> " + mDataSet[position];
                    Toast.makeText(viewHolder.getContext(), msg, Toast.LENGTH_SHORT).show();
                    final int random = new Random().nextInt(6);
                    placementDataSet[position] = mColorSet[random];
                    viewHolder.getPlaceholderView().setBackgroundColor(placementDataSet[position]);
                }
            });


        }





    }

}