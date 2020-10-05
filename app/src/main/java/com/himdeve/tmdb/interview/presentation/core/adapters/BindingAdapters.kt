package com.himdeve.tmdb.interview.presentation.core.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.himdeve.tmdb.interview.R
import com.himdeve.tmdb.interview.application.core.GlideApp
import com.himdeve.tmdb.interview.presentation.core.views.GridAutoFitLayoutManager

/**
 * Created by Himdeve on 9/29/2020.
 */

@BindingAdapter("useAutoFitGridLayoutOrientation")
fun bindOrientationToRecyclerView(
    recyclerView: RecyclerView,
    useAutoFitGridLayoutOrientation: Boolean
) {
    if (useAutoFitGridLayoutOrientation) {
        val isTablet: Boolean = recyclerView.resources.getBoolean(R.bool.isTablet)

        val orientation: Int = when (isTablet) {
            true -> GridLayoutManager.HORIZONTAL
            false -> GridLayoutManager.VERTICAL
        }

        // Depends on orientation, but it is convenient to use an IsTable variable right now
        val columnWidth: Int = when (isTablet) {
            true -> recyclerView.context.resources.getDimension(R.dimen.grid_column_y).toInt()
            false -> recyclerView.context.resources.getDimension(R.dimen.grid_column_x).toInt()
        }

        recyclerView.layoutManager = GridAutoFitLayoutManager(
            recyclerView.context,
            orientation,
            columnWidth
        )
    }
}

// using this attribute, the original layout_width and layout_height will be ignored
@BindingAdapter("useAutoFitConstraintSize")
fun bindSizeToConstraintLayout(
    constraintLayout: ConstraintLayout,
    useAutoFitConstraintSize: Boolean
) {
    if (useAutoFitConstraintSize) {
        val isTablet: Boolean = constraintLayout.resources.getBoolean(R.bool.isTablet)

        val layoutParams = constraintLayout.layoutParams

        when (isTablet) {
            true -> {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            false -> {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }
}

@BindingAdapter(
    value = ["imageUrl", "baseUrl", "progressBarColorRes", "brokenImageRes"],
    requireAll = false
)
fun bindUrlToImageView(
    imageView: ImageView,
    imageUrl: String?,
    baseUrl: String?,
    progressBarColorRes: Int?,
    brokenImageRes: Int?
) {
    // This nullability check is here because Movie in Movie Detail Fragment
    //  is loaded after it gets data from viewModel
    imageUrl?.let { notNullImageUrl ->
        GlideApp.with(imageView.context).run {
            if (notNullImageUrl.isEmpty()) {
                load(brokenImageRes ?: R.drawable.ic_broken_image)
            } else {
                load(baseUrl.orEmpty() + notNullImageUrl)
                    .placeholder(
                        getCircularProgressDrawable(imageView.context, progressBarColorRes)
                    )
            }
        }.into(imageView)
    }
}

private fun getCircularProgressDrawable(
    context: Context,
    progressBarColorRes: Int?
): CircularProgressDrawable {
    val defaultColorRes = R.color.colorPrimary
    val retCircularProgressDrawable = CircularProgressDrawable(context)

    retCircularProgressDrawable.strokeWidth = 7f
    retCircularProgressDrawable.centerRadius = 40f
    retCircularProgressDrawable.colorFilter =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            ContextCompat.getColor(context, progressBarColorRes ?: defaultColorRes),
            BlendModeCompat.SRC_ATOP
        )
    retCircularProgressDrawable.start()

    return retCircularProgressDrawable
}