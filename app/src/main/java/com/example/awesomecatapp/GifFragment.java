package com.example.awesomecatapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class GifFragment extends Fragment {


    public GifFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gif, container, false);
    }


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Set text
		Bundle bundle = getArguments();
		if (bundle != null) {
			setGif(bundle.getString("url"));
		}
	}

	public void setGif(String url) {
		ImageView iv = (ImageView) getView().findViewById(R.id.imageViewGif);
		Glide
				.with(this)
				.load(url)
				.asGif()
				.error(R.mipmap.kisse)
				.into(iv);
	}

}
