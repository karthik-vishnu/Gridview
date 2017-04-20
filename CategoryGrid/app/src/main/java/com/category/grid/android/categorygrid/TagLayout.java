package com.category.grid.android.categorygrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Nilanchala on 8/3/15.
 */
public class TagLayout extends ViewGroup {
    int deviceWidth;
    int childWithPadding;
    int childLeftSpacing;

    public TagLayout(Context context) {
        this(context, null, 0);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.v("context "," "+context);
        Log.v("attrs "," "+attrs);
        Log.v("defStyleAttr "," "+defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        Log.v("num of ","columns "+typedArray.getInteger(0, 0));
        typedArray.recycle();

        init(context);
    }

    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

    }

    @Override
    protected void onLayout(boolean b, int index, int i1, int i2, int i3) {

        int count = getChildCount();
        int parentLeft = getPaddingLeft();
        int parentTop = getPaddingTop();
        int parentRight = getPaddingRight();
        int parentBottom = getPaddingBottom();

        int parentWidth = getMeasuredWidth();
        int parentHeight = getMeasuredHeight();

        int childLeft = parentLeft;
        int childTop = parentTop;
        int maxHeight = 0;

        for (int i=0; i<count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                return;

            child.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST));
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if(childWidth != 0 ) {
                if(i == 0) {
                    int[] values = imageSize(parentWidth, childWidth);
                    childLeftSpacing = values[0];
                    childLeft = childLeftSpacing;
                    childWithPadding = values[1];
                    childTop = childLeftSpacing/2;
                }
                childWidth = childWithPadding;

            }

            if(childLeft >= (parentWidth-5)) {
                childTop += maxHeight;
                childLeft = childLeftSpacing;
                maxHeight = 0;
            }
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

            if(maxHeight < childHeight)
                maxHeight = childHeight;

            childLeft += childWidth;
        }
    }

    public int[] imageSize(int parentWidth, int childWidth) {
        int numOfColumns = parentWidth/childWidth;
        int imageSize = parentWidth/numOfColumns;
        int[] values = new int[2];
        if(imageSize > childWidth) {
            int diff = imageSize - childWidth;
            diff = diff * numOfColumns;
            diff = diff / (numOfColumns +1);
            childWidth = childWidth + diff;
            values[0] = diff;
            values[1] = childWidth;
        }
        return values;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v("on ","measure " +widthMeasureSpec + heightMeasureSpec);
        int count = getChildCount();
        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();
            Log.v("Divided ","val "+ mLeftWidth/deviceWidth);
            if ((mLeftWidth / deviceWidth) > rowCount) {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }
}