package com.example.awesomecatapp;

import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";
    String factUrl = "http://catfacts-api.appspot.com/api/facts?number=1";

    Fragment fr;
    FragmentManager fm;
    FragmentTransaction ft;

    public String factText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showWelcomeMessage(savedInstanceState);

        setListeners();

    }

    private void setListeners() {
        Button factButton = (Button) findViewById(R.id.factButton);
        factButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fr = new FactFragment();


                // TODO
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                    System.out.println("Fetching data...");
                    new DownloadWebpageTask().execute(factUrl);
                } else {
                    // display error
                    System.out.println("Ooijjojoi, en virhe...");
                }

                fm = getFragmentManager();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();

                System.out.println("Tämä tässä on tallennettu fakta: " + factText);

            }
        });

        Button picButton = (Button) findViewById(R.id.picButton);
        picButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fr = new PicFragment();

                fm = getFragmentManager();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();

            }
        });

        Button gifButton = (Button) findViewById(R.id.gifButton);
        gifButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fr = new GifFragment();

                fm = getFragmentManager();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();

            }
        });
    }

    private void showWelcomeMessage(Bundle savedInstanceState) {

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            WelcomeFragment firstFragment = new WelcomeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }




    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            System.out.println("The result is: " + result);
            //TODO
            //setFactText(result);
            factText = result;
        }


        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }


        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

    }

}

