package com.KonDuckJoa.kuleumbridge.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.KonDuckJoa.kuleumbridge.R;


public class tastePlaceFragment extends Fragment {

    public tastePlaceFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taste_place_layout,container,false);
        return view;
    }
}
