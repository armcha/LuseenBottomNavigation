package com.luseen.luseenbottomnavigation.BottomNavigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import com.luseen.luseenbottomnavigation.R;

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
    private boolean withText = true;
    private boolean coloredBackground = true;
    private int itemActiveColor;
    private int itemInactiveColor;

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
        if (coloredBackground) {
            itemActiveColor = ContextCompat.getColor(context, R.color.colorActive);
            itemInactiveColor = ContextCompat.getColor(context, R.color.colorInactive);
        } else {
            itemActiveColor = ContextCompat.getColor(context, R.color.colorAccent);
            itemInactiveColor = ContextCompat.getColor(context, R.color.withoutColoredBackground);
        }
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = NAVIGATION_HEIGHT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getResources().getDimension(R.dimen.bottom_navigation_elevation));
        }
        setLayoutParams(params);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int white = ContextCompat.getColor(context, R.color.white);
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < bottomNavigationItems.size(); i++) {
            final int index = i;
            if (!coloredBackground)
                bottomNavigationItems.get(i).setColor(white);
            int textActivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_active);
            int viewInactivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive);
            int viewInactivePaddingTopWithoutText = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive_without_text);
            final View view = inflater.inflate(R.layout.bottom_navigation, this, false);
            ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
            TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
            title.setTextColor(itemInactiveColor);
            viewList.add(view);
            if (i == currentItem) {
                setBackgroundColor(bottomNavigationItems.get(index).getColor());
                title.setTextColor(currentItem == i ?
                        itemActiveColor :
                        itemInactiveColor);
            }
            view.setPadding(view.getPaddingLeft(), i == 0 ? textActivePaddingTop : withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, view.getPaddingRight(),
                    view.getPaddingBottom());
            icon.setImageResource(bottomNavigationItems.get(i).getImageResource());
            icon.setColorFilter(i == 0 ? itemActiveColor : itemInactiveColor);
            if (i == 0) {
                icon.setScaleX((float) 1.1);
                icon.setScaleY((float) 1.1);
            }
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentItem == i ?
                    context.getResources().getDimension(R.dimen.bottom_navigation_text_size_active) :
                    withText ? context.getResources().getDimension(R.dimen.bottom_navigation_text_size_inactive) : 0);
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

    public void addTab(BottomNavigationItem item) {
        bottomNavigationItems.add(item);
    }

    public void isWithText(boolean withText) {
        this.withText = withText;
    }

    public void isColoredBackground(boolean coloredBackground) {
        this.coloredBackground = coloredBackground;
    }

    private void onBottomNavigationItemClick(final int itemIndex) {
        if (currentItem == itemIndex) {
            return;
        }

        int viewActivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_active);
        int viewInactivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive);
        int viewInactivePaddingTopWithoutText = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive_without_text);
        float textActiveSize = context.getResources().getDimension(R.dimen.bottom_navigation_text_size_active);
        float textInactiveSize = context.getResources().getDimension(R.dimen.bottom_navigation_text_size_inactive);
        for (int i = 0; i < viewList.size(); i++) {
            if (i == itemIndex) {
                View view = viewList.get(itemIndex).findViewById(R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.changeTextColor(title, itemInactiveColor, itemActiveColor);
                BottomNavigationUtils.changeTextSize(title, withText ? textInactiveSize : 0, textActiveSize);
                BottomNavigationUtils.imageColorChange(icon, itemInactiveColor, itemActiveColor);
                BottomNavigationUtils.changeTopPadding(view, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, viewActivePaddingTop);
                icon.animate()
                        .setDuration(150)
                        .scaleX((float) 1.1)
                        .scaleY((float) 1.1)
                        .start();
                int centerX = (int) viewList.get(itemIndex).getX() + viewList.get(itemIndex).getWidth() / 2;
                int centerY = viewList.get(itemIndex).getHeight() / 2;
                int finalRadius = Math.max(getWidth(), getHeight());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    backgroundColorTemp.setBackgroundColor(bottomNavigationItems.get(itemIndex).getColor());
                    Animator changeBackgroundColor = ViewAnimationUtils.createCircularReveal(backgroundColorTemp, centerX, centerY, 0, finalRadius);
                    changeBackgroundColor.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            setBackgroundColor(bottomNavigationItems.get(itemIndex).getColor());
                        }
                    });
                    changeBackgroundColor.start();
                } else {
                    BottomNavigationUtils.backgroundColorChange
                            (this, bottomNavigationItems.get(currentItem).getColor(), bottomNavigationItems.get(itemIndex).getColor());
                }
            } else if (i == currentItem) {
                View view = viewList.get(i).findViewById(R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.imageColorChange(icon, itemActiveColor, itemInactiveColor);
                BottomNavigationUtils.changeTopPadding(view, viewActivePaddingTop, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText);
                BottomNavigationUtils.changeTextColor(title, itemActiveColor, itemInactiveColor);
                BottomNavigationUtils.changeTextSize(title, textActiveSize, withText ? textInactiveSize : 0);
                icon.animate()
                        .setDuration(150)
                        .scaleX(1)
                        .scaleY(1)
                        .start();
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
