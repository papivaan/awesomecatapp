package com.example.awesomecatapp;

import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.base.Utf8;
import android.support.test.espresso.core.deps.guava.io.CharStreams;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadGifService extends IntentService {


	public DownloadGifService() {

		super("DownloadGifService");
	}



	@Override
	protected void onHandleIntent(Intent intent) {

		String apiUrl = intent.getDataString();
		String gifUrl = downloadGif(apiUrl);

		MainActivity.setGifUrl(gifUrl);

	}

	private String downloadGif(String apiUrl) {

		String gifUrl = null;
		InputStream in = null;

		try {

			in = new java.net.URL(apiUrl).openStream();
			String xmlString = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
			gifUrl = parseGifUrl(xmlString);

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

		return gifUrl;
	}


	/**
	 * Parses the image URL out of the XML string
	 * @param xmlString XML string that contains URL to the image
	 * @return URL to the image
	 */
	private String parseGifUrl(String xmlString) {

		String gifUrl = "";

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

						gifUrl = eElement.getElementsByTagName("url").item(0).getTextContent();
						System.out.println("Gif URL: " + gifUrl);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gifUrl;
	}


}
