package com.luseen.luseenbottomnavigation.BottomNavigation;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BottomNavigationUtils {

    public static void imageColorChange(final ImageView image, int fromColor, int toColor) {
        ValueAnimator imageColorChangeAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        imageColorChangeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                image.setColorFilter((Integer) animator.getAnimatedValue());
            }
        });
        imageColorChangeAnimation.setDuration(150);
        imageColorChangeAnimation.start();
    }

    public static void backgroundColorChange(final View view, int fromColor, int toColor) {
        ValueAnimator imageColorChangeAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        imageColorChangeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });
        imageColorChangeAnimation.setDuration(150);
        imageColorChangeAnimation.start();
    }

    public static void changeTopPadding(final View view, int fromPadding, int toPadding) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromPadding, toPadding);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                view.setPadding(view.getPaddingLeft(),
                        (int) animatedValue,
                        view.getPaddingRight(),
                        view.getPaddingBottom());
            }
        });
        animator.start();
    }

    public static void changeTextSize(final TextView textView, float from, float to) {
        ValueAnimator textSizeChangeAnimator = ValueAnimator.ofFloat(from, to);
        textSizeChangeAnimator.setDuration(150);
        textSizeChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) valueAnimator.getAnimatedValue());
            }
        });
        textSizeChangeAnimator.start();
    }

    public static void changeTextColor(final TextView textView, int fromColor, int toColor) {
        ValueAnimator changeTextColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        changeTextColorAnimation.setDuration(150);
        changeTextColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        changeTextColorAnimation.start();
    }
}
