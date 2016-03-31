package com.luseen.luseenbottomnavigation.BottomNavigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luseen.luseenbottomnavigation.R;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationView extends RelativeLayout {

    private OnBottomNavigationItemClickListener onBottomNavigationItemClickListener;
    private Context context;
    private final int NAVIGATION_HEIGHT = (int) getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_height);
    private final int NAVIGATION_LINE_WIDTH = (int) getResources().getDimension(R.dimen.bottom_navigation_line_width);
    private int NAVIGATION_WIDTH;
    private int SHADOW_HEIGHT;
    private int currentItem = 0;
    private int itemActiveColorWithoutColoredBackground = -1;
    private int itemInactiveColor;
    private int itemWidth;
    private int itemHeight;
    private boolean withText = true;
    private boolean coloredBackground = true;
    private boolean disableShadow = false;
    private boolean isTablet = false;
    private boolean viewPagerSlide = true;
    private FrameLayout container;
    private View backgroundColorTemp;
    private ViewPager mViewPager;
    private List<BottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();


    public BottomNavigationView(Context context) {
        super(context);
        this.context = context;
    }


    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        NAVIGATION_WIDTH = BottomNavigationUtils.getActionbarSize(context);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (coloredBackground) {
            itemActiveColorWithoutColoredBackground = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.colorActive);
            itemInactiveColor = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.colorInactive);
            SHADOW_HEIGHT = (int) getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_shadow_height);
        } else {
            if (itemActiveColorWithoutColoredBackground == -1)
                itemActiveColorWithoutColoredBackground = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.itemActiveColorWithoutColoredBackground);
            itemInactiveColor = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.withoutColoredBackground);
            SHADOW_HEIGHT = (int) getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_shadow_height_without_colored_background);
        }
        if (isTablet) {
            params.width = NAVIGATION_WIDTH + NAVIGATION_LINE_WIDTH;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = disableShadow ? NAVIGATION_HEIGHT : NAVIGATION_HEIGHT + SHADOW_HEIGHT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setElevation(getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_elevation));
            }
        }
        setLayoutParams(params);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (bottomNavigationItems.size() == 0) {
            throw new NullPointerException("You need at least one item");
        }
        LayoutParams containerParams, params, lineParams;
        int white = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.white);
        backgroundColorTemp = new View(context);
        viewList.clear();
        if (isTablet) {
            itemWidth = LayoutParams.MATCH_PARENT;
            itemHeight = NAVIGATION_WIDTH;
        } else {
            itemWidth = getWidth() / bottomNavigationItems.size();
            itemHeight = LayoutParams.MATCH_PARENT;
        }
        container = new FrameLayout(context);
        View shadow = new View(context);
        View line = new View(context);
        LinearLayout items = new LinearLayout(context);
        items.setOrientation(isTablet ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        LayoutParams shadowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SHADOW_HEIGHT);
        if (isTablet) {
            line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorInactive));
            containerParams = new LayoutParams(NAVIGATION_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams = new LayoutParams(NAVIGATION_LINE_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params = new LayoutParams(NAVIGATION_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
            items.setPadding(0, itemHeight / 2, 0, 0);
            addView(line, lineParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LayoutParams backgroundLayoutParams = new LayoutParams(
                        NAVIGATION_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                container.addView(backgroundColorTemp, backgroundLayoutParams);
            }
        } else {
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT);
            containerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT);
            shadowParams.addRule(RelativeLayout.ABOVE, container.getId());
            shadow.setBackgroundResource(com.luseen.luseenbottomnavigation.R.drawable.shadow);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LayoutParams backgroundLayoutParams = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT);
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                container.addView(backgroundColorTemp, backgroundLayoutParams);
            }
        }
        containerParams.addRule(isTablet ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(shadow, shadowParams);
        addView(container, containerParams);
        container.addView(items, params);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < bottomNavigationItems.size(); i++) {
            final int index = i;
            if (!coloredBackground)
                bottomNavigationItems.get(i).setColor(white);
            int textActivePaddingTop = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_active);
            int viewInactivePaddingTop = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_inactive);
            int viewInactivePaddingTopWithoutText = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_inactive_without_text);
            final View view = inflater.inflate(com.luseen.luseenbottomnavigation.R.layout.bottom_navigation, this, false);
            ImageView icon = (ImageView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_icon);
            TextView title = (TextView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_title);
            if (isTablet)
                title.setVisibility(GONE);
            title.setTextColor(itemInactiveColor);
            viewList.add(view);
            if (i == currentItem) {
                container.setBackgroundColor(bottomNavigationItems.get(index).getColor());
                title.setTextColor(currentItem == i ?
                        itemActiveColorWithoutColoredBackground :
                        itemInactiveColor);
            }
            if (isTablet)
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), i == 0 ? textActivePaddingTop : withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText,
                        view.getPaddingBottom());
            else
                view.setPadding(view.getPaddingLeft(), i == 0 ? textActivePaddingTop : withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, view.getPaddingRight(),
                        view.getPaddingBottom());
            icon.setImageResource(bottomNavigationItems.get(i).getImageResource());
            icon.setColorFilter(i == 0 ? itemActiveColorWithoutColoredBackground : itemInactiveColor);
            if (i == 0) {
                icon.setScaleX((float) 1.1);
                icon.setScaleY((float) 1.1);
            }
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentItem == i ?
                    context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_text_size_active) :
                    withText ? context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_text_size_inactive) : 0);
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

    /**
     * Add item for BottomNavigation
     *
     * @param item item to add
     */
    public void addTab(BottomNavigationItem item) {
        bottomNavigationItems.add(item);
    }

    /**
     * Activate BottomNavigation tablet mode
     */
    public void activateTabletMode() {
        isTablet = true;
    }

    public void isWithText(boolean withText) {
        this.withText = withText;
    }

    public void setItemActiveColorWithoutColoredBackground(int itemActiveColorWithoutColoredBackground) {
        this.itemActiveColorWithoutColoredBackground = itemActiveColorWithoutColoredBackground;
    }

    /**
     * With this BottomNavigation background will be white
     *
     * @param coloredBackground disable or enable background color
     */
    public void isColoredBackground(boolean coloredBackground) {
        this.coloredBackground = coloredBackground;
    }

    /**
     * Disable shadow of BottomNavigationView
     */
    public void disableShadow() {
        disableShadow = true;
    }

    /**
     * Disable slide animation when using ViewPager
     */
    public void disableViewPagerSlide() {
        viewPagerSlide = false;
    }

    private void onBottomNavigationItemClick(final int itemIndex) {

        if (currentItem == itemIndex) {
            return;
        }

        int viewActivePaddingTop = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_active);
        int viewInactivePaddingTop = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_inactive);
        int viewInactivePaddingTopWithoutText = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_inactive_without_text);
        float textActiveSize = context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_text_size_active);
        float textInactiveSize = context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_text_size_inactive);
        int centerX;
        int centerY;
        for (int i = 0; i < viewList.size(); i++) {
            if (i == itemIndex) {
                View view = viewList.get(itemIndex).findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.changeTextColor(title, itemInactiveColor, itemActiveColorWithoutColoredBackground);
                BottomNavigationUtils.changeTextSize(title, withText ? textInactiveSize : 0, textActiveSize);
                BottomNavigationUtils.imageColorChange(icon, itemInactiveColor, itemActiveColorWithoutColoredBackground);
                if (isTablet)
                    BottomNavigationUtils.changeTopRight(view, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, viewActivePaddingTop);
                else
                    BottomNavigationUtils.changeTopPadding(view, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, viewActivePaddingTop);
                icon.animate()
                        .setDuration(150)
                        .scaleX((float) 1.1)
                        .scaleY((float) 1.1)
                        .start();
                if (isTablet) {
                    centerX = viewList.get(itemIndex).getWidth() / 2;
                    centerY = (int) viewList.get(itemIndex).getY() + viewList.get(itemIndex).getHeight() / 2;
                } else {
                    centerX = (int) viewList.get(itemIndex).getX() + viewList.get(itemIndex).getWidth() / 2;
                    centerY = viewList.get(itemIndex).getHeight() / 2;
                }
                int finalRadius = Math.max(getWidth(), getHeight());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    backgroundColorTemp.setBackgroundColor(bottomNavigationItems.get(itemIndex).getColor());
                    Animator changeBackgroundColor = ViewAnimationUtils.createCircularReveal(backgroundColorTemp, centerX, centerY, 0, finalRadius);
                    changeBackgroundColor.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            container.setBackgroundColor(bottomNavigationItems.get(itemIndex).getColor());
                        }
                    });
                    changeBackgroundColor.start();
                } else {
                    BottomNavigationUtils.backgroundColorChange
                            (container, bottomNavigationItems.get(currentItem).getColor(), bottomNavigationItems.get(itemIndex).getColor());
                }
            } else if (i == currentItem) {
                View view = viewList.get(i).findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.imageColorChange(icon, itemActiveColorWithoutColoredBackground, itemInactiveColor);
                BottomNavigationUtils.changeTextColor(title, itemActiveColorWithoutColoredBackground, itemInactiveColor);
                BottomNavigationUtils.changeTextSize(title, textActiveSize, withText ? textInactiveSize : 0);
                if (isTablet)
                    BottomNavigationUtils.changeTopRight(view, viewActivePaddingTop, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText);
                else
                    BottomNavigationUtils.changeTopPadding(view, viewActivePaddingTop, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText);
                icon.animate()
                        .setDuration(150)
                        .scaleX(1)
                        .scaleY(1)
                        .start();
            }
        }

        if (mViewPager != null)
            mViewPager.setCurrentItem(itemIndex, viewPagerSlide);

        if (onBottomNavigationItemClickListener != null)
            onBottomNavigationItemClickListener.onNavigationItemClick(itemIndex);
        currentItem = itemIndex;
    }

    /**
     * Creates a connection between this navigation view and a ViewPager
     *
     * @param pager          pager to connect to
     * @param colorResources color resources for every item in the ViewPager adapter
     * @param imageResources images resources for every item in the ViewPager adapter
     */

    public void setViewPager(ViewPager pager, int[] colorResources, int[] imageResources) {
        this.mViewPager = pager;
        if (pager.getAdapter().getCount() != colorResources.length || pager.getAdapter().getCount() != imageResources.length)
            throw new IllegalArgumentException("colorResources and imageResources must be equal to the ViewPager items : " + pager.getAdapter().getCount());

        for (int i = 0; i < pager.getAdapter().getCount(); i++)
            addTab(new BottomNavigationItem(pager.getAdapter().getPageTitle(i).toString(), colorResources[i], imageResources[i]));

        mViewPager.addOnPageChangeListener(new internalViewPagerListener());
        invalidate();
    }

    private class internalViewPagerListener implements ViewPager.OnPageChangeListener {

        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mScrollState == ViewPager.SCROLL_STATE_DRAGGING)
                onBottomNavigationItemClick(position);
        }

        @Override
        public void onPageSelected(int position) {
            onBottomNavigationItemClick(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING)
                mScrollState = ViewPager.SCROLL_STATE_DRAGGING;
            else if (state == ViewPager.SCROLL_STATE_IDLE)
                mScrollState = ViewPager.SCROLL_STATE_IDLE;
        }
    }


    /**
     * Setup interface for item onClick
     */

    public void setOnBottomNavigationItemClickListener(OnBottomNavigationItemClickListener onBottomNavigationItemClickListener) {
        this.onBottomNavigationItemClickListener = onBottomNavigationItemClickListener;
    }
}
