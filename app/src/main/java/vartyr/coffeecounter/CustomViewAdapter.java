package vartyr.coffeecounter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;

import org.w3c.dom.Text;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.ViewHolder> {
    private static final String TAG = "CustomViewAdapter";

    private String[] mDataSet;
    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final TextView placeholderView;
        private final Context context;
        private AerServBanner banner = null;

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
            textView = (TextView) v.findViewById(R.id.textView);
            placeholderView = (TextView) v.findViewById(R.id.banner_placeholder);
            banner = (AerServBanner) v.findViewById(R.id.banner_recycle);
            context = (Context) v.getContext();
        }

        public TextView getTextView() {
            Log.d(TAG, "TextView getTextView");
            return textView;
        }

        public TextView getPlaceholderView() {
            return placeholderView;
        }

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
    public CustomViewAdapter(String[] dataSet) {
        mDataSet = dataSet;
        Log.d(TAG, "DataSet created");

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

    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        Log.d(TAG, "Element " + position + " set.");
//
//        // Get element from your dataset at this position and replace the contents of the view
//        // with that element
//        viewHolder.getTextView().setText(mDataSet[position]);
//
//        if (position % 5 == 0) {
//            Log.d(TAG, "ADDING AN AD");
//            showBanner(viewHolder);
//            // viewHolder.getPlaceholderView().setBackgroundColor(Color.RED);
//
//        } else {
//            killBanner(viewHolder);
//            // viewHolder.getPlaceholderView().setBackgroundColor(Color.BLUE);
//
//        }
//
//    }


    public void showBanner(ViewHolder viewHolder) {
        AerServConfig config = new AerServConfig(viewHolder.getContext(), "380000");
        AerServBanner banner = (AerServBanner) viewHolder.getBanner();
        banner.configure(config).show();
    }

    public void killBanner(ViewHolder viewHolder) {
        AerServBanner banner = (AerServBanner) viewHolder.getBanner();
        banner.kill();
    }

    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }


    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position, and replace
        if (position % 5 == 0) {
            Log.d(TAG, "ADDING AN AD");
            showBanner(viewHolder);

        } else {
            Log.d(TAG, "KILLING THE AD");
            killBanner(viewHolder);

        }

    }

}