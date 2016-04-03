package com.luseen.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    TextView t;
    Button b;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        t = (TextView) rootView.findViewById(R.id.textView);
        b = (Button) rootView.findViewById(R.id.button);
        ((MainActivity) getActivity()).bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index) {
                    case 0:
                        t.setText("Record");
                        break;
                    case 1:
                        t.setText("Like");
                        break;
                    case 2:
                        t.setText("Books");
                        break;
                    case 3:
                        t.setText("Github");
                        break;
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).bottomNavigationView.selectTab(2);
            }
        });
        return rootView;
    }

}
