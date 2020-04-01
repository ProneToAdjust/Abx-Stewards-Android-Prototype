package com.ryan.ryan.abxstewards;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class inflates a DialogFragment for the user to make their final decision and,
 * if the device is connected to the internet, upload the result data to the server or
 * store the data locally for a later upload
 */
public class SubmitAlertDialogFragment extends DialogFragment {


    public FragUtils fragUtils = new FragUtils(getActivity());

    private View view;

    private TextView results_info_text;
    private ToggleButton results_action_yes_toggle;
    private ToggleButton results_action_no_toggle;
    private Button results_confirm_button;

    private int layout_id;
    private String results;
    private String url;
    private String json_string;
    private JSONObject json;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        fragUtils.setFragment_context(getActivity());

        this.layout_id = getArguments().getInt("layout_id");
        this.results = getArguments().getString("results");
        this.url = getArguments().getString("url");
        this.json_string = getArguments().getString("json_string");

        try {
            this.json = new JSONObject(this.json_string);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(this.layout_id, null);

        builder.setView(view);


        // Add if statement to check layout id

        if(this.layout_id == R.layout.edurti_fragment_results){
            results_info_text = (TextView) view.findViewById(R.id.edurti_results_info_text);
            results_action_yes_toggle = (ToggleButton) view.findViewById(R.id.edurtiActionTglBtnYes);
            results_action_no_toggle = (ToggleButton) view.findViewById(R.id.edurtiActionTglBtnNo);
            results_confirm_button = view.findViewById(R.id.submitDismissButton);

        }

        results_info_text.setText(this.results);

        results_action_no_toggle.setChecked(true);

        results_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        // on click alternate the state of the yes and no buttons
        results_action_yes_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(results_action_yes_toggle.isChecked() == true){
                    results_action_no_toggle.setChecked(false);

                }
                else if(results_action_yes_toggle.isChecked() == false){
                    results_action_yes_toggle.setChecked(true);

                }

            }
        });

        // on click alternate the state of the yes and no buttons
        results_action_no_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(results_action_no_toggle.isChecked() == true){
                    results_action_yes_toggle.setChecked(false);

                }
                else if(results_action_no_toggle.isChecked() == false){
                    results_action_no_toggle.setChecked(true);

                }

            }
        });

        return builder.create();

    }


    // on dismiss, upload the data to the server or store the data on a local json file for later
    // upload
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);

        // get the connection status of the device
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // get the binary value of the button state
        boolean prescribed_antibiotics = results_action_yes_toggle.isChecked();
        int prescribed_antibiotics_num = prescribed_antibiotics ? 1:0;

        // add the prescribed_antibiotics_num tot the json to be uploaded to the server
        try {
            JSONObject results_object = this.json.getJSONObject("results");
            results_object.put("prescribed_antibiotics", prescribed_antibiotics_num);

            this.json.put("results", results_object);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        // if the device is connected to the internet, upload the result to the server.
        // else store the data in a local json file to br uploaded when the app detects
        // an internet connection
        if(isConnected){
            fragUtils.postData(this.url, this.json);

            try {
                // Read from "unsent.json" to get unsent submissions, if any
                JSONArray jsonArray = fragUtils.readJSONArray("unsent.json");

                JSONObject jsonObjectIterable = null;

                // If "unsent.json" is not empty, post the stored unsent data
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObjectIterable = jsonArray.getJSONObject(i);

                        fragUtils.postData(view.getResources()
                                .getString(R.string.post_url), jsonObjectIterable);

                    }

                    // Clear "unsent.json" after posting unsent data
                    fragUtils.clearJSONArray("unsent.json");

                }

            } catch(JSONException e){
                e.printStackTrace();

            }

            Toast.makeText(getActivity(), "Your entry has successfully been recorded", Toast.LENGTH_SHORT).show();

        }
        else{
            // add the data to the unsent.json file
            JSONArray jsonArray = fragUtils.readJSONArray("unsent.json");
            jsonArray.put(this.json);

            fragUtils.writeJSONArray("unsent.json", jsonArray);

            new AlertDialog.Builder(getActivity())
                    .setTitle("Save successful!")
                    .setMessage("Data has been save locally. Data will be uploaded to server when an internet connection is detected!")
                    .setPositiveButton("Ok", null).show();

        }

    }

}
