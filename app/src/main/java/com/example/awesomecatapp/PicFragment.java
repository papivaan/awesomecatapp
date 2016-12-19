package com.example.awesomecatapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class PicFragment extends Fragment {


    public PicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pic, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String imgUrl = "";

        // Set text
        Bundle bundle = getArguments();
        if (bundle != null) {
            imgUrl = bundle.getString("imgUrl");
            setImg(imgUrl);
        }

        // show The Image in a ImageView
        new DownloadImageTask((ImageView) findViewById(R.id.picView)).execute(imgUrl);

    }


    public void setImg(String imgUrl) {

        TextView t = (TextView) getView().findViewById(R.id.picUrl);
        t.setText(imgUrl);

        /*
        Bitmap bmpimg = getBitmapFromURL(imgUrl);
        ImageView iv = (ImageView) getView().findViewById(R.id.picView);
        iv.setImageBitmap(bmpimg);
        */
    }






}
