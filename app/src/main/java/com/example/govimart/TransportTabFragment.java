package com.example.govimart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TransportTabFragment extends Fragment {
    private static final String TAG = "TransportFragment";

    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.transport_fragment_layout,container,false);
        //View view = inflater.inflate(R.layout.categories_fragment_layout,container,false);
        //btnTEST = (Button) view.findViewById(R.id.btnTEST);

        //btnTEST.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
        //    }
        //});

        //return view;
    }
}



