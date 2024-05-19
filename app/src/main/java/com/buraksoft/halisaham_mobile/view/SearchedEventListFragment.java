package com.buraksoft.halisaham_mobile.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksoft.halisaham_mobile.R;


public class SearchedEventListFragment extends Fragment {

    public SearchedEventListFragment() {
        // Required empty public constructor
    }

    public static SearchedEventListFragment newInstance() {
        return new SearchedEventListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searched_event_list, container, false);
    }
}