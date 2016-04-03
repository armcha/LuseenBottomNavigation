package com.luseen.myapplication;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;
    ViewPager pager;
    SimpleTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] image = {R.drawable.ic_mic_black_24dp, R.drawable.ic_favorite_black_24dp,
                R.drawable.ic_book_black_24dp, R.drawable.github_circle};
        int[] color = {ContextCompat.getColor(this, R.color.firstColor), ContextCompat.getColor(this, R.color.secondColor),
                ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.fourthColor)};
        adapter = new SimpleTabAdapter(getSupportFragmentManager());
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        pager = (ViewPager) findViewById(R.id.viewPager);
        /*if you use tablet mode uncomment this*/
        /*RelativeLayout.LayoutParams pagerParams = (RelativeLayout.LayoutParams)pager.getLayoutParams();
        pagerParams.setMargins(BottomNavigationUtils.getActionbarSize(this),pagerParams.topMargin,
                pagerParams.rightMargin,pagerParams.bottomMargin);
        pager.setLayoutParams(pagerParams);*/
        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(false);
            //bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(true);
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.firstColor));
        }
        pager.setAdapter(adapter);
        bottomNavigationView.setUpWithViewPager(pager, color, image);
    }
}

