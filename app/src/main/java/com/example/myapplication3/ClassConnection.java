package com.example.myapplication3;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ClassConnection extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection httpURLConnection = null;
        URL url=null;
        try {
            url = new URL(strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            //if(code==HttpURLConnection.HTTP_OK){

            //}
            return Integer.toString(code);



        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }




    /*public static void main(String[] args) throws Exception {




        ClassConnection http = new ClassConnection();

        //System.out.println("GET Request Using HttpURLConnection");
        //http.sendGet();
        //System.out.println();
        //System.out.println("POST Request Using HttpURLConnection");
        //http.sendPost();
}

    // HTTP GET request
    private void sendGet() throws Exception {

        //String username="hitenpratap";
        StringBuilder stringBuilder = new StringBuilder("http://192.168.4.1:8266");
        stringBuilder.append("?num1=15");
        //stringBuilder.append(URLEncoder.encode(username, "UTF-8"));

        URL obj = new URL(stringBuilder.toString());

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        //con.setRequestProperty("Accept-Charset", "UTF-8");

        //System.out.println("\nSending request to URL : " + url);
        //System.out.println("Response Code : " + con.getResponseCode());
        //System.out.println("Response Message : " + con.getResponseMessage());


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        //System.out.println(response.toString());

    }*/
}