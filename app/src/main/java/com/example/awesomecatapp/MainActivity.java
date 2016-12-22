package com.example.awesomecatapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends FragmentActivity {

    private Context context;

    // API source: https://catfacts-api.appspot.com
    String factUrl = "http://catfacts-api.appspot.com/api/facts?number=1";

    // API source: http://thecatapi.com
    String imageApiUrl = "http://thecatapi.com/api/images/get?format=xml&results_per_page=1";

    Intent downloadImageIntent;

    // Variable for storing current random fact
    public String factText;

    static Bitmap catImage;


    Fragment fr;
    FragmentManager fm;
    FragmentTransaction ft;


    /**
     * Shows welcome message and sets click listeners when activity is created
     *
     * @param savedInstanceState Saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //TODO: Appin nimi ei näy. Jos sen haluaa näkyviin, pitänee luoda sille oma fragment

        showWelcomeMessage(savedInstanceState);

        setListeners();

    }


    /**
     * Sets listeners for Fact, Pic and Gif buttons
     */
    private void setListeners() {

        /*
         * Button for getting the cat fact
         */
        Button factButton = (Button) findViewById(R.id.factButton);

        /*
         * Sets click listener on fact button
         */
        factButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Creates a connection and fetches the cat fact from the API URL.
             * Sets the factText variable
             *
             * @param v View where the button is clicked
             */
            @Override
            public void onClick(View v) {

                // Create new fact fragment
                fr = new FactFragment();

                String result = "";

                // Check if Internet connection is available
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a connection, send GET request
                if (networkInfo != null && networkInfo.isConnected()) {

                    // fetch data
                    AsyncTask<String, Void, String> dwt = new DownloadWebpageTask().execute(factUrl);

                    try {

                        // Store the result of the request
                        result = dwt.get();

                        // Find the index of last curly bracket
                        int lastIndex = result.lastIndexOf("}");
                        // Store text only until last curly bracket, ie. get rid of the funny characters at the end
                        result = StringUtils.left(result, lastIndex + 1);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    // Parse the json result into a single string
                    result = parseFact(result);

                    // Store the result as factText
                    factText = result;

                    // configure factText
                    Bundle bundle = new Bundle();
                    bundle.putString("text", factText);
                    fr.setArguments(bundle);


                } else {
                    // display error
                    System.out.println("Error! Something wrong with the network...");
                }

                // Change the fragment in the container
                fm = getFragmentManager();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();


            }
        });

        /*
         * Button for getting a cat picture
         */
        Button picButton = (Button) findViewById(R.id.picButton);

        /*
         * Sets click listener on the pic button
         */
        picButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Create a connection and fetch a new cat picture
             *
             * @param v View where the button was clicked
             */
            @Override
            public void onClick(View v) {

                fr = new PicFragment();

                // Check if Internet connection is available
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a connection, send GET request
                if (networkInfo != null && networkInfo.isConnected()) {


                    /*
                     * Creates a new Intent to start the DownloadImageService
                     * IntentService. Passes a URI in the
                     * Intent's "data" field.
                     */
                    Intent downloadImageIntent = new Intent(context, DownloadImageService.class);
                    downloadImageIntent.setData(Uri.parse(imageApiUrl));

                    // Starts the IntentService
                    context.startService(downloadImageIntent);

                    // configure image url
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("img", catImage);
                    bundle.putString("apiUrl", imageApiUrl);
                    fr.setArguments(bundle);

                    // Change the fragment in the container
                    fm = getFragmentManager();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fr);
                    ft.commit();

                }
                else {
                    // display error
                    System.out.println("Error! Something wrong with the network...");
                }

            }
        });


        /**
         * Button for getting a cat gif
         */
        Button gifButton = (Button) findViewById(R.id.gifButton);

        /**
         * Sets click listener on the button
         */
        gifButton.setOnClickListener(new View.OnClickListener() {

            /**
             *
             * @param v View where the button was clicked
             */
            @Override
            public void onClick(View v) {

                fr = new GifFragment();

                //TODO: Hae gif

                // Change the fragment in the container
                // TODO:
                fm = getFragmentManager();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();

            }
        });
    }


    /**
     * Parses the cat fact out of a json string
     *
     * @param jsonString String that needs parsing
     * @return Parsed fact
     */
    private String parseFact(String jsonString) {

        String parsedResult = "";

        // Create new JSON parser
        JSONParser parser = new JSONParser();

        try {

            // Create an object from string
            Object obj;
            obj = parser.parse(jsonString);

            // Create a JSON object from object
            JSONObject jsonObject = (JSONObject) obj;

            // Create a JSON array that has facts in it
            JSONArray fact = (JSONArray) jsonObject.get("facts");

            // Iterate over the facts
            Iterator<String> iterator = fact.iterator();
            // Store the fact into the result
            while (iterator.hasNext()) {
                parsedResult = iterator.next();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedResult;
    }


    /**
     * Fills the fragment container with a welcome message
     *
     * @param savedInstanceState Saved state
     */
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

    public static void setImage(Bitmap image) {
        catImage = image;
    }


}
