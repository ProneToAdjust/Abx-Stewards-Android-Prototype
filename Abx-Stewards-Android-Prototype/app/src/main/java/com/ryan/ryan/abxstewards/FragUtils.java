package com.ryan.ryan.abxstewards;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

public class FragUtils {
    private Context fragment_context;

    public FragUtils(Context context) {
        this.fragment_context = context;

    }

    public Context getFragment_context() {
        return fragment_context;

    }

    public void setFragment_context(Context context) {
        this.fragment_context = context;

    }

    // Make a volley post request to the url parameter with the json parameter as the payload
    public void postData(String url, JSONObject json) {
        RequestQueue requestQueue = Volley.newRequestQueue(getFragment_context());

        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        requestQueue.add(json_obj_req);

    }

    // Write the jsonArray parameter to a file that matches the json_file_name parameter
    public void writeJSONArray(String json_file_name, JSONArray jsonArray) {
        try {
            Writer output = null;

            File file = new File(fragment_context.getFilesDir(), json_file_name);

            output = new BufferedWriter(new FileWriter(file));
            output.write(jsonArray.toString());
            output.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

}

    // Returns the json array read from the file that matches the json_file_name parameter
    public JSONArray readJSONArray(String json_file_name) {
        String text = "";
        JSONArray jsonArray = new JSONArray();

        try {
            File file = new File(fragment_context.getFilesDir(), json_file_name);

            InputStream inputStream = new FileInputStream(file);
            StringBuilder stringBuilder = new StringBuilder();

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);

                }

                inputStream.close();
                text = stringBuilder.toString();

                jsonArray = new JSONArray(text);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;

    }

    // Clears the json file whose name matches the json_file_name parameter
    public void clearJSONArray(String json_file_name) {
        JSONArray jsonArray = new JSONArray();

        writeJSONArray(json_file_name, jsonArray);

    }

    // Writes the credentialsJSON to the credentials json file
    public void writeCredentialsJSON(JSONObject credentialsJSON) {
        try {
            Writer output = null;

            File file = new File(fragment_context.getFilesDir(), "credentials.json");

            output = new BufferedWriter(new FileWriter(file));
            output.write(credentialsJSON.toString());
            output.close();

        } catch (Exception e) {
            Toast.makeText(fragment_context, e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }


    // Reads the credentials json file and returns the credentials json object
    public JSONObject readCredentialsJSON() {
        String text;
        JSONObject jsonObject = new JSONObject();

        try {
            File file = new File(fragment_context.getFilesDir(), "credentials.json");

            InputStream inputStream = new FileInputStream(file);
            StringBuilder stringBuilder = new StringBuilder();

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);

                }

                inputStream.close();
                text = stringBuilder.toString();

                jsonObject = new JSONObject(text);

            }
            else{
                jsonObject = new JSONObject();
                jsonObject.put("username", "none");
                jsonObject.put("password", "none");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    // Returns a boolean to indicate whether the credentials json file contains any stored credentials
    public boolean hasCredentials() throws JSONException{
        boolean hasCreds;
        JSONObject jsonObject = readCredentialsJSON();
        String jsonUsername = jsonObject.get("username").toString();

        if(!jsonUsername.equals("none")){
            hasCreds = true;

        }
        else{
            hasCreds = false;

        }

        return hasCreds;
    }

    public void clearCredentialsJSON() {
        JSONObject jsonObject = new JSONObject();

        writeCredentialsJSON(jsonObject);

    }

    public String getUUID() {
        TelephonyManager tManager = (TelephonyManager) fragment_context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(fragment_context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "No Permission";
        }
        String uuid = tManager.getDeviceId();

        return uuid;
    }

    // Returns the current time stamp in the format of "YYYY-MM-DD hh-mm-ss"
    public String getTimeStamp(){
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        String hour = Integer.toString(currentTime.getHours());
        String minute = Integer.toString(currentTime.getMinutes());
        String second = Integer.toString(currentTime.getSeconds());

        String dateTime = year + "-" + month + "-" + day + " " +
                hour + ":" + minute + ":" + second;

        return dateTime;
    }

    public int getMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);

    }

    // Returns the data from the last entry the user made
    public JSONObject getPreviousEntryRecord(){
        JSONObject prev_entry_record = new JSONObject();
        String text;

        try {
            File file = new File(fragment_context.getFilesDir(), "previousRecordEntry.json");

            InputStream inputStream = new FileInputStream(file);
            StringBuilder stringBuilder = new StringBuilder();

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);

                }

                inputStream.close();
                text = stringBuilder.toString();

                prev_entry_record = new JSONObject(text);

            }
            else{
                prev_entry_record = new JSONObject();
                prev_entry_record.put("previous_entry_record", "none");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

            prev_entry_record = new JSONObject();
            try {
                prev_entry_record.put("previous_entry_record", "none");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return prev_entry_record;
    }

    // Writes json from the prev_entry_record parameter to a json file
    public void setPreviousEntryRecord(JSONObject prev_entry_record){
        try {
            Writer output = null;

            File file = new File(fragment_context.getFilesDir(), "previousRecordEntry.json");

            output = new BufferedWriter(new FileWriter(file));
            output.write(prev_entry_record.toString());
            output.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
