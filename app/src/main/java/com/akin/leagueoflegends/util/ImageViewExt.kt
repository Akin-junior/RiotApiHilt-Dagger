package com.akin.leagueoflegends.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.akin.leagueoflegends.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadInt(imgUrl: Int) {
    Glide.with(context)
        .load(imgUrl)
        .circleCrop()
        .into(this)

}

fun ImageView.loadString(imgUrl: String?, placeholder: CircularProgressDrawable) {
    val options = RequestOptions().placeholder(placeholder).error(R.mipmap.ic_launcher_round)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(imgUrl)
        .fitCenter()
        .centerCrop()
        .into(this)

}

fun ImageView.loadStringForDetailPage(imgUrl: String?, placeholder: CircularProgressDrawable) {
    val options = RequestOptions().placeholder(placeholder).error(R.mipmap.ic_launcher_round)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(imgUrl)
        .transform(RoundedCorners(55))
        .into(this)

}

fun makePlaceHolder(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }

}

