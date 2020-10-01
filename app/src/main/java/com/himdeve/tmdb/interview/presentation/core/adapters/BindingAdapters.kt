package com.himdeve.tmdb.interview.presentation.core.adapters

import android.content.Context
import android.widget.ImageView
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

        recyclerView.layoutManager = GridAutoFitLayoutManager(
            recyclerView.context,
            1, // will be ignored here
            orientation, false
        )
    }
}

@BindingAdapter(value = ["imageUrl", "baseUrl", "progressBarColorRes"], requireAll = false)
fun bindUrlToImageView(
    imageView: ImageView,
    imageUrl: String?,
    baseUrl: String?,
    progressBarColorRes: Int?
) {
    // This nullability check is here because Movie in Movie Detail Fragment
    //  is loaded after it gets data from viewModel
    imageUrl?.let { notNullImageUrl ->
        GlideApp.with(imageView.context).run {
            if (notNullImageUrl.isEmpty()) {
                load(R.drawable.ic_broken_image)
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