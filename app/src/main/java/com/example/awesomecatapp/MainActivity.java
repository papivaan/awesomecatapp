package com.example.awesomecatapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    Fragment fr;
    FragmentManager fm;
    FragmentTransaction ft;

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

                fm = getFragmentManager();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();

                // TODO
                System.out.println("Going in, cover me...");
                try {
                    System.out.println("Sending get from fact button");
                    getFact();
                } catch (Exception ex){
                    System.out.println("Oijjojoi, en virhe!");
                }

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

    private void getFact() throws Exception {
        String url = "http://catfacts-api.appspot.com/api/facts?number=1";

        System.out.println(url);
    }

}

