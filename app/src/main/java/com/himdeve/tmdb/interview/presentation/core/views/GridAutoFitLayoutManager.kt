package com.himdeve.tmdb.interview.presentation.core.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.himdeve.tmdb.interview.R
import kotlin.math.max

// https://stackoverflow.com/a/30256880/538284
// https://stackoverflow.com/a/42241730/538284
// https://stackoverflow.com/a/38082715/538284

/**
 * Created by Himdeve on 9/29/2020.
 */

// -> <dimen name="grid_column_width">120dp</dimen>
private const val ASSUMED_COLUMN_WIDTH_IN_DP = 120

class GridAutoFitLayoutManager : GridLayoutManager {
    private var mColumnWidth = 0
    private var mColumnWidthChanged = true
    private var mWidthChanged = true
    private var mWidth = 0

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    // span count will be ignored here
    constructor(context: Context?, spanCount: Int) : super(context, spanCount) {
        init(context)
    }

    // span count will be ignored here
    constructor(
        context: Context?,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout) {
        init(context)
    }

    // span count will be ignored here
    constructor(
        context: Context?,
        orientation: Int,
        columnWidthInDp: Int
    ) : super(context, 1, orientation, false) {
        updateColumnWidth(columnWidthInDp)
    }

    private fun init(context: Context?) {
        setColumnWidth(context)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        val width = width
        val height = height
        if (width != mWidth) {
            mWidthChanged = true
            mWidth = width
        }
        if (mColumnWidthChanged && mColumnWidth > 0 && width > 0 && height > 0
            || mWidthChanged
        ) {
            val totalSpace: Int = if (orientation == VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = max(1, totalSpace / mColumnWidth)
            setSpanCount(spanCount)
            mColumnWidthChanged = false
            mWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }

    private fun setColumnWidth(context: Context?) {
        val newColumnWidthInPx =
            context?.resources?.getDimension(R.dimen.grid_column_x)?.toInt()
                ?: ASSUMED_COLUMN_WIDTH_IN_DP

        updateColumnWidth(newColumnWidthInPx)
    }

    private fun updateColumnWidth(widthInDp: Int) {
        if (widthInDp > 0 && widthInDp != mColumnWidth) {
            mColumnWidth = widthInDp
            mColumnWidthChanged = true
        }
    }
}