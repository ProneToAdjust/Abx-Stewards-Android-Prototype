package com.ryan.ryan.abxstewards;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is main class that on startup, will first check for stored credentials.
 * If there are stored credentials, it will display the EDURTI fragment, if not it will display the
 * login fragment.
 */
public class MainActivity extends AppCompatActivity {
    private FragUtils fragUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragUtils = new FragUtils(this);

        boolean has_credentials = false;

        // on startup check for stored credentials from previous login
        try {
            has_credentials = fragUtils.hasCredentials();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(has_credentials){
            // display edurti fragment
            Fragment fragment = (Fragment) EDURTIFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment, "edurti_fragment").commit();

        }
        else{
            // display login fragment
            Fragment fragment = (Fragment) loginFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment, "login_fragment").commit();

        }

    }

    //On resume verify the stored login credentials with the server
    //If failed, display a black screen with an error alert
    @Override
    protected void onResume() {
        super.onResume();

        Fragment edurti_fragment = (Fragment)getSupportFragmentManager()
                .findFragmentByTag("edurti_fragment");

        // Check if EDURTIFragment is the visible fragment
        if (edurti_fragment != null && edurti_fragment.isVisible()) {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            // Get stored credentials json
            JSONObject json = fragUtils.readCredentialsJSON();

            // Make authentication post request to server
            JsonObjectRequest json_obj_req = new JsonObjectRequest(
                    Request.Method.POST, getResources()
                    .getString(R.string.auth_url), json,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        // Valid response, do nothing
                        if(response.get("response").equals("valid")){

                        }
                        // Switch edurti fragment with the black fragment and
                        // show error alert dialog fragment
                        else{
                            Fragment fragment = (Fragment) edurtiBlackFragment.newInstance();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.flContent, fragment, "edurti_black_fragment")
                                    .commit();

                            DialogFragment errorFragment = new loginCheckErrorAlertDialogFragment();

                            errorFragment.show(getSupportFragmentManager(), "login_check_error_fragment");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }

            });

            requestQueue.add(json_obj_req);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;

    }

    // On options item selected display the MoreInfoAlertDialogFragment fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_more_info) {
            DialogFragment newFragment = new MoreInfoAlertDialogFragment();

            newFragment.show(getSupportFragmentManager(), "edurtimoreinfodialogfragment");

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
