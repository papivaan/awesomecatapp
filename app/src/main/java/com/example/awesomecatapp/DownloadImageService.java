package com.example.awesomecatapp;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by rennehir on 21/12/16.
 */

public class DownloadImageService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadImageService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent (Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

        // Do work here, based on the contents of dataString

    }
}
