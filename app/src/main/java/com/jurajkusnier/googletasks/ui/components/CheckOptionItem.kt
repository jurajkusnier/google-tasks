package com.jurajkusnier.googletasks.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jurajkusnier.googletasks.R
import kotlinx.android.synthetic.main.check_option_item.view.*

class CheckOptionItem: ConstraintLayout {

    private val textView: TextView
    private val imageView:ImageView

    constructor(context: Context,attributeSet: AttributeSet):this(context,attributeSet,R.attr.optionItemStyle)

    constructor(context: Context,attrs: AttributeSet,param:Int):super(context,attrs,param) {

        val localLayout = LayoutInflater.from(context).inflate(R.layout.check_option_item,this)
        textView = localLayout.option_text
        imageView = localLayout.option_check

        val styles = context.theme.obtainStyledAttributes(attrs, R.styleable.CheckOptionItem,param,0)
        try {
            textView.text = styles.getString(R.styleable.CheckOptionItem_text)
        } finally {
            styles.recycle()
        }
    }

    var isChecked:Boolean
        get() = imageView.visibility == View.VISIBLE
        set(value) = if (value) imageView.visibility = View.VISIBLE else imageView.visibility = View.INVISIBLE
}