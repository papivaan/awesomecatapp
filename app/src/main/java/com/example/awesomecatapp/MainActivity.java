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
import android.view.View;
import android.widget.Button;


import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.StringReader;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";

    // URL for the CatFact API
    String factUrl = "http://catfacts-api.appspot.com/api/facts?number=1";
    String imageApiUrl = "http://thecatapi.com/api/images/get?format=xml&results_per_page=1";

    // Variable for storing current random fact
    public String factText;


    Fragment fr;
    FragmentManager fm;
    FragmentTransaction ft;


    /**
     * Shows welcome message and sets click listeners when activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showWelcomeMessage(savedInstanceState);

        setListeners();

    }


    /**
     * Sets listeners for Fact, Pic and Gif buttons
     */
    private void setListeners() {

        /**
         * Button for getting the cat fact
         */
        Button factButton = (Button) findViewById(R.id.factButton);

        /**
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

        /**
         * Button for getting a cat picture
         */
        Button picButton = (Button) findViewById(R.id.picButton);

        /**
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

                String imageUrl = "";

                //TODO: Hae kuveja

                // Check if Internet connection is available
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a connection, send GET request
                if (networkInfo != null && networkInfo.isConnected()) {

                    // fetch data
                    AsyncTask<String, Void, String> dwt = new DownloadWebpageTask().execute(imageApiUrl);

                    try {

                        // Store the result of the request
                        imageUrl = dwt.get();
                        System.out.println("URL of the image: " + imageUrl);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    imageUrl = parseImgUrl(imageUrl);

                    // configure image url
                    Bundle bundle = new Bundle();
                    bundle.putString("imgUrl", imageUrl);
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
            Object obj = parser.parse(jsonString);

            // Create a JSON object from object
            JSONObject jsonObject = (JSONObject) obj;

            // Create a JSON array that has facts in it
            JSONArray fact = (JSONArray) jsonObject.get("facts");

            // Iterate over the facts
            Iterator<String> iterator = fact.iterator();
            while (iterator.hasNext()) {
                // Store the fact into the result
                parsedResult = iterator.next();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedResult;
    }


    public String parseImgUrl(String xmlString) {

        String imageUrl = "";

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            Document doc = null;
            try {
                builder = factory.newDocumentBuilder();
                doc = builder.parse(new InputSource(new StringReader(xmlString)));
            } catch (Exception e) {
                e.printStackTrace();
            }


            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();


            NodeList nList = doc.getElementsByTagName("image");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    imageUrl = eElement.getElementsByTagName("url").item(0).getTextContent();
                    System.out.println("Image URL: " + imageUrl);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageUrl;
    }


    /**
     * Fills the fragment container with a welcome message
     *
     * @param savedInstanceState
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



}
