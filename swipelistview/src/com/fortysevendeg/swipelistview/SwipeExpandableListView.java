package com.fortysevendeg.swipelistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.List;

/**
 * Created by Vladislav Lipskiy on 10/9/13.
 */
public class SwipeExpandableListView extends ExpandableListView implements SwipeView<ExpandableListAdapter> {

    private int touchState = TOUCH_STATE_REST;

    private float lastMotionX;
    private float lastMotionY;
    private int touchSlop;

    int swipeFrontView = 0;
    int swipeBackView = 0;

    /**
     * Internal listener for common swipe events
     */
    private SwipeViewListener swipeViewListener;

    /**
     * Internal touch listener
     */
    private SwipeExpandableListViewTouchListener touchListener;

    /**
     * If you create a View programmatically you need send back and front identifier
     *
     * @param context        Context
     * @param swipeBackView  Back Identifier
     * @param swipeFrontView Front Identifier
     */
    public SwipeExpandableListView(Context context, int swipeBackView, int swipeFrontView) {
        super(context);
        this.swipeFrontView = swipeFrontView;
        this.swipeBackView = swipeBackView;
        init(null);
    }

    /**
     * @see android.widget.ExpandableListView#ExpandableListView(android.content.Context, android.util.AttributeSet)
     */
    public SwipeExpandableListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * @see android.widget.ExpandableListView#ExpandableListView(android.content.Context, android.util.AttributeSet, int)
     */
    public SwipeExpandableListView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Init ListView
     *
     * @param attrs AttributeSet
     */
    private void init(AttributeSet attrs) {

        int swipeMode = SWIPE_MODE_BOTH;
        boolean swipeOpenOnLongPress = true;
        boolean swipeCloseAllItemsWhenMoveList = true;
        long swipeAnimationTime = 0;
        float swipeOffsetLeft = 0;
        float swipeOffsetRight = 0;
        int swipeDrawableChecked = 0;
        int swipeDrawableUnchecked = 0;

        int swipeActionLeft = SWIPE_ACTION_REVEAL;
        int swipeActionRight = SWIPE_ACTION_REVEAL;

        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.SwipeListView);
            swipeMode = styled.getInt(R.styleable.SwipeListView_swipeMode, SWIPE_MODE_BOTH);
            swipeActionLeft = styled.getInt(R.styleable.SwipeListView_swipeActionLeft, SWIPE_ACTION_REVEAL);
            swipeActionRight = styled.getInt(R.styleable.SwipeListView_swipeActionRight, SWIPE_ACTION_REVEAL);
            swipeOffsetLeft = styled.getDimension(R.styleable.SwipeListView_swipeOffsetLeft, 0);
            swipeOffsetRight = styled.getDimension(R.styleable.SwipeListView_swipeOffsetRight, 0);
            swipeOpenOnLongPress = styled.getBoolean(R.styleable.SwipeListView_swipeOpenOnLongPress, true);
            swipeAnimationTime = styled.getInteger(R.styleable.SwipeListView_swipeAnimationTime, 0);
            swipeCloseAllItemsWhenMoveList = styled.getBoolean(R.styleable.SwipeListView_swipeCloseAllItemsWhenMoveList, true);
            swipeDrawableChecked = styled.getResourceId(R.styleable.SwipeListView_swipeDrawableChecked, 0);
            swipeDrawableUnchecked = styled.getResourceId(R.styleable.SwipeListView_swipeDrawableUnchecked, 0);
            swipeFrontView = styled.getResourceId(R.styleable.SwipeListView_swipeFrontView, 0);
            swipeBackView = styled.getResourceId(R.styleable.SwipeListView_swipeBackView, 0);
        }

        if (swipeFrontView == 0 || swipeBackView == 0) {
            swipeFrontView = getContext().getResources().getIdentifier(SWIPE_DEFAULT_FRONT_VIEW, "id", getContext().getPackageName());
            swipeBackView = getContext().getResources().getIdentifier(SWIPE_DEFAULT_BACK_VIEW, "id", getContext().getPackageName());

            if (swipeFrontView == 0 || swipeBackView == 0) {
                throw new RuntimeException(String.format("You forgot the attributes swipeFrontView or swipeBackView. You can add this attributes or use '%s' and '%s' identifiers", SWIPE_DEFAULT_FRONT_VIEW, SWIPE_DEFAULT_BACK_VIEW));
            }
        }

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        touchListener = new SwipeExpandableListViewTouchListener(this, swipeFrontView, swipeBackView);
        if (swipeAnimationTime > 0) {
            touchListener.setAnimationTime(swipeAnimationTime);
        }
        touchListener.setRightOffset(swipeOffsetRight);
        touchListener.setLeftOffset(swipeOffsetLeft);
        touchListener.setSwipeActionLeft(swipeActionLeft);
        touchListener.setSwipeActionRight(swipeActionRight);
        touchListener.setSwipeMode(swipeMode);
        touchListener.setSwipeClosesAllItemsWhenListMoves(swipeCloseAllItemsWhenMoveList);
        touchListener.setSwipeOpenOnLongPress(swipeOpenOnLongPress);
        touchListener.setSwipeDrawableChecked(swipeDrawableChecked);
        touchListener.setSwipeDrawableUnchecked(swipeDrawableUnchecked);
        setOnTouchListener(touchListener);
        setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    public void recycle(final View convertView, final int position) {
        touchListener.reloadChoiceStateInView(convertView.findViewById(swipeFrontView), position);
    }

    @Override
    public boolean isChecked(final int position) {
        return touchListener.isChecked(position);
    }

    @Override
    public List<Integer> getPositionsSelected() {
        return touchListener.getPositionsSelected();
    }

    @Override
    public int getCountSelected() {
        return touchListener.getCountSelected();
    }

    @Override
    public void unselectedChoiceStates() {
        touchListener.unselectedChoiceStates();
    }

    @Override
    public void setMainAdapter(final ExpandableListAdapter adapter) {
        setAdapter(adapter);
    }

    @Override
    public void setAdapter(final ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        touchListener.resetItems();
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                onListChanged();
                touchListener.resetItems();
            }
        });
    }

    @Override
    public ExpandableListAdapter getMainAdapter() {
        return getExpandableListAdapter();
    }

    @Override
    public void dismiss(final int position) {
        int height = touchListener.dismiss(position);
        if (height > 0) {
            touchListener.handlerPendingDismisses(height);
        } else {
            int[] dismissPositions = new int[1];
            dismissPositions[0] = position;
            onDismiss(dismissPositions);
            touchListener.resetPendingDismisses();
        }
    }

    @Override
    public void dismissSelected() {
        List<Integer> list = touchListener.getPositionsSelected();
        int[] dismissPositions = new int[list.size()];
        int height = 0;
        for (int i = 0; i < list.size(); i++) {
            int position = list.get(i);
            dismissPositions[i] = position;
            int auxHeight = touchListener.dismiss(position);
            if (auxHeight > 0) {
                height = auxHeight;
            }
        }
        if (height > 0) {
            touchListener.handlerPendingDismisses(height);
        } else {
            onDismiss(dismissPositions);
            touchListener.resetPendingDismisses();
        }
        touchListener.returnOldActions();
    }

    @Override
    public void openAnimate(final int position) {
        touchListener.openAnimate(position);
    }

    @Override
    public void closeAnimate(final int position) {
        touchListener.closeAnimate(position);
    }

    @Override
    public void setSwipeViewListener(final SwipeViewListener swipeViewListener) {
        this.swipeViewListener = swipeViewListener;
    }

    @Override
    public void resetScrolling() {
        touchState = TOUCH_STATE_REST;
    }

    @Override
    public void setOffsetRight(final float offsetRight) {
        touchListener.setRightOffset(offsetRight);
    }

    @Override
    public void setOffsetLeft(final float offsetLeft) {
        touchListener.setLeftOffset(offsetLeft);
    }

    @Override
    public void setSwipeCloseAllItemsWhenMoveList(final boolean swipeCloseAllItemsWhenMoveList) {
        touchListener.setSwipeClosesAllItemsWhenListMoves(swipeCloseAllItemsWhenMoveList);
    }

    @Override
    public void setSwipeOpenOnLongPress(final boolean swipeOpenOnLongPress) {
        touchListener.setSwipeOpenOnLongPress(swipeOpenOnLongPress);
    }

    @Override
    public void setSwipeMode(final int swipeMode) {
        touchListener.setSwipeMode(swipeMode);
    }

    @Override
    public int getSwipeActionLeft() {
        return touchListener.getSwipeActionLeft();
    }

    @Override
    public void setSwipeActionLeft(final int swipeActionLeft) {
        touchListener.setSwipeActionLeft(swipeActionLeft);
    }

    @Override
    public int getSwipeActionRight() {
        return touchListener.getSwipeActionRight();
    }

    @Override
    public void setSwipeActionRight(final int swipeActionRight) {
        touchListener.setSwipeActionRight(swipeActionRight);
    }

    @Override
    public void setAnimationTime(final long animationTime) {
        touchListener.setAnimationTime(animationTime);
    }

    @Override
    public void closeOpenedItems() {
        touchListener.closeOpenedItems();
    }

    @Override
    public void onDismiss(final int[] reverseSortedPositions) {
        if (swipeViewListener != null) {
            swipeViewListener.onDismiss(reverseSortedPositions);
        }
    }

    @Override
    public void onStartOpen(final int position, final int action, final boolean right) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onStartOpen(position, action, right);
        }
    }

    @Override
    public void onStartClose(final int position, final boolean right) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onStartClose(position, right);
        }
    }

    @Override
    public void onClickFrontView(final int position) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onClickFrontView(position);
        }
    }

    @Override
    public void onClickBackView(final int position) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onClickBackView(position);
        }
    }

    @Override
    public void onOpened(final int position, final boolean toRight) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onOpened(position, toRight);
        }
    }

    @Override
    public void onClosed(final int position, final boolean fromRight) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onClosed(position, fromRight);
        }
    }

    @Override
    public void onChoiceChanged(final int position, final boolean selected) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onChoiceChanged(position, selected);
        }
    }

    @Override
    public void onChoiceStarted() {
        if (swipeViewListener != null) {
            swipeViewListener.onChoiceStarted();
        }
    }

    @Override
    public void onChoiceEnded() {
        if (swipeViewListener != null) {
            swipeViewListener.onChoiceEnded();
        }
    }

    @Override
    public void onFirstListItem() {
        if (swipeViewListener != null) {
            swipeViewListener.onFirstListItem();
        }
    }

    @Override
    public void onLastListItem() {
        if (swipeViewListener != null) {
            swipeViewListener.onLastListItem();
        }
    }

    @Override
    public void onListChanged() {
        if (swipeViewListener != null) {
            swipeViewListener.onListChanged();
        }
    }

    @Override
    public void onMove(final int position, final float x) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            swipeViewListener.onMove(position, x);
        }
    }

    @Override
    public int changeSwipeMode(final int position) {
        if (swipeViewListener != null && position != AdapterView.INVALID_POSITION) {
            return swipeViewListener.onChangeSwipeMode(position);
        }
        return SWIPE_MODE_DEFAULT;
    }

    /**
     * @see android.widget.ExpandableListView#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        final float x = ev.getX();
        final float y = ev.getY();

        if (isEnabled() && touchListener.isSwipeEnabled()) {

            if (touchState == TOUCH_STATE_SCROLLING_X) {
                return touchListener.onTouch(this, ev);
            }

            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    checkInMoving(x, y);
                    return touchState == TOUCH_STATE_SCROLLING_Y;
                case MotionEvent.ACTION_DOWN:
                    touchListener.onTouch(this, ev);
                    touchState = TOUCH_STATE_REST;
                    lastMotionX = x;
                    lastMotionY = y;
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    touchState = TOUCH_STATE_REST;
                    break;
                case MotionEvent.ACTION_UP:
                    touchListener.onTouch(this, ev);
                    return touchState == TOUCH_STATE_SCROLLING_Y;
                default:
                    break;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    /**
     * Check if the user is moving the cell
     *
     * @param x Position X
     * @param y Position Y
     */
    private void checkInMoving(float x, float y) {
        final int xDiff = (int) Math.abs(x - lastMotionX);
        final int yDiff = (int) Math.abs(y - lastMotionY);

        final int touchSlop = this.touchSlop;
        boolean xMoved = xDiff > touchSlop;
        boolean yMoved = yDiff > touchSlop;

        if (xMoved) {
            touchState = TOUCH_STATE_SCROLLING_X;
            lastMotionX = x;
            lastMotionY = y;
        }

        if (yMoved) {
            touchState = TOUCH_STATE_SCROLLING_Y;
            lastMotionX = x;
            lastMotionY = y;
        }
    }

}
