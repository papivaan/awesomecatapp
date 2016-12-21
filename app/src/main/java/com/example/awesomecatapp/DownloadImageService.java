package com.example.awesomecatapp;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.test.espresso.core.deps.guava.io.CharStreams;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DownloadImageService extends IntentService {

    public static final String ACTION_DOWNLOAD = "com.example.awesomecatapp.action.DOWNLOAD";
    //public static final String ACTION_BAZ = "com.example.dara.myapplication.action.BAZ";

    public static final String EXTRA_APIURL = "com.example.awesomecatapp.extra.APIURL";
    //public static final String EXTRA_MESSAGE = "com.example.awesomecatapp.extra.message";

    public static Bitmap img = null;

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

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String apiUrl = intent.getStringExtra(EXTRA_APIURL);
                Log.e("Service", apiUrl);
                String imgUrl = downloadXml(apiUrl);
                downloadImage(imgUrl);
            }
        }
    }


    /**
     * Connects to the cat pic API and parses the image url from the XML
     * @param apiUrl URL of the API to be used
     * @return URL of the image
     */
    private String downloadXml(String apiUrl) {

        String xmlString = null;

        try {
            InputStream in = new java.net.URL(apiUrl).openStream();
            xmlString = CharStreams.toString(new InputStreamReader(in, "UTF-8"));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        String imgUrl = parseImgUrl(xmlString);
        return imgUrl;
    }



    private void downloadImage(String url) {


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
