package com.example.solierboix.recyclerviewtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlickrManager extends AsyncTask<String, Void, String[]>{

    public AsyncResponse delegete = null;
    private Context context;
    public FlickrManager(Context context){
        this.context = context;
    }
    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.show();
        dialog.setMessage("Loading");
        dialog.setCancelable(false);

    }

    @Override
    protected String[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;

        try {
            final String BASE_URL = params[0];

            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                jsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                jsonStr = null;
            }
            jsonStr = buffer.toString();
            System.out.println(jsonStr);
        } catch (IOException e) {
            android.util.Log.e("DOWNLOAD", "Error ", e);
            jsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    android.util.Log.e("DOWNLOAD", "Error closing stream", e);
                }
            }
        }
        String[] rlt = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("photos");

            JSONArray jsonArray = (JSONArray) jsonObject.get("photo");
            rlt = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int farm = obj.getInt("farm");
                String server = obj.getString("server");
                String id = obj.getString("id");
                String secret = obj.getString("secret");
                rlt[i] = "http://farm"+farm+".static.flickr.com/"+server+"/"+id+"_"+secret+".jpg";
                System.out.println(rlt[i]);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return rlt;

    }

    @Override
    protected void onPostExecute(String[] rtl) {
        dialog.dismiss();
        String finalImage = rtl[0];
        delegete.processFinish(finalImage);
    }

}
