package com.example.awesomecatapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class FactFragment extends Fragment {

    TextView factTextView;


    public FactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fact, container, false);
    }


    public void setText(String text) {
        TextView t = (TextView) getView().findViewById(R.id.factTextView);
        t.setText(text);
    }

}
