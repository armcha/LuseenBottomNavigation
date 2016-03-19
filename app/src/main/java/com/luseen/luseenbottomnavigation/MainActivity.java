package com.luseen.luseenbottomnavigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    BottomNavigation bottomNavigation;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView)findViewById(R.id.textView);
        bottomNavigation = (BottomNavigation)findViewById(R.id.bottomNavigation);

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("First",getResources().getColor(R.color.firstColor),R.drawable.ic_mic_black_24dp);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("second",getResources().getColor(R.color.secondColor),R.drawable.ic_favorite_black_24dp);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("Third",getResources().getColor(R.color.thirdColor),R.drawable.ic_book_black_24dp);

        bottomNavigation.addItem(bottomNavigationItem);
        bottomNavigation.addItem(bottomNavigationItem1);
        bottomNavigation.addItem(bottomNavigationItem2);

        bottomNavigation.setOnBottomNavigationItemClickListener(new BottomNavigation.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                t.setText(index+"");
            }
        });
    }
}
