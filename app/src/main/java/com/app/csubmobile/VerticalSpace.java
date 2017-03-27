package com.app.csubmobile;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by qpngd on 3/26/2017.
 */

public class VerticalSpace extends RecyclerView.ItemDecoration {
    int Space;

    public VerticalSpace(int Space) {
        this.Space = Space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = Space;
        outRect.bottom = Space;
        outRect.right = Space;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = Space;
        }
    }
}
