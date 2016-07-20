package com.luseen.luseenbottomnavigation.BottomNavigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
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

    private float textActiveSize;

    private float textInactiveSize;

    private List<BottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    private List<View> viewList = new ArrayList<>();

    private int itemActiveColorWithoutColoredBackground = -1;

    private static int currentItem = 0;

    private int navigationWidth;

    private int shadowHeight;

    private int itemInactiveColor;

    private int itemWidth;

    private int itemHeight;

    private boolean withText;

    private boolean coloredBackground;

    private boolean disableShadow;

    private boolean isTablet;

    private boolean viewPagerSlide;

    private boolean isCustomFont = false;

    private boolean willNotRecreate = true;

    private FrameLayout container;

    private View backgroundColorTemp;

    private ViewPager mViewPager;

    private Typeface font;


    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            Resources res = getResources();

            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView);
            withText = array.getBoolean(R.styleable.BottomNavigationView_bnv_with_text, true);
            coloredBackground = array.getBoolean(R.styleable.BottomNavigationView_bnv_colored_background, true);
            disableShadow = array.getBoolean(R.styleable.BottomNavigationView_bnv_shadow, false);
            isTablet = array.getBoolean(R.styleable.BottomNavigationView_bnv_tablet, false);
            viewPagerSlide = array.getBoolean(R.styleable.BottomNavigationView_bnv_viewpager_slide, true);
            itemActiveColorWithoutColoredBackground = array.getColor(R.styleable.BottomNavigationView_bnv_active_color, -1);
            textActiveSize = array.getDimensionPixelSize(R.styleable.BottomNavigationView_bnv_active_text_size, res.getDimensionPixelSize(R.dimen.bottom_navigation_text_size_active));
            textInactiveSize = array.getDimensionPixelSize(R.styleable.BottomNavigationView_bnv_inactive_text_size, res.getDimensionPixelSize(R.dimen.bottom_navigation_text_size_inactive));

            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        navigationWidth = BottomNavigationUtils.getActionbarSize(context);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (coloredBackground) {
            itemActiveColorWithoutColoredBackground = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.colorActive);
            itemInactiveColor = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.colorInactive);
            shadowHeight = (int) getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_shadow_height);
        } else {
            if (itemActiveColorWithoutColoredBackground == -1)
                itemActiveColorWithoutColoredBackground = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.itemActiveColorWithoutColoredBackground);
            itemInactiveColor = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.withoutColoredBackground);
            shadowHeight = (int) getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_shadow_height_without_colored_background);
        }
        if (isTablet) {
            params.width = navigationWidth + NAVIGATION_LINE_WIDTH;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = disableShadow ? NAVIGATION_HEIGHT : NAVIGATION_HEIGHT + shadowHeight;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setElevation(getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_elevation));
            }
        }
        setLayoutParams(params);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (willNotRecreate)
            removeAllViews();
        if (currentItem < 0 || currentItem > (bottomNavigationItems.size() - 1)) {
            throw new IndexOutOfBoundsException(currentItem < 0 ? "Position must be 0 or greater than 0, current is " + currentItem
                    : "Position must be less or equivalent than items size, items size is " + (bottomNavigationItems.size() - 1) + " current is " + currentItem);
        }
        if (bottomNavigationItems.size() == 0) {
            throw new NullPointerException("You need at least one item");
        }
        LayoutParams containerParams, params, lineParams;
        int white = ContextCompat.getColor(context, com.luseen.luseenbottomnavigation.R.color.white);
        backgroundColorTemp = new View(context);
        viewList.clear();
        if (isTablet) {
            itemWidth = LayoutParams.MATCH_PARENT;
            itemHeight = navigationWidth;
        } else {
            itemWidth = getWidth() / bottomNavigationItems.size();
            itemHeight = LayoutParams.MATCH_PARENT;
        }
        container = new FrameLayout(context);
        View shadow = new View(context);
        View line = new View(context);
        LinearLayout items = new LinearLayout(context);
        items.setOrientation(isTablet ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        LayoutParams shadowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, shadowHeight);
        if (isTablet) {
            line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorInactive));
            containerParams = new LayoutParams(navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams = new LayoutParams(NAVIGATION_LINE_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params = new LayoutParams(navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            items.setPadding(0, itemHeight / 2, 0, 0);
            addView(line, lineParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LayoutParams backgroundLayoutParams = new LayoutParams(
                        navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
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

            if (isCustomFont)
                title.setTypeface(font);

            if (isTablet)
                title.setVisibility(GONE);

            title.setTextColor(itemInactiveColor);
            viewList.add(view);

            if (bottomNavigationItems.get(i).getImageResourceActive() != 0) {
                if (i == currentItem)
                    icon.setImageResource(bottomNavigationItems.get(i).getImageResourceActive());
                else
                    bottomNavigationItems.get(i).getImageResource();
            } else {
                icon.setImageResource(bottomNavigationItems.get(i).getImageResource());
                icon.setColorFilter(i == currentItem ? itemActiveColorWithoutColoredBackground : itemInactiveColor);
            }

            if (i == currentItem) {
                container.setBackgroundColor(bottomNavigationItems.get(index).getColor());
                title.setTextColor(itemActiveColorWithoutColoredBackground);
                icon.setScaleX((float) 1.1);
                icon.setScaleY((float) 1.1);
            }

            if (isTablet)
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), i == currentItem ? textActivePaddingTop : withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText,
                        view.getPaddingBottom());
            else
                view.setPadding(view.getPaddingLeft(), i == currentItem ? textActivePaddingTop : withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, view.getPaddingRight(),
                        view.getPaddingBottom());

            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, i == currentItem ?
                    textActiveSize :
                    withText ? textInactiveSize : 0);
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

    private void onBottomNavigationItemClick(final int itemIndex) {

        if (currentItem == itemIndex) {
            return;
        }

        int viewActivePaddingTop = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_active);
        int viewInactivePaddingTop = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_inactive);
        int viewInactivePaddingTopWithoutText = (int) context.getResources().getDimension(com.luseen.luseenbottomnavigation.R.dimen.bottom_navigation_padding_top_inactive_without_text);
        int centerX;
        int centerY;
        for (int i = 0; i < viewList.size(); i++) {
            if (i == itemIndex) {
                View view = viewList.get(itemIndex).findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.changeTextColor(title, itemInactiveColor, itemActiveColorWithoutColoredBackground);
                BottomNavigationUtils.changeTextSize(title, withText ? textInactiveSize : 0, textActiveSize);

                if (bottomNavigationItems.get(i).getImageResourceActive() != 0) {
                    icon.setImageResource((bottomNavigationItems.get(i).getImageResourceActive()));
                } else {
                    BottomNavigationUtils.changeImageColorFilter(icon, itemInactiveColor, itemActiveColorWithoutColoredBackground);
                }

                if (isTablet)
                    BottomNavigationUtils.changeRightPadding(view, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, viewActivePaddingTop);
                else {
                    BottomNavigationUtils.changeViewTopPadding(view, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, viewActivePaddingTop);
                }

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
                    BottomNavigationUtils.changeViewBackgroundColor
                            (container, bottomNavigationItems.get(currentItem).getColor(), bottomNavigationItems.get(itemIndex).getColor());
                }

            } else if (i == currentItem) {
                View view = viewList.get(i).findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(com.luseen.luseenbottomnavigation.R.id.bottom_navigation_item_icon);

                if (bottomNavigationItems.get(i).getImageResourceActive() != 0) {
                    icon.setImageResource((bottomNavigationItems.get(i).getImageResource()));
                } else {
                    BottomNavigationUtils.changeImageColorFilter(icon, itemActiveColorWithoutColoredBackground, itemInactiveColor);
                }

                BottomNavigationUtils.changeTextColor(title, itemActiveColorWithoutColoredBackground, itemInactiveColor);
                BottomNavigationUtils.changeTextSize(title, textActiveSize, withText ? textInactiveSize : 0);

                if (isTablet)
                    BottomNavigationUtils.changeRightPadding(view, viewActivePaddingTop, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText);
                else
                    BottomNavigationUtils.changeViewTopPadding(view, viewActivePaddingTop, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText);

                icon.animate()
                        .setDuration(150)
                        .scaleX((float) 0.9)
                        .scaleY((float) 0.9)
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

    public void setUpWithViewPager(ViewPager pager, int[] colorResources, int[] imageResources) {
        this.mViewPager = pager;
        if (pager.getAdapter().getCount() != colorResources.length || pager.getAdapter().getCount() != imageResources.length)
            throw new IllegalArgumentException("colorResources and imageResources must be equal to the ViewPager items : " + pager.getAdapter().getCount());

        for (int i = 0; i < pager.getAdapter().getCount(); i++)
            addTab(new BottomNavigationItem(pager.getAdapter().getPageTitle(i).toString(), colorResources[i], imageResources[i]));
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

    /**
     * Change text visibility
     *
     * @param withText disable or enable item text
     */
    public void isWithText(boolean withText) {
        this.withText = withText;
    }

    /**
     * Item Active Color if isColoredBackground(false)
     *
     * @param itemActiveColorWithoutColoredBackground active item color
     */
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
     * Change tab programmatically
     *
     * @param position selected tab position
     */
    public void selectTab(int position) {
        onBottomNavigationItemClick(position);
        currentItem = position;
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

    /**
     * Change Active text size
     *
     * @param textActiveSize size
     */
    public void setTextActiveSize(float textActiveSize) {
        this.textActiveSize = textActiveSize;
    }

    /**
     * Change Inactive text size
     *
     * @param textInactiveSize size
     */
    public void setTextInactiveSize(float textInactiveSize) {
        this.textInactiveSize = textInactiveSize;
    }

    /**
     * Setup interface for item onClick
     */
    public void setOnBottomNavigationItemClickListener(OnBottomNavigationItemClickListener onBottomNavigationItemClickListener) {
        this.onBottomNavigationItemClickListener = onBottomNavigationItemClickListener;
    }

    /**
     * Returns the item that is currently selected
     *
     * @return Currently selected item
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * If your activity/fragment will not recreate
     * you can call this method
     *
     * @param willNotRecreate set true if will not recreate
     */
    public void willNotRecreate(boolean willNotRecreate) {
        this.willNotRecreate = willNotRecreate;
    }

    /**
     * set custom font for item texts
     *
     * @param font custom font
     */
    public void setFont(Typeface font) {
        isCustomFont = true;
        this.font = font;
    }

    /**
     * get item text size on active status
     *
     * @return font size
     */
    public float getTextActiveSize() {
        return textActiveSize;
    }

    /**
     * get item text size on inactive status
     *
     * @return font size
     */
    public float getTextInactiveSize() {
        return textInactiveSize;
    }


    public BottomNavigationItem getItem(int position) {
        onBottomNavigationItemClick(position);
        return bottomNavigationItems.get(position);
    }
}
