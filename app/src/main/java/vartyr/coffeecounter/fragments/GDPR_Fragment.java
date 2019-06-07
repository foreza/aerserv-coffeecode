package vartyr.coffeecounter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aerserv.sdk.AerServSdk;

import vartyr.coffeecounter.R;


public class GDPR_Fragment extends Fragment {

    private static String LOG_TAG = "[CC: GDPR DEBUG]";

    // Fragment interaction listener
    private OnFragmentInteractionListener mListener;

    // Leave this constructor empty
    public GDPR_Fragment() {
        // Required empty public constructor
    }


    public static GDPR_Fragment newInstance() {
        GDPR_Fragment fragment = new GDPR_Fragment();
        Log.d(LOG_TAG, "newInstance");
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "onCreateView");
        final View view = inflater.inflate(R.layout.fragment_gdpr,
                container, false);

        // Set up binding for YES button
        Button buttonYes = (Button) view.findViewById(R.id.gdpr_yes);
        buttonYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AerServSdk.setGdprConsentFlag(view.getContext(), true);
                Log.d(LOG_TAG, "GDPR Consent indicated");
                mListener.onSelection(true);
            }
        });

        // Set up binding for NO button
        Button buttonNo = (Button) view.findViewById(R.id.gdpr_no);
        buttonNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AerServSdk.setGdprConsentFlag(view.getContext(), false);
                Log.d(LOG_TAG, "GDPR Consent not given");
                mListener.onSelection(false);
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            Log.d(LOG_TAG, "onAttach");

        } else {
            Log.d(LOG_TAG, "onAttach failed, context missing?");
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(LOG_TAG, "onDetach");

    }


    public interface OnFragmentInteractionListener {
        // Interface method that will convey the selected value to the main activity and also close it out.
        void onSelection(Boolean sel);
    }
}
