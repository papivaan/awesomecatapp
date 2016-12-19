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


import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;



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

                String result = "";

                // TODO
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    // fetch data
                    AsyncTask<String, Void, String> dwt = new DownloadWebpageTask().execute(factUrl);

                    try {
                        result = dwt.get();
                        int lastIndex = result.lastIndexOf("}");
                        result = StringUtils.left(result, lastIndex + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    result = parseFact(result);

                    factText = result;

                } else {
                    // display error
                    System.out.println("Ooijjojoi, en virhe...");
                }

                fm = getFragmentManager();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();



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

    private String parseFact(String result) {

        String parsedResult = "";
        JSONParser parser = new JSONParser();


        try {

            Object obj = parser.parse(result);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray fact = (JSONArray) jsonObject.get("facts");
            Iterator<String> iterator = fact.iterator();
            while (iterator.hasNext()) {
                parsedResult = iterator.next();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedResult;
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






}

