package com.luseen.luseenbottomnavigation;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chatikyan on 18.03.2016.
 */
public class BottomNavigation extends FrameLayout {

    private OnBottomNavigationItemClickListener onBottomNavigationItemClickListener;
    private Context context;
    private final int NAVIGATION_HEIGHT = (int) getResources().getDimension(R.dimen.bottom_navigation_height);
    private int itemWidth;
    private int itemHeight;
    private int currentItem = 0;
    private View backgroundColorTemp;
    //View view;

    private List<BottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();


    public BottomNavigation(Context context) {
        super(context);
        this.context = context;
    }


    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = NAVIGATION_HEIGHT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getResources().getDimension(R.dimen.bottom_navigation_elevation));
        }
        setLayoutParams(params);
        Log.e("onMeasure", "onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        backgroundColorTemp = new View(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LayoutParams backgroundLayoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(backgroundColorTemp, backgroundLayoutParams);
        }
        viewList.clear();
        itemWidth = getWidth() / bottomNavigationItems.size();
        itemHeight = LayoutParams.MATCH_PARENT;
        LinearLayout items = new LinearLayout(context);
        items.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT);
        addView(items, params);
        Log.e("onSizeChanged", "onSizeChanged");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < bottomNavigationItems.size(); i++) {
            final int index = i;
            final View view = inflater.inflate(R.layout.bottom_navigation, this, false);
            ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
            TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
            viewList.add(view);
            if (i == currentItem) {
                int textActivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_active);
                setBackgroundColor(bottomNavigationItems.get(index).getColor());
                view.setPadding(view.getPaddingLeft(), textActivePaddingTop, view.getPaddingRight(),
                        view.getPaddingBottom());
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentItem == i ?
                        context.getResources().getDimension(R.dimen.bottom_navigation_text_size_active) :
                        context.getResources().getDimension(R.dimen.bottom_navigation_text_size_inactive));
                title.setTextColor(currentItem == i ?
                        getResources().getColor(R.color.colorActive) :
                        getResources().getColor(R.color.colorInactive));
            }
            icon.setImageResource(bottomNavigationItems.get(i).getImageResource());
            icon.setColorFilter(ContextCompat.getColor(context, i == 0 ? R.color.colorActive : R.color.colorInactive));
            title.setText(bottomNavigationItems.get(i).getTitle());
            LayoutParams itemParams = new LayoutParams(itemWidth, itemHeight);
            items.addView(view, itemParams);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBottomNavigationItemClick(index);
                }
            });
        }

    }

    public void addItem(BottomNavigationItem item) {
        bottomNavigationItems.add(item);
    }

    private void onBottomNavigationItemClick(final int itemIndex) {
        if (currentItem == itemIndex) {
            return;
        }

        int viewActivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_active);
        int viewInactivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive);
        float textActiveSize = context.getResources().getDimension(R.dimen.bottom_navigation_text_size_active);
        float textInactiveSize = context.getResources().getDimension(R.dimen.bottom_navigation_text_size_inactive);
        int itemActiveColor = ContextCompat.getColor(context, R.color.colorActive);
        int itemInactiveColor = ContextCompat.getColor(context, R.color.colorInactive);
        for (int i = 0; i < viewList.size(); i++) {
            if (i == itemIndex) {
                View view = viewList.get(itemIndex).findViewById(R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.changeTextColor(title, itemInactiveColor, itemActiveColor);
                BottomNavigationUtils.changeTextSize(title, textInactiveSize, textActiveSize);
                BottomNavigationUtils.imageColorChange(icon, itemInactiveColor, itemActiveColor);
                BottomNavigationUtils.changeTopPadding(view, viewInactivePaddingTop, viewActivePaddingTop);
                int centerX = (int) viewList.get(itemIndex).getX() + viewList.get(itemIndex).getWidth() / 2;
                int centerY = viewList.get(itemIndex).getHeight() / 2;
                int finalRadius = Math.max(getWidth(), getHeight());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    backgroundColorTemp.setBackgroundColor(bottomNavigationItems.get(itemIndex).getColor());
                    Animator changeBackgroundColor = ViewAnimationUtils.createCircularReveal(backgroundColorTemp, centerX, centerY, 0, finalRadius);
                    changeBackgroundColor.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            setBackgroundColor(bottomNavigationItems.get(itemIndex).getColor());
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    changeBackgroundColor.start();
                } else {
                    BottomNavigationUtils.backgroundColorChange
                            (this,bottomNavigationItems.get(currentItem).getColor(),bottomNavigationItems.get(itemIndex).getColor());
                }
            } else if (i == currentItem) {
                View view = viewList.get(i).findViewById(R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.imageColorChange(icon, itemActiveColor, itemInactiveColor);
                BottomNavigationUtils.changeTopPadding(view, viewActivePaddingTop, viewInactivePaddingTop);
                BottomNavigationUtils.changeTextColor(title, itemActiveColor, itemInactiveColor);
                BottomNavigationUtils.changeTextSize(title, textActiveSize, textInactiveSize);
            }
        }

        if (onBottomNavigationItemClickListener != null)
            onBottomNavigationItemClickListener.onNavigationItemClick(itemIndex);
        currentItem = itemIndex;
    }


    //setup interface for item onClick

    public interface OnBottomNavigationItemClickListener {
        void onNavigationItemClick(int index);
    }

    public void setOnBottomNavigationItemClickListener(OnBottomNavigationItemClickListener onBottomNavigationItemClickListener) {
        this.onBottomNavigationItemClickListener = onBottomNavigationItemClickListener;
    }
}
