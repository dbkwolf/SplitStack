package com.example.splitstack.ViewHolders

import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
//import com.bignerdranch.expandablerecyclerview.ParentViewHolder
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.example.splitstack.R

class TitleParentViewHolder(itemView: View) : ParentViewHolder(itemView) {


    var textView: TextView
    var imageView: ImageView
    var cardView: CardView

    init {
        cardView = itemView.findViewById<View>(R.id.parentCardView) as CardView
        textView = itemView.findViewById<View>(R.id.parentTitle) as TextView
        imageView = itemView.findViewById<View>(R.id.expandArrow) as ImageView
    }
}
