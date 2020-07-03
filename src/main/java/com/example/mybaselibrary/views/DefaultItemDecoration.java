//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.mybaselibrary.views;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mDividerWidth;
    private int mDividerHeight;
    private List<Integer> mViewTypeList;

    public DefaultItemDecoration(@ColorInt int color) {
        this(color, 2, 2, -1);
    }

    public DefaultItemDecoration(@ColorInt int color, int dividerWidth, int dividerHeight, int... excludeViewType) {
        this.mViewTypeList = new ArrayList();
        this.mDivider = new ColorDrawable(color);
        this.mDividerWidth = dividerWidth;
        this.mDividerHeight = dividerHeight;
        int[] var5 = excludeViewType;
        int var6 = excludeViewType.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            int i = var5[var7];
            this.mViewTypeList.add(i);
        }

    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position >= 0) {
            if (this.mViewTypeList.contains(parent.getAdapter().getItemViewType(position))) {
                outRect.set(0, 0, 0, 0);
            } else {
                int columnCount = this.getSpanCount(parent);
                int childCount = parent.getAdapter().getItemCount();
                boolean firstRaw = this.isFirstRaw(position, columnCount);
                boolean lastRaw = this.isLastRaw(position, columnCount, childCount);
                boolean firstColumn = this.isFirstColumn(position, columnCount);
                boolean lastColumn = this.isLastColumn(position, columnCount);
                if (columnCount == 1) {
                    if (firstRaw) {
                        outRect.set(0, 0, 0, this.mDividerHeight / 2);
                    } else if (lastRaw) {
                        outRect.set(0, this.mDividerHeight / 2, 0, 0);
                    } else {
                        outRect.set(0, this.mDividerHeight / 2, 0, this.mDividerHeight / 2);
                    }
                } else if (firstRaw && firstColumn) {
                    outRect.set(0, 0, this.mDividerWidth / 2, this.mDividerHeight / 2);
                } else if (firstRaw && lastColumn) {
                    outRect.set(this.mDividerWidth / 2, 0, 0, this.mDividerHeight / 2);
                } else if (firstRaw) {
                    outRect.set(this.mDividerWidth / 2, 0, this.mDividerWidth / 2, this.mDividerHeight / 2);
                } else if (lastRaw && firstColumn) {
                    outRect.set(0, this.mDividerHeight / 2, this.mDividerWidth / 2, 0);
                } else if (lastRaw && lastColumn) {
                    outRect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, 0, 0);
                } else if (lastRaw) {
                    outRect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, this.mDividerWidth / 2, 0);
                } else if (firstColumn) {
                    outRect.set(0, this.mDividerHeight / 2, this.mDividerWidth / 2, this.mDividerHeight / 2);
                } else if (lastColumn) {
                    outRect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, 0, this.mDividerHeight / 2);
                } else {
                    outRect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, this.mDividerWidth / 2, this.mDividerHeight / 2);
                }

            }
        }
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager)layoutManager).getSpanCount();
        } else {
            return layoutManager instanceof StaggeredGridLayoutManager ? ((StaggeredGridLayoutManager)layoutManager).getSpanCount() : 1;
        }
    }

    private boolean isFirstRaw(int position, int columnCount) {
        return position < columnCount;
    }

    private boolean isLastRaw(int position, int columnCount, int childCount) {
        if (columnCount == 1) {
            return position + 1 == childCount;
        } else {
            int lastRawItemCount = childCount % columnCount;
            int rawCount = (childCount - lastRawItemCount) / columnCount + (lastRawItemCount > 0 ? 1 : 0);
            int rawPositionJudge = (position + 1) % columnCount;
            int rawPosition;
            if (rawPositionJudge == 0) {
                rawPosition = (position + 1) / columnCount;
                return rawCount == rawPosition;
            } else {
                rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1;
                return rawCount == rawPosition;
            }
        }
    }

    private boolean isFirstColumn(int position, int columnCount) {
        if (columnCount == 1) {
            return true;
        } else {
            return position % columnCount == 0;
        }
    }

    private boolean isLastColumn(int position, int columnCount) {
        if (columnCount == 1) {
            return true;
        } else {
            return (position + 1) % columnCount == 0;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        this.drawHorizontal(c, parent);
        this.drawVertical(c, parent);
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        c.save();
        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            int childPosition = parent.getChildAdapterPosition(child);
            if (childPosition >= 0 && !this.mViewTypeList.contains(parent.getAdapter().getItemViewType(childPosition))) {
                int left = child.getLeft();
                int top = child.getBottom();
                int right = child.getRight();
                int bottom = top + this.mDividerHeight;
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(c);
            }
        }

        c.restore();
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        c.save();
        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            int childPosition = parent.getChildAdapterPosition(child);
            if (childPosition >= 0 && !this.mViewTypeList.contains(parent.getAdapter().getItemViewType(childPosition)) ) {
                int left = child.getRight();
                int top = child.getTop();
                int right = left + this.mDividerWidth;
                int bottom = child.getBottom();
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(c);
            }
        }

        c.restore();
    }
}
