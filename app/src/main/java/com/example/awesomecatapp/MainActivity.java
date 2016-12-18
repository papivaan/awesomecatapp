package com.example.awesomecatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView newText;
    Button changeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newText = (TextView)findViewById(R.id.text_box);
        changeText = (Button)findViewById(R.id.factButton);

        changeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO: API-kutsu varmaan tähän
                String catFact = "Not initialized yet...";



                //Set Text on button click via this function.
                newText.setText(catFact);

            }
        });
    }




    /** Shows a random cat fact */
    public void showFact(View view) {
        String testMessage = "Here you go, this should be a cat fact!";

        //TODO: Jatka tätä, en tiedä mitä pitäs tehhä täsä


    }




    /** Shows a random cat picture */
    public void showPic(View view) {

    }


    /** Shows a random cat gif */
    public void showGif(View view) {

    }
}

