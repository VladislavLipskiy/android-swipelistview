/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
 *
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.swipelistview;

import android.widget.ListAdapter;

/**
 * Touch listener impl for the SwipeListView
 */
public class SwipeListViewTouchListener extends AbsSwipeViewTouchListener<ListAdapter> {

    /**
     * Constructor
     *
     * @param swipeListView  SwipeListView
     * @param swipeFrontView front view Identifier
     * @param swipeBackView  back view Identifier
     */
    public SwipeListViewTouchListener(final SwipeView<ListAdapter> swipeListView, final int swipeFrontView, final int swipeBackView) {
        super(swipeListView, swipeFrontView, swipeBackView);
    }

    @Override
    protected int getCount() {
        return swipeListView.getMainAdapter().getCount();
    }

    @Override
    protected boolean isSwipeAllowed(final int childPosition) {
        return swipeListView.getMainAdapter().isEnabled(childPosition) && swipeListView.getMainAdapter().getItemViewType(childPosition) >= 0;
    }

}
