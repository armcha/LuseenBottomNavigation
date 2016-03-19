package com.luseen.luseenbottomnavigation;

/**
 * Created by Chatikyan on 18.03.2016.
 */
public class BottomNavigationItem {

    String title;
    int color;
    int imageResource;

    public BottomNavigationItem(String title, int color, int imageResource) {
        this.title = title;
        this.color = color;
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
