package com.example.kuleumbridge.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.kuleumbridge.R;


public class gradeCheckFragment extends Fragment {

    public gradeCheckFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grade_check_layout, container, false);
    }
}
