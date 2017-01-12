package com.example.awesomecatapp;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


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

        // Set text
        Bundle bundle = getArguments();
        if (bundle != null) {
            Bitmap img = (Bitmap) bundle.get("img");
            ImageView iv = (ImageView) getView().findViewById(R.id.picView);

            iv.setImageBitmap(img);
            String imgUrl = bundle.getString("imgUrl");
            setImgText(imgUrl);
        }

    }


    public void setImgText(String imgUrl) {

        try {
            TextView t = (TextView) getView().findViewById(R.id.picUrl);
            String text = "Image source: " + imgUrl;
            t.setText(text);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}
