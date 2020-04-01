package com.ryan.ryan.abxstewards;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class inflates the login fragment and handles the authentication into the EDURTI fragment
 */
public class loginFragment extends Fragment {

    private FragUtils fragUtils;

    private EditText usernameTextField;
    private EditText passwordTextField;
    private Button loginButton;

    public static loginFragment newInstance(){
        return new loginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = (View) inflater.inflate(
                R.layout.login_fragment, container, false);

        final Activity activity = getActivity();

        activity.setTitle("Login");

        fragUtils = new FragUtils(getContext());

        usernameTextField = (EditText) view.findViewById(R.id.usernameTextField);
        passwordTextField = (EditText) view.findViewById(R.id.passwordTextField);
        loginButton = (Button) view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new LoginButtonClick());

        return view;


    }

    class LoginButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            loginButtonClicked();

        }
    }

    // On click, get and authenticate the entered username and password from the text fields
    private void loginButtonClicked(){
        String entered_id = usernameTextField.getText().toString();
        String entered_password = passwordTextField.getText().toString();

        // check if fields are empty
        if(entered_id.equals("") || entered_password.equals("")){
            Toast.makeText(
                    getContext(),
                    "Fields must not be empty", Toast.LENGTH_SHORT).show();

        }
        else{
//            JSONObject entered_credentials = new JSONObject();

            if(entered_id.matches("username") && entered_password.equals("password")){
                replaceFragments(EDURTIFragment.class);

            }
            else{
                Toast.makeText(getContext(),"Incorrect username or password",
                        Toast.LENGTH_SHORT).show();

                passwordTextField.setText("");

            }

            // commented out the portion for server credentials authentication
                    /*try{
                        entered_credentials.put("username", entered_id);
                        entered_credentials.put("password", entered_password);

                    }catch(JSONException e){

                    }

                    postCredentials(view.getResources()
                            .getString(R.string.auth_url), entered_credentials);*/

        }

    }

    // Function to replace the current fragment with the fragment parameter
    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
                .commit();
    }

    // Function to post the entered credentials to the authentication server
    // Feature is removed due to decoupling from production server
    public void postCredentials(String url, final JSONObject json){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checkResponse(response, json);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }

        });

        requestQueue.add(json_obj_req);
    }

    // Function to check the json response from the authentication server
    private void checkResponse(JSONObject response, JSONObject creds){
        try {
            if(response.get("response").equals("valid")){
                // Replace the current fragment with the EDURTI fragment
                replaceFragments(EDURTIFragment.class);

                // Write the valid credentials to a json file
                fragUtils.writeCredentialsJSON(creds);

            }
            else{
                // Show the authentication error with a toast message
                Toast.makeText(
                        getContext(),
                        "Incorrect username or password", Toast.LENGTH_SHORT).show();

                // Clear the password field
                passwordTextField.setText("");

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

}
