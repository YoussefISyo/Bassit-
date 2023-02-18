package com.optim.bassit;


import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;


public abstract class PagedScrollView implements ViewTreeObserver.OnScrollChangedListener {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 2;
    // The current offset index of data you have loaded
    private int currentPage = 1;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 1;


    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    private final LinearLayoutManager mLayoutManager;
    ScrollView scroll;

    public PagedScrollView(ScrollView scroll, LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        this.scroll = scroll;
    }

    @Override
    public void onScrollChanged() {
        int scrollY = scroll.getScrollY(); // For ScrollView
        int scrollX = scroll.getScrollX(); // For HorizontalScrollView

        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();


        lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }


        View view = (View) scroll.getChildAt(scroll.getChildCount() - 1);
        int diff = (view.getBottom() - (scroll.getHeight() + scroll.getScrollY()));

        if (loading)
            return;

        // if diff is zero, then the bottom has been reached
        if (diff < 300) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }


    }

    // Call this method whenever performing new searches
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount);
}
