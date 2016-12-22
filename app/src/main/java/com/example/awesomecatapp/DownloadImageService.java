package com.example.awesomecatapp;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.CharStreams;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DownloadImageService extends IntentService {

    public static final String ACTION_DOWNLOAD = "com.example.awesomecatapp.action.DOWNLOAD";
    //public static final String ACTION_BAZ = "com.example.dara.myapplication.action.BAZ";

    public static final String EXTRA_APIURL = "com.example.awesomecatapp.extra.APIURL";
    //public static final String EXTRA_MESSAGE = "com.example.awesomecatapp.extra.message";

    public Bitmap img = null;



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadImageService() {

        super("DownloadImageService");
    }


    /**
     *
     * @param intent Intent to work on
     */
    @Override
    protected void onHandleIntent (Intent intent) {

        String apiUrl = intent.getDataString();
        img = downloadImage(apiUrl);

        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.putExtra("image", img);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backIntent);

    }


    private Bitmap downloadImage(String apiUrl) {

        Bitmap img = null;
        String imgUrl = null;
        InputStream in = null;

        try {

            in = new java.net.URL(apiUrl).openStream();
            String xmlString = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
            imgUrl = parseImgUrl(xmlString);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {

            in = new java.net.URL(imgUrl).openStream();
            img = BitmapFactory.decodeStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return img;

    }


    /**
     * Parses the image URL out of the XML string
     * @param xmlString XML string that contains URL to the image
     * @return URL to the image
     */
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


            NodeList nList = null;
            if (doc != null) {
                doc.getDocumentElement().normalize();
                nList = doc.getElementsByTagName("image");
            }



            System.out.println("----------------------------");

            if (nList != null) {
                for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;

                        imageUrl = eElement.getElementsByTagName("url").item(0).getTextContent();
                        System.out.println("Image URL: " + imageUrl);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageUrl;
    }

}


