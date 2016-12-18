package com.example.awesomecatapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            FactFragment firstFragment = new FactFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    public void showFact(View view){
        // TODO
        System.out.println("Fact button pressed");
/*
        FragmentManager fm = getFragmentManager();
        FactFragment f = (FactFragment) fm.findFragmentById(FactFragment.TAG);
        if (f == null) {
            f = new FactFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, f, FactFragment.TAG)
                    //.addToBackStack(null);  // uncomment this line if you want to be able to return to the prev. fragment with "back" button
                    .commit();
        }
        */
    }

    public void showPic(View view){
        // TODO
        System.out.println("Pic button pressed");
    }

    public void showGif(View view){
        // TODO
        System.out.println("Gif button pressed");
    }
}

