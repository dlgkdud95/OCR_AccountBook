package com.example.accountbook_uiux;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TravelViewFragment extends Fragment {

    private View view_travel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_travel = inflater.inflate(R.layout.frame_travel, container, false);
        return view_travel;
    }
}