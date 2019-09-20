package com.example.myapplication1;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DB extends AsyncTask<String, Boolean, JSONObject> {

    Boolean finish;

    private URLConnection setUrlCon(String params) {
        URL myUrl = null;
        try {
            myUrl = new URL("https://resenddb2.000webhostapp.com/" + params);
        } catch (
                MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(myUrl);
        URLConnection myUrlCon = null;
        try {
            myUrlCon = myUrl.openConnection();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return (myUrlCon);
    }

    protected JSONObject getJson(String urlParams) {
                URLConnection urlCon = setUrlCon(urlParams);
                JSONObject jsonObject = null;
                InputStream inputStream = null;
                String result = null;
                try {

                    inputStream = urlCon.getInputStream();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    jsonObject = new JSONObject(result);
                    System.out.println("Прочитанно - " + result);
                    System.out.println("json - " + jsonObject.toString());
                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return (jsonObject);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        finish = false;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        return getJson(strings[0]);
    }

//    @Override
//    protected void onProgressUpdate(Boolean... values) {
//        super.onProgressUpdate(values);
//    }

    @Override
    protected void onPostExecute(JSONObject object) {
        super.onPostExecute(object);
        finish = true;
    }
}

