package com.example.awesomecatapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.CharStreams;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Iterator;


public class DownloadFactService extends IntentService {

	public String fact = null;


	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 */
	public DownloadFactService() {

		super("DownloadFactService");
	}


	/**
	 *
	 * @param intent Intent to work on
	 */
	@Override
	protected void onHandleIntent (Intent intent) {

		String apiUrl = intent.getDataString();
		fact = downloadFact(apiUrl);

		MainActivity.setFact(fact);

	}


	private String downloadFact(String apiUrl) {

		InputStream in = null;

		try {

			in = new java.net.URL(apiUrl).openStream();
			String jsonString = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
			fact = parseFact(jsonString);

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

		return fact;

	}


	/**
	 * Parses the cat fact out of a json string
	 *
	 * @param jsonString String that needs parsing
	 * @return Parsed fact
	 */
	private String parseFact(String jsonString) {

		String parsedResult = "";

		// Create new JSON parser
		JSONParser parser = new JSONParser();

		try {

			// Create an object from string
			Object obj;
			System.out.println(jsonString);
			obj = parser.parse(jsonString);

			// Create a JSON object from object
			JSONObject jsonObject = (JSONObject) obj;

			// Create a JSON array that has facts in it
			JSONArray fact = (JSONArray) jsonObject.get("facts");

			// Iterate over the facts
			Iterator<String> iterator = fact.iterator();
			// Store the fact into the result
			while (iterator.hasNext()) {
				parsedResult = iterator.next();
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return parsedResult;
	}


}


