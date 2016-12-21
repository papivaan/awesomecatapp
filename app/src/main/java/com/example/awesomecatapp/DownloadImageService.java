package com.example.awesomecatapp;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;


public class DownloadImageService extends IntentService {

    public static final String ACTION_DOWNLOAD = "com.example.awesomecatapp.action.DOWNLOAD";
    //public static final String ACTION_BAZ = "com.example.dara.myapplication.action.BAZ";

    public static final String EXTRA_URL = "com.example.awesomecatapp.extra.URL";
    public static final String EXTRA_MESSAGE = "com.example.awesomecatapp.extra.message";

    public static final Bitmap IMAGE = null;

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
                final String url = intent.getStringExtra(EXTRA_URL);
                Log.e("Service", url);
                downloadImage(url);
            }
        }
    }



    private void downloadImage(String url) {


    }
}
