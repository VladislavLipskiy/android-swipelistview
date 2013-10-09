package com.fortysevendeg.swipelistview;

import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by Vladislav Lipskiy on 10/9/13.
 */
public class SwipeExpandableListViewTouchListener extends AbsSwipeViewTouchListener<ExpandableListAdapter> {

    /**
     * Constructor
     *
     * @param swipeListView  SwipeListView
     * @param swipeFrontView front view Identifier
     * @param swipeBackView  back view Identifier
     */
    public SwipeExpandableListViewTouchListener(final SwipeView<ExpandableListAdapter> swipeListView, final int swipeFrontView, final int swipeBackView) {
        super(swipeListView, swipeFrontView, swipeBackView);
    }

    @Override
    protected int getCount() {
        ExpandableListAdapter adapter = swipeListView.getMainAdapter();
        int count = 0;
        final int groupCount = adapter.getGroupCount();
        count += groupCount;
        for (int i = 0; i < groupCount; i++) {
            count += adapter.getChildrenCount(i);
        }
        return count;
    }

    @Override
    protected boolean isSwipeAllowed(final int childPosition) {
        final long packedPosition = swipeListView.getExpandableListPosition(childPosition);
        return ExpandableListView.getPackedPositionType(packedPosition) != ExpandableListView.PACKED_POSITION_TYPE_NULL;
    }

}
