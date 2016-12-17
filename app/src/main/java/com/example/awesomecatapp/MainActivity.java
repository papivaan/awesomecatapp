package com.example.awesomecatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.awesomecatapp.MESSAGE";

    ArticleFragment articleFragment = new ArticleFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * Called when user clicks the "Send" button
     * @param view View that was clicked
     */
    public void sendMessage(View view) {
        /*
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        */
    }


    /** Shows a random cat fact */
    public void showFact(View view) {
        Intent intent = new Intent(this, ShowFactFragment.class);
        String testMessage = "Here you go, this should be a cat fact!";
        intent.putExtra(EXTRA_MESSAGE, testMessage);
        //TODO: Jatka tätä, en tiedä mitä pitäs tehhä täsä
        
    }


    /** Shows a random cat picture */
    public void showPic(View view) {

    }


    /** Shows a random cat gif */
    public void showGif(View view) {

    }
}
