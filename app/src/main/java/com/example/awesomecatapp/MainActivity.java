package com.example.awesomecatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


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

                

                //Set Text on button click via this function.
                newText.setText("Text Change successfully");

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

