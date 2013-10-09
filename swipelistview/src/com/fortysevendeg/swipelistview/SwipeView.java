package com.fortysevendeg.swipelistview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by Vladislav Lipskiy on 10/9/13.
 * @param <T> adapter type
 */
public interface SwipeView<T> {

    /**
     * Used when user want change swipe list mode on some rows
     */
    public final static int SWIPE_MODE_DEFAULT = -1;

    /**
     * Disables all swipes
     */
    public final static int SWIPE_MODE_NONE = 0;

    /**
     * Enables both left and right swipe
     */
    public final static int SWIPE_MODE_BOTH = 1;

    /**
     * Enables right swipe
     */
    public final static int SWIPE_MODE_RIGHT = 2;

    /**
     * Enables left swipe
     */
    public final static int SWIPE_MODE_LEFT = 3;

    /**
     * Binds the swipe gesture to reveal a view behind the row (Drawer style)
     */
    public final static int SWIPE_ACTION_REVEAL = 0;

    /**
     * Dismisses the cell when swiped over
     */
    public final static int SWIPE_ACTION_DISMISS = 1;

    /**
     * Marks the cell as checked when swiped and release
     */
    public final static int SWIPE_ACTION_CHOICE = 2;

    /**
     * No action when swiped
     */
    public final static int SWIPE_ACTION_NONE = 3;

    /**
     * Default ids for front view
     */
    public final static String SWIPE_DEFAULT_FRONT_VIEW = "swipelist_frontview";

    /**
     * Default id for back view
     */
    public final static String SWIPE_DEFAULT_BACK_VIEW = "swipelist_backview";

    /**
     * Indicates no movement
     */
    final static int TOUCH_STATE_REST = 0;

    /**
     * State scrolling x position
     */
    final static int TOUCH_STATE_SCROLLING_X = 1;

    /**
     * State scrolling y position
     */
    final static int TOUCH_STATE_SCROLLING_Y = 2;

    /**
     * Recycle cell. This method should be called from getView in Adapter when use SWIPE_ACTION_CHOICE
     *
     * @param convertView parent view
     * @param position    position in list
     */
    public void recycle(View convertView, int position);

    /**
     * Get if item is selected
     *
     * @param position position in list
     * @return
     */
    public boolean isChecked(int position);

    /**
     * Get positions selected
     *
     * @return
     */
    public List<Integer> getPositionsSelected();

    /**
     * Count selected
     *
     * @return
     */
    public int getCountSelected();

    /**
     * Unselected choice state in item
     */
    public void unselectedChoiceStates();

    /**
     * @see android.widget.ListView#setAdapter(android.widget.ListAdapter)
     * @see android.widget.ExpandableListView#setAdapter(android.widget.ExpandableListAdapter)
     */
    public void setMainAdapter(T adapter);

    /** @return Adapter */
    public T getMainAdapter();

    /**
     * Dismiss item
     *
     * @param position Position that you want open
     */
    public void dismiss(int position);

    /**
     * Dismiss items selected
     */
    public void dismissSelected();

    /**
     * Open ListView's item
     *
     * @param position Position that you want open
     */
    public void openAnimate(int position);

    /**
     * Close ListView's item
     *
     * @param position Position that you want open
     */
    public void closeAnimate(int position);

    /**
     * Sets the Listener
     *
     * @param swipeViewListener Listener
     */
    public void setSwipeViewListener(SwipeViewListener swipeViewListener);

    /**
     * Resets scrolling
     */
    public void resetScrolling();

    /**
     * Set offset on right
     *
     * @param offsetRight Offset
     */
    public void setOffsetRight(float offsetRight);

    /**
     * Set offset on left
     *
     * @param offsetLeft Offset
     */
    public void setOffsetLeft(float offsetLeft);

    /**
     * Set if all items opened will be closed when the user moves the ListView
     *
     * @param swipeCloseAllItemsWhenMoveList
     */
    public void setSwipeCloseAllItemsWhenMoveList(boolean swipeCloseAllItemsWhenMoveList);

    /**
     * Sets if the user can open an item with long pressing on cell
     *
     * @param swipeOpenOnLongPress
     */
    public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress);

    /**
     * Set swipe mode
     *
     * @param swipeMode
     */
    public void setSwipeMode(int swipeMode);

    /**
     * Return action on left
     *
     * @return Action
     */
    public int getSwipeActionLeft();

    /**
     * Set action on left
     *
     * @param swipeActionLeft Action
     */
    public void setSwipeActionLeft(int swipeActionLeft);

    /**
     * Return action on right
     *
     * @return Action
     */
    public int getSwipeActionRight();

    /**
     * Set action on right
     *
     * @param swipeActionRight Action
     */
    public void setSwipeActionRight(int swipeActionRight);

    /**
     * Sets animation time when user drops cell
     *
     * @param animationTime milliseconds
     */
    public void setAnimationTime(long animationTime);

    /**
     * Close all opened items
     */
    public void closeOpenedItems();

    public Context getContext();

    /**
     * Notifies onDismiss
     *
     * @param reverseSortedPositions All dismissed positions
     */
    void onDismiss(int[] reverseSortedPositions);

    /**
     * Start open item
     *
     * @param position list item
     * @param action   current action
     * @param right    to right
     */
    void onStartOpen(int position, int action, boolean right);

    /**
     * Start close item
     *
     * @param position list item
     * @param right
     */
    void onStartClose(int position, boolean right);

    /**
     * Notifies onClickFrontView
     *
     * @param position item clicked
     */
    void onClickFrontView(int position);

    /**
     * Notifies onClickBackView
     *
     * @param position back item clicked
     */
    void onClickBackView(int position);

    /**
     * Notifies onOpened
     *
     * @param position Item opened
     * @param toRight  If should be opened toward the right
     */
    void onOpened(int position, boolean toRight);

    /**
     * Notifies onClosed
     *
     * @param position  Item closed
     * @param fromRight If open from right
     */
    void onClosed(int position, boolean fromRight);

    /**
     * Notifies onChoiceChanged
     *
     * @param position position that choice
     * @param selected if item is selected or not
     */
    void onChoiceChanged(int position, boolean selected);

    /**
     * User start choice items
     */
    void onChoiceStarted();

    /**
     * User end choice items
     */
    void onChoiceEnded();

    /**
     * User is in first item of list
     */
    void onFirstListItem();

    /**
     * User is in last item of list
     */
    void onLastListItem();

    /**
     * Notifies onListChanged
     */
    void onListChanged();

    /**
     * Notifies onMove
     *
     * @param position Item moving
     * @param x        Current position
     */
    void onMove(int position, float x);

    int changeSwipeMode(int position);

    View getChildAt(int position);

    int getFirstVisiblePosition();

    int getLastVisiblePosition();

    void setItemChecked(int position, boolean checked);

    int getWidth();

    int getHeight();

    int getChildCount();

    void getLocationOnScreen(int[] location);

    int getPositionForView(View view);

    void requestDisallowInterceptTouchEvent(boolean disallowIntercept);

    boolean onTouchEvent(MotionEvent ev);

    /** @see android.widget.ExpandableListView#getExpandableListPosition(int) */
    public long getExpandableListPosition(int flatListPosition);

    /** @see android.widget.ExpandableListView#getFlatListPosition(long) */
    public int getFlatListPosition(long packedPosition);

}
