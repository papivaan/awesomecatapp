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
import android.widget.ImageView;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;


public class MainActivity extends FragmentActivity {

    private Context context;

    // API source: https://catfacts-api.appspot.com
    String factUrl = "http://catfacts-api.appspot.com/api/facts?number=1";

    // API source: http://thecatapi.com
    String imageApiUrl = "http://thecatapi.com/api/images/get?format=xml&results_per_page=1";

	// API source: http://thecatapi.com
	String gifApiUrl = "http://thecatapi.com/api/images/get?format=xml&type=gif";

    // Variable for storing current random fact
    static String factText;

    // Cat image
    static Bitmap catImage;

	static String catGif;


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

                // Check if Internet connection is available
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a connection, send GET request
                if (networkInfo != null && networkInfo.isConnected()) {

					Intent downloadFactIntent = new Intent(context, DownloadFactService.class);
					downloadFactIntent.setData(Uri.parse(factUrl));

					context.startService(downloadFactIntent);

                    // configure factText
                    Bundle bundle = new Bundle();
                    bundle.putString("text", factText);
					fr.setArguments(bundle);

					changeFragment(fr);

                } else {
                    // display error
                    System.out.println("Error! Something wrong with the network...");
                }


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

					/*
                	 * TODO:
                	 * Jostain syystä ekalla kerralla kun klikkaa kuvaa, niin ei tule mitään
                	 * ja sen jälkeen tulee "pykälän myöhässä"
                	 */

                    // configure image url
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("img", catImage);
                    bundle.putString("apiUrl", imageApiUrl);
                    fr.setArguments(bundle);

					changeFragment(fr);

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

				// Check if Internet connection is available
				ConnectivityManager connMgr = (ConnectivityManager)
						getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

				// If there is a connection, send GET request
				if (networkInfo != null && networkInfo.isConnected()) {

					Intent downloadGifIntent = new Intent(context, DownloadGifService.class);
					downloadGifIntent.setData(Uri.parse(gifApiUrl));

					context.startService(downloadGifIntent);

					Bundle bundle = new Bundle();
					bundle.putString("url", catGif);
					fr.setArguments(bundle);

					changeFragment(fr);

				}
				else {
					// display error
					System.out.println("Error! Something wrong with the network...");
				}

            }
        });
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


	private void changeFragment(Fragment fr) {
		// Change the fragment in the container
		fm = getFragmentManager();
		ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, fr);
		ft.commit();
	}


    public static void setImage(Bitmap image) {
        catImage = image;
    }


	public static void setFact(String catFact) {
		factText = catFact;
	}

	public static void setGifUrl(String gifUrl) {
		catGif = gifUrl;
	}



}
