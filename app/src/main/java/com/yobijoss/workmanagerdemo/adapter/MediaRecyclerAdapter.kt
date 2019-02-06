package com.yobijoss.workmanagerdemo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.yobijoss.workmanagerdemo.R

class MediaRecyclerAdapter(private val uriMediaList: ArrayList<Uri>) :
    RecyclerView.Adapter<MediaRecyclerAdapter.DraweeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraweeViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return DraweeViewHolder(v)
    }

    override fun onBindViewHolder(holder: DraweeViewHolder, position: Int) {
        holder.bind(uriMediaList[position])
    }

    override fun getItemCount(): Int = uriMediaList.size

    class DraweeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(uri: Uri) {
            val image = itemView.findViewById<SimpleDraweeView>(R.id.ic_image)

            val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.forceRotation(RotationOptions.NO_ROTATION))
                .build()
            val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(image.controller)
                .setImageRequest(request)
                .build()
            image.controller = controller
        }
    }
}