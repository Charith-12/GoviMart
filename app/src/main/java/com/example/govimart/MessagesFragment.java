package com.example.govimart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {
    private static final String TAG = "MessagesFragment";

    /*
    private static final String ARG_TEXT = "argText";
    private static final String ARG_NUMBER = "argNumber";

    private String text;
    private int number;

    // Get args when creation.. and creating upon those args
    public static ProfileFragment newInstance(String text, int number){
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //
        View view = inflater.inflate(R.layout.messages_layout,container,false);
        //

        /*
         *   Defining text views...(On fragment) by find view by id...
         *   TextView textView = view.findViewById(R.id.);
         *
         *   if (getArguments() != null){
         *       text = getArguments().getString(ARG_TEXT);
         *       number = getArguments().getInt(ARG_NUMBER);
         *   }
         *
         *   textView.setText(text + number);
         *
         * */

        //TODO: fetching messages from database and populating a List
        // RecyclerView code goes here

        return view;
    }

}