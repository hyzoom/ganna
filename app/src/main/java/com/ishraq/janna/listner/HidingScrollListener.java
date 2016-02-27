package com.ishraq.janna.listner;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Ahmed Saeed on 12/21/2015.
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_THRESHOLD = 20;

    private int mScrolledDistance = 0;
    private boolean mControlsVisible = true;


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (firstVisibleItem == 0) {
            if (recyclerView.getChildAt(0).getTop() == 0) {
                swipeRefresh(true);
            }
            else {
                swipeRefresh(false);
            }

            if(!mControlsVisible) {
                onShow();
                mControlsVisible = true;
            }
        } else {
            swipeRefresh(false);
            if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                onHide();
                mControlsVisible = false;
                mScrolledDistance = 0;
            } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                onShow();
                mControlsVisible = true;
                mScrolledDistance = 0;
            }
        }
        if((mControlsVisible && dy>0) || (!mControlsVisible && dy<0)) {
            mScrolledDistance += dy;
        }

//        if(lastVisibleItem == recyclerView.getLayoutManager().getItemCount()-1){
//            onLoadMore();
//        }

        if(dy > 0) //check for scroll down
        {
            visibleItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getChildCount();
            totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
            pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            if (loading)
            {
                if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                {
                    onLoadMore();
                }
            }
        }

    }


    public abstract void onHide();
    public abstract void onShow();
    public abstract void swipeRefresh(boolean state);
    public abstract void onLoadMore();
}
