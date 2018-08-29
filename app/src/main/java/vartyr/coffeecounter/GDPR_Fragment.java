package vartyr.coffeecounter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aerserv.sdk.AerServSdk;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GDPR_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GDPR_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class GDPR_Fragment extends Fragment {

    // TODO: How do we pass in the GV? Get Application context does not work here
    private GlobalClass globalVariable;
    private static String LOG_TAG = "[CC: GDPR DEBUG]";

    // TODO: See if we can remove these
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Fragment interaction listener/
    private OnFragmentInteractionListener mListener;

    // Leave this constructor empty
    public GDPR_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GDPR_Fragment.
     */
    // TODO: Rename and change types and number of parameters as required
    public static GDPR_Fragment newInstance(String param1, String param2) {
        GDPR_Fragment fragment = new GDPR_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d(LOG_TAG, "newInstance");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d(LOG_TAG, "onCreateView");
        final View view = inflater.inflate(R.layout.fragment_gdpr,
                container, false);
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



        // GDPR RELATED METHODS

//        public void acceptConsent(View view) {
////        AerServSdk.setGdprConsentFlag(this.getContext(), true);
//            Log.d(LOG_TAG, "GDPR Consent indicated");
////        Intent returnIntent = this.getIntent();
////        setResult(RESULT_OK, returnIntent);
////        finish();
//        }
//
//        public void denyConsent(View view){
//            Log.d(LOG_TAG, "GDPR Consent not given");
////        Intent returnIntent = this.getIntent();
////        setResult(RESULT_CANCELED, returnIntent);
////        finish();
//        }

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



    // TODO: Rename method, update argument and hook method into UI event (FOR LATER)
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
            Log.d(LOG_TAG, "onButtonPressed, mListener != null ");

        }
        Log.d(LOG_TAG, "onButtonPressed, mListener == null");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onSelection(Boolean sel);
    }
}
