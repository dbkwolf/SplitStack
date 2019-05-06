package com.example.splitstack.ViewHolders

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.example.splitstack.R

class TitleParentViewHolder(itemView: View) : ParentViewHolder(itemView) {


    var textView: TextView
    var imageView: ImageView

    init {
        textView = itemView.findViewById<View>(R.id.parentTitle) as TextView
        imageView = itemView.findViewById<View>(R.id.expandArrow) as ImageView

    }
}
