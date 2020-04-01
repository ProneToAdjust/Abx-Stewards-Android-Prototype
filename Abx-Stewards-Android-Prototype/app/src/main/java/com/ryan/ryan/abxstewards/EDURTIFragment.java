package com.ryan.ryan.abxstewards;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EDURTIFragment extends Fragment {

    public static EDURTIFragment newInstance() {
        return new EDURTIFragment();
    }

    public FragUtils frag_utils = new FragUtils(getContext());

    private Activity activity;

    private View view;

    private Spinner q1_spinner;
    private EditText q2_text_field;
    private ToggleButton q3_tgl_btn;
    private Spinner q4_spinner;
    private ToggleButton q5_tgl_btn;
    private ToggleButton q6_tgl_btn;
    private ToggleButton q8_tgl_btn;
    private ToggleButton q9_tgl_btn;
    private EditText q10_text_field;
    private EditText q11_text_field;
    private ToggleButton q12_tgl_btn;

    private ImageView edurtiInitialDescriptionImageView;

    private EditText q13_inital_1_text_field;
    private EditText q13_inital_2_text_field;
    private EditText q13_inital_3_text_field;
    private EditText q13_inital_4_text_field;

    private TableRow edurtiQ2Warning;
    private TableRow edurtiQ10Warning;
    private TableRow edurtiQ11Warning;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        frag_utils.setFragment_context(getContext());

        activity = getActivity();

        activity.setTitle("EDURTI");

        closeKeyboard();

        view = (View) inflater.inflate(R.layout.edurti_fragment, container, false);

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getContext().registerReceiver(new NetworkChangeReceiver(), intentFilter);

        initElementObjects();

        resetElementObjects();

        // Add and set the value listener and limits for the warnings to appear
        q2_text_field.addTextChangedListener(new valueWatcher(21,120,edurtiQ2Warning));
        q10_text_field.addTextChangedListener(new valueWatcher(34.0,46.5,edurtiQ10Warning));
        q11_text_field.addTextChangedListener(new valueWatcher(20,200,edurtiQ11Warning));

        edurtiInitialDescriptionImageView.setOnClickListener(new descriptionImageOnClick());

        // Add and set the listener and the next initial EditText element for the focus to jump to
        q13_inital_1_text_field.addTextChangedListener(new initialsTextWatcher(q13_inital_2_text_field));
        q13_inital_2_text_field.addTextChangedListener(new initialsTextWatcher(q13_inital_3_text_field));
        q13_inital_3_text_field.addTextChangedListener(new initialsTextWatcher(q13_inital_4_text_field));

        // Add and set the listener and the previous initial EditText element for the focus to jump to
        q13_inital_2_text_field.setOnKeyListener(new initialsOnKeyListener(q13_inital_1_text_field));
        q13_inital_3_text_field.setOnKeyListener(new initialsOnKeyListener(q13_inital_2_text_field));
        q13_inital_4_text_field.setOnKeyListener(new initialsOnKeyListener(q13_inital_3_text_field));

        // set input filters on q10 edit text to restrict to 2 digits and 1 decimal place
        q10_text_field.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(1)});

        final Button edurti_submit_btn = (Button) view.findViewById(R.id.edurtiSubmitBtn);

        initQ1Spinner(q1_spinner);

        initQ4Spinner(q4_spinner);

        q12_tgl_btn.setOnClickListener(new q12OnClick());

        edurti_submit_btn.setOnClickListener(new submitButtonOnClick());

        return view;
    }

    // Initializes the input and warning elements from the edurti_fragment.xml
    public void initElementObjects(){
        q1_spinner = (Spinner) view.findViewById(R.id.edurtiQ1Spinner);
        q2_text_field = (EditText) view.findViewById(R.id.edurtiQ2TextField);
        q3_tgl_btn = (ToggleButton) view.findViewById(R.id.edurtiQ3TglBtn);
        q4_spinner = (Spinner) view.findViewById(R.id.edurtiQ4Spinner);
        q5_tgl_btn = (ToggleButton) view.findViewById(R.id.edurtiQ5TglBtn);
        q6_tgl_btn = (ToggleButton) view.findViewById(R.id.edurtiQ6TglBtn);
        q8_tgl_btn = (ToggleButton) view.findViewById(R.id.edurtiQ8TglBtn);
        q9_tgl_btn = (ToggleButton) view.findViewById(R.id.edurtiQ9TglBtn);
        q10_text_field = (EditText) view.findViewById(R.id.edurtiQ10TextField);
        q11_text_field = (EditText) view.findViewById(R.id.edurtiQ11TextField);
        q12_tgl_btn = (ToggleButton) view.findViewById(R.id.edurtiQ12TglBtn);

        edurtiInitialDescriptionImageView = (ImageView) view.findViewById(R.id.edurtiInitialImageView);

        q13_inital_1_text_field = (EditText) view.findViewById(R.id.edurtiQ13Initial1);
        q13_inital_2_text_field = (EditText) view.findViewById(R.id.edurtiQ13Initial2);
        q13_inital_3_text_field = (EditText) view.findViewById(R.id.edurtiQ13Initial3);
        q13_inital_4_text_field = (EditText) view.findViewById(R.id.edurtiQ13Initial4);

        edurtiQ2Warning = (TableRow) view.findViewById(R.id.edurtiQ2WarningRow);
        edurtiQ10Warning = (TableRow) view.findViewById(R.id.edurtiQ10WarningRow);
        edurtiQ11Warning = (TableRow) view.findViewById(R.id.edurtiQ11WarningRow);

    }

    class valueWatcher implements TextWatcher{
        double lower_limit;
        double upper_limit;
        TableRow warningRow;

        public valueWatcher(double lower_limit, double upper_limit, TableRow warningRow){
            this.lower_limit = lower_limit;
            this.upper_limit = upper_limit;
            this.warningRow = warningRow;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String q_text = s.toString();

            if(!q_text.matches("")){
                double q_double = Double.parseDouble(q_text);

                if(q_double < lower_limit || q_double > upper_limit){
                    warningRow.setVisibility(View.VISIBLE);

                }
                else{
                    warningRow.setVisibility(View.GONE);
                }

            }
            else if(q_text.matches("")){
                warningRow.setVisibility(View.GONE);

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class descriptionImageOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Patient Initials")
                    .setMessage("Please provide the first alphabet of each word in the patient’s name")
                    .setPositiveButton("Close", null)
                    .show();

        }
    }

    class initialsTextWatcher implements TextWatcher{

        EditText nextInitialEditText;

        public initialsTextWatcher(EditText nextInitialEditText){
            this.nextInitialEditText = nextInitialEditText;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String initial_char = s.toString();

            if(!initial_char.matches("")){
                nextInitialEditText.requestFocus();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class initialsOnKeyListener implements View.OnKeyListener{

        EditText prevInitialEditText;

        public initialsOnKeyListener(EditText prevInitalEditText){
            this.prevInitialEditText = prevInitalEditText;

        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
            //If the keyevent is a key-down event on the "backspace" button
            if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {
                prevInitialEditText.requestFocus();
                prevInitialEditText.getText().clear();
            }
            return true;
        }
    }

    public void initQ1Spinner(Spinner spinner){
        List<String> q1_spinner_options = new ArrayList<String>();
        q1_spinner_options.add("JAN");
        q1_spinner_options.add("FEB");
        q1_spinner_options.add("MAR");
        q1_spinner_options.add("APR");
        q1_spinner_options.add("MAY");
        q1_spinner_options.add("JUN");
        q1_spinner_options.add("JUL");
        q1_spinner_options.add("AUG");
        q1_spinner_options.add("SEP");
        q1_spinner_options.add("OCT");
        q1_spinner_options.add("NOV");
        q1_spinner_options.add("DEC");

        ArrayAdapter<String> q1_spinner_data_adapter = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_item, q1_spinner_options);

        q1_spinner_data_adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(q1_spinner_data_adapter);

        spinner.setSelection(frag_utils.getMonth());

    }

    public void initQ4Spinner(Spinner spinner){
        List<String> q4_spinner_options = new ArrayList<String>();
        q4_spinner_options.add("CHINESE");
        q4_spinner_options.add("MALAY");
        q4_spinner_options.add("INDIAN");
        q4_spinner_options.add("OTHERS");

        ArrayAdapter<String> q4_spinner_data_adapter = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_item, q4_spinner_options);

        q4_spinner_data_adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(q4_spinner_data_adapter);

    }

    class q12OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            boolean isChecked = q12_tgl_btn.isChecked();

            if(isChecked){
                JSONObject prev_entry_record = frag_utils.getPreviousEntryRecord();

                if(prev_entry_record.has("previous_entry_record")){
                    Toast.makeText(getContext(),
                            "No previous entry recorded",
                            Toast.LENGTH_SHORT).show();

                }
                else{
                    setElements(prev_entry_record);

                }

            }
            else{
                resetElementObjects();

            }

        }
    }

    class submitButtonOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            // Get the values from input fields
            String q1_ans = q1_spinner.getSelectedItem().toString();
            String q2_ans_string = q2_text_field.getText().toString();
            Boolean q3_ans = q3_tgl_btn.isChecked();
            String q4_ans_string = q4_spinner.getSelectedItem().toString();
            Boolean q5_ans = q5_tgl_btn.isChecked();
            Boolean q6_ans = q6_tgl_btn.isChecked();
            Boolean q8_ans = q8_tgl_btn.isChecked();
            Boolean q9_ans = q9_tgl_btn.isChecked();
            String q10_ans_string = q10_text_field.getText().toString();
            String q11_ans_string = q11_text_field.getText().toString();
            Boolean q12_ans = q12_tgl_btn.isChecked();

            String q13_initial_1_string = q13_inital_1_text_field.getText().toString();
            String q13_initial_2_string = q13_inital_2_text_field.getText().toString();
            String q13_initial_3_string = q13_inital_3_text_field.getText().toString();
            String q13_initial_4_string = q13_inital_4_text_field.getText().toString();

            // Check for empty fields
            boolean patient_initals_is_empty = (q13_initial_1_string.matches("")&&
                    q13_initial_2_string.matches("")&&
                    q13_initial_3_string.matches("")&&
                    q13_initial_4_string.matches(""));

            if(q2_ans_string.matches("")||
                    q10_ans_string.matches("")||
                    q11_ans_string.matches("")||
                    patient_initals_is_empty){
                Toast.makeText(getContext(), "Some responses are missing!", Toast.LENGTH_SHORT).show();

                return;
            }

            // parse strings to respective double/int data type
            int q2_ans = Integer.parseInt(q2_ans_string);

            boolean q4_ans;
            if(q4_ans_string.equals("INDIAN")){
                q4_ans = true;

            }
            else{
                q4_ans = false;

            }

            // parse strings to respective double/int data type
            Double q10_ans = Double.parseDouble(q10_ans_string);
            int q11_ans = Integer.parseInt(q11_ans_string);

            // Check if field values are outside of restricted range
            if((q2_ans < 21 || q2_ans > 120) ||
                    (q10_ans < 34.0 || q10_ans > 46.5) ||
                    (q11_ans < 30 || q11_ans > 200)){
                Toast.makeText(getContext(), "All fields must be within restricted range", Toast.LENGTH_SHORT).show();

                return;

            }

            // assign question ans to variables with their representative names
            String visit_month = q1_ans;
            int age = q2_ans;
            boolean gender = q3_ans;
            boolean ethnicity = q4_ans;
            boolean history_of_cancer = q5_ans;
            boolean fever = q6_ans;
            boolean shortness_of_breath_history = q8_ans;
            boolean presence_of_giddiness = q9_ans;
            double highest_body_temp = q10_ans;
            int highest_pulse_rate = q11_ans;
            boolean same_as_prev_patient = q12_ans;
            String patient_initals = q13_initial_1_string + q13_initial_2_string +
                    q13_initial_3_string + q13_initial_4_string;

            //init probability variable
            double p_decision = 0;
            double p_lasso = 0;
            double p_logistic = 0;

            // get probability from decision tree model
            p_decision = getProbabilityDecisionTree(highest_body_temp, highest_pulse_rate);

            // get probability from lasso regression model
            p_lasso = getProbabilityLassoRegression(ethnicity, history_of_cancer,
                    fever, presence_of_giddiness,
                    highest_body_temp, highest_pulse_rate);

            // get probability from logistic regression model
            p_logistic = getProbabilityLogisticRegression(age, fever,
                    shortness_of_breath_history, presence_of_giddiness,
                    highest_body_temp, highest_pulse_rate);

            // init recommendation strings
            String decision_recommendation = "";
            String lasso_recommendation = "";
            String logistic_recommendation = "";
            String overall_recommendation = "";

            String recommended = view.getResources().getString(R.string.recommended);
            String not_recommended = view.getResources().getString(R.string.not_recommended);

            decision_recommendation = p_decision<0.675 ? recommended:not_recommended;
            lasso_recommendation = p_lasso<0.625 ? recommended:not_recommended;
            logistic_recommendation = p_logistic<0.6 ? recommended:not_recommended;

            overall_recommendation = (p_decision < 0.5 || p_lasso < 0.625 || p_logistic < 0.6)
                    ? recommended:not_recommended;

            String alert_message = overall_recommendation;


            // Add username to payload json
            String username = "";
            try {
                username = frag_utils.readCredentialsJSON().get("username").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String timestamp = frag_utils.getTimeStamp();

            String url = view.getResources().getString(R.string.post_url);

            JSONObject json = makeJSON(visit_month, age, gender, ethnicity, history_of_cancer,
                    fever, shortness_of_breath_history, presence_of_giddiness,
                    highest_body_temp, highest_pulse_rate, username, same_as_prev_patient,
                    patient_initals, p_decision, p_lasso, p_logistic, decision_recommendation,
                    lasso_recommendation, logistic_recommendation, overall_recommendation,
                    timestamp);

            showSubmitAlertDialogAndPost(alert_message, url, json);

            // Store the last posted data in a local json file
            JSONObject prev_entry_json = new JSONObject();

            try {
                prev_entry_json.put("q1", q1_ans);
                prev_entry_json.put("q2", q2_ans_string);
                prev_entry_json.put("q3", q3_ans);
                prev_entry_json.put("q4", q4_ans_string);
                prev_entry_json.put("q5", q5_ans);
                prev_entry_json.put("q6", q6_ans);
                prev_entry_json.put("q8", q8_ans);
                prev_entry_json.put("q9", q9_ans);
                prev_entry_json.put("q10", q10_ans_string);
                prev_entry_json.put("q11", q11_ans_string);
                prev_entry_json.put("q12", q12_ans);
                prev_entry_json.put("initial_1", q13_initial_1_string);
                prev_entry_json.put("initial_2", q13_initial_2_string);
                prev_entry_json.put("initial_3", q13_initial_3_string);
                prev_entry_json.put("initial_4", q13_initial_4_string);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            frag_utils.setPreviousEntryRecord(prev_entry_json);

            closeKeyboard();

            resetElementObjects();

        }
    }

    public double getProbabilityDecisionTree(double highest_body_temp,
                                             int highest_pulse_rate){
        double p = 0.0;

        if(highest_body_temp < 37.25){
            p = 0.737160120845921;
        }
        else if(highest_body_temp >= 38.85){
            p = 0.200;
        }
        else if(highest_body_temp >= 37.25 && highest_body_temp < 38.85){
            if(highest_pulse_rate < 102.5){
                p = 0.612244898;

            }
            else if(highest_pulse_rate >= 102.5){
                p = 0.266666667;

            }

        }

        return p;

    }

    public double getProbabilityLassoRegression(boolean ethnicity,
                                                boolean history_of_cancer,
                                                boolean fever,
                                                boolean presence_of_giddiness,
                                                double highest_body_temp,
                                                int highest_pulse_rate){
        double p = 0.0;

        // convert boolean to int 1/0
        int ethnicity_num = ethnicity ? 1 : 0;
        int cancer_num = history_of_cancer ? 1 : 0;
        int fever_num = fever ? 1 : 0;
        int giddiness_num = presence_of_giddiness ? 1 : 0;

        p = Math.exp(17.559653071 + (ethnicity_num * -0.087582939) + (cancer_num * -0.182490954) +
                (fever_num * -0.247903983) + (giddiness_num * 0.008771653) +
                (highest_body_temp * -0.422987865) + (highest_pulse_rate * -0.011455751));

        p = p/(1+p);

        return p;

    }

    public double getProbabilityLogisticRegression(int age,
                                                   boolean fever,
                                                   boolean shortness_of_breath_history,
                                                   boolean presence_of_giddiness,
                                                   double highest_body_temp,
                                                   int highest_pulse_rate){
        double p = 0.0;

        // convert boolean to int 1/0
        int fever_num = fever ? 1 : 0;
        int shortness_of_breath_history_num = shortness_of_breath_history ? 1 : 0;
        int giddiness_num = presence_of_giddiness ? 1 : 0;

        p = Math.exp(21.21197724 + (age * -0.01921583) + (fever_num * -0.60659876) +
                (shortness_of_breath_history_num * 0.55104701) + (giddiness_num * 1.34265642) +
                (highest_body_temp * -0.45608855) + (highest_pulse_rate * -0.02907569));

        p = p/(1+p);

        return p;

    }

    public class DecimalDigitsInputFilter implements InputFilter {

        private final int decimalDigits;

        /**
         * Constructor.
         *
         * @param decimalDigits maximum decimal digits
         */
        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {


            int dot_position = -1;
            int field_value_length = dest.length();

            // if first input char is ".", return nothing
            if(field_value_length == 0 && source.equals(".")){
                return "";
            }

            // iterate through current field value and get dot position, if present
            for (int i = 0; i < field_value_length; i++){
                char c = dest.charAt(i);

                if (c == '.' || c == ','){
                    dot_position = i;
                    break;

                }

            }

            // if field_value_length == 2 and input char is ".",
            // but if a dot exists in the field
            // allow number input, else return nothing
            if(field_value_length == 2 && !source.equals(".")){
                if(dot_position >= 0){
                    return null;
                }
                else{
                    return "";
                }

            }


            // if dot exists in field
            if (dot_position >= 0) {

                // protects against many dots
                // do not allow any more dot input
                if (source.equals(".") || source.equals(",")){
                    return "";

                }

                // if the text is entered before the dot
                // disallow further input if dot is at index position 2
                if (dend <= dot_position){
                    if(dot_position == 2){
                        return "";

                    }
                    else{
                        return null;

                    }

                }

                // restrict to specified decimal place
                if (field_value_length - dot_position > decimalDigits){
                    return "";

                }

            }

            return null;

        }

    }

    public JSONObject makeJSON(String visit_month,
                               int age,
                               boolean gender,
                               boolean ethnicity,
                               boolean history_of_cancer,
                               boolean fever,
                               boolean shortness_of_breath_history,
                               boolean presence_of_giddiness,
                               double highest_body_temp,
                               int highest_pulse_rate,
                               String username,
                               boolean same_as_prev_patient,
                               String patient_initials,
                               double p_decision,
                               double p_lasso,
                               double p_logistic,
                               String decision_recommendation,
                               String lasso_recommendation,
                               String logistic_recommendation,
                               String overall_recommendation,
                               String timestamp
                               ){
        // Get the numerical version of the month
        int visit_month_num = getMonthNumber(visit_month);

        // convert boolean to int 1/0
        int gender_num = gender ? 1 : 0;
        int ethnicity_num = ethnicity ? 1 : 0;
        int cancer_num = history_of_cancer ? 1 : 0;
        int fever_num = fever ? 1 : 0;
        int breath_num = shortness_of_breath_history ? 1 : 0;
        int giddiness_num = presence_of_giddiness ? 1 : 0;
        int same_as_prev_patient_num = same_as_prev_patient ? 1 : 0;

        JSONObject json = new JSONObject();

        try {

            JSONObject variables_object = new JSONObject();

            variables_object.put("visit_month", visit_month_num);
            variables_object.put("age", age);
            variables_object.put("gender", gender_num);
            variables_object.put("ethnicity", ethnicity_num);
            variables_object.put("history_of_cancer", cancer_num);
            variables_object.put("fever", fever_num);
            variables_object.put("shortness_of_breath", breath_num);
            variables_object.put("presence_of_giddiness", giddiness_num);
            variables_object.put("highest_body_temperature", highest_body_temp);
            variables_object.put("highest_pulse_rate", highest_pulse_rate);
            variables_object.put("username", username);
            variables_object.put("same_patient", same_as_prev_patient_num);
            variables_object.put("patient_initials", patient_initials);
            variables_object.put("source", "Android");

            json.put("variables", variables_object);

            JSONObject results_object = new JSONObject();

            results_object.put("decision_probability", p_decision);
            results_object.put("lasso_probability", p_lasso);
            results_object.put("logistic_probability", p_logistic);
            results_object.put("decision_recommendation", decision_recommendation);
            results_object.put("lasso_recommendation", lasso_recommendation);
            results_object.put("logistic_recommendation", logistic_recommendation);
            results_object.put("overall_recommendation", overall_recommendation);
            results_object.put("timestamp", timestamp);

            json.put("results", results_object);

            return json;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void showSubmitAlertDialogAndPost(String results, String url, JSONObject json) {
        DialogFragment newFragment = new SubmitAlertDialogFragment();

        Bundle df_args = new Bundle();
        df_args.putInt("layout_id", R.layout.edurti_fragment_results);
        df_args.putString("results", results);
        df_args.putString("url" , url);
        df_args.putString("json_string", json.toString());

        newFragment.setCancelable(false);

        newFragment.setArguments(df_args);
        newFragment.show(getFragmentManager(), "third_fragment_results");

    }

    public int getMonthNumber(String month){
        int visit_month_num = 0;

        switch(month){
            case "JAN": visit_month_num = 1; break;
            case "FEB": visit_month_num = 2; break;
            case "MAR": visit_month_num = 3; break;
            case "APR": visit_month_num = 4; break;
            case "MAY": visit_month_num = 5; break;
            case "JUN": visit_month_num = 6; break;
            case "JUL": visit_month_num = 7; break;
            case "AUG": visit_month_num = 8; break;
            case "SEP": visit_month_num = 9; break;
            case "OCT": visit_month_num = 10; break;
            case "NOV": visit_month_num = 11; break;
            case "DEC": visit_month_num = 12; break;

        }

        return visit_month_num;

    }

    public int getEthnicityNumber(String ethnicity){
        int ethnicity_num = 0;

        switch(ethnicity){
            case "CHINESE": ethnicity_num = 1; break;
            case "MALAY": ethnicity_num = 2; break;
            case "INDIAN": ethnicity_num = 3; break;
            case "OTHERS": ethnicity_num = 4; break;

        }

        return ethnicity_num;

    }

    public void closeKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();

        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }

    public void resetElementObjects(){
        q1_spinner.setSelection(frag_utils.getMonth());
        q2_text_field.getText().clear();
        q3_tgl_btn.setChecked(false);
        q4_spinner.setSelection(0);
        q5_tgl_btn.setChecked(false);
        q6_tgl_btn.setChecked(false);
        q8_tgl_btn.setChecked(false);
        q9_tgl_btn.setChecked(false);
        q10_text_field.getText().clear();
        q11_text_field.getText().clear();
        q12_tgl_btn.setChecked(false);

        q13_inital_1_text_field.getText().clear();
        q13_inital_2_text_field.getText().clear();
        q13_inital_3_text_field.getText().clear();
        q13_inital_4_text_field.getText().clear();

        edurtiQ2Warning.setVisibility(View.GONE);
        edurtiQ10Warning.setVisibility(View.GONE);
        edurtiQ11Warning.setVisibility(View.GONE);

    }

    public void setElements(JSONObject prev_entry_json){
        try {
            q1_spinner.setSelection(getMonthNumber(prev_entry_json.get("q1").toString()) - 1);
            q2_text_field.setText(prev_entry_json.get("q2").toString());
            q3_tgl_btn.setChecked((Boolean) prev_entry_json.get("q3"));
            q4_spinner.setSelection(getEthnicityNumber(prev_entry_json.get("q4").toString()) - 1);
            q5_tgl_btn.setChecked((Boolean) prev_entry_json.get("q5"));
            q6_tgl_btn.setChecked((Boolean) prev_entry_json.get("q6"));
            q8_tgl_btn.setChecked((Boolean) prev_entry_json.get("q8"));
            q9_tgl_btn.setChecked((Boolean) prev_entry_json.get("q9"));
            q10_text_field.setText(prev_entry_json.get("q10").toString());
            q11_text_field.setText(prev_entry_json.get("q11").toString());
            q13_inital_1_text_field.setText(prev_entry_json.get("initial_1").toString());
            q13_inital_2_text_field.setText(prev_entry_json.get("initial_2").toString());
            q13_inital_3_text_field.setText(prev_entry_json.get("initial_3").toString());
            q13_inital_4_text_field.setText(prev_entry_json.get("initial_4").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            int status = NetworkUtil.getConnectivityStatusString(context);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {

                } else {
                    try {
                        // Read from "unsent.json" to get unsent submissions, if any
                        JSONArray jsonArray = frag_utils.readJSONArray("unsent.json");

                        JSONObject jsonObjectIterable = null;

                        // If "unsent.json" is not empty, post the stored unsent data
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectIterable = jsonArray.getJSONObject(i);

                                frag_utils.postData(view.getResources()
                                        .getString(R.string.post_url), jsonObjectIterable);

                            }

                            // Clear "unsent.json" after posting unsent data
                            frag_utils.clearJSONArray("unsent.json");

                        }

                    } catch(JSONException e){
                        e.printStackTrace();

                    }

                }

            }

        }

    }

}
