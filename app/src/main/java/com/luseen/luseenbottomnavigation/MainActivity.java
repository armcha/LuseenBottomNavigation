package com.luseen.luseenbottomnavigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigation;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity {

    BottomNavigation bottomNavigation;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.textView);
        bottomNavigation = (BottomNavigation) findViewById(R.id.bottomNavigation);
        if (bottomNavigation != null){
            bottomNavigation.isWithText(true);
            bottomNavigation.isColoredBackground(false);
        }
        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("Record", getResources().getColor(R.color.firstColor), R.drawable.ic_mic_black_24dp);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("Like", getResources().getColor(R.color.secondColor), R.drawable.ic_favorite_black_24dp);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("Books", getResources().getColor(R.color.thirdColor), R.drawable.ic_book_black_24dp);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("Github", getResources().getColor(R.color.fourthColor), R.drawable.github_circle);


        bottomNavigation.addTab(bottomNavigationItem);
        bottomNavigation.addTab(bottomNavigationItem1);
        bottomNavigation.addTab(bottomNavigationItem2);
        bottomNavigation.addTab(bottomNavigationItem3);

        bottomNavigation.setOnBottomNavigationItemClickListener(new BottomNavigation.OnBottomNavigationItemClickListener() {
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
    }
}
