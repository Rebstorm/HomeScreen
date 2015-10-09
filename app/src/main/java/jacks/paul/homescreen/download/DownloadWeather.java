package jacks.paul.homescreen.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by Paul on 2015-10-08.
 */
public class DownloadWeather extends AsyncTask<String, Void, String> {


    public String xmlData = "";

    public DownloadInterface delegate = null;



    @Override
    protected String doInBackground(String... urls) {

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            // Reading the XML

            String rLine = null;
            while((rLine = reader.readLine()) != null){
                xmlData += rLine;
            }


            delegate.processFinished(xmlData);

            return xmlData;

                // BufferedReader doesnt have to be closed, right?
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();


        }

        return xmlData;
    }

    @Override
    protected void onPostExecute(String result) {

    }




}


