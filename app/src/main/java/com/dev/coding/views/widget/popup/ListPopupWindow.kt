package com.dev.coding.views.widget.popup

import android.annotation.SuppressLint
import android.support.core.view.RecyclerAdapter
import android.support.core.view.RecyclerHolder
import android.support.core.view.bindingOf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev.coding.R
import com.dev.coding.databinding.ItemPopupBinding
import com.dev.coding.extension.onClick

interface IPopupItem {
    val iconRes: Int
    val tintColorRes: Int
    val text: String
}

class PopupItem(
    override val iconRes: Int,
    override val tintColorRes: Int,
    override val text: String
) : IPopupItem

@SuppressLint("InflateParams")
class ListPopupWindow(private val anchor: View) : PopupWindow(anchor.context) {
    private var mOnItemClick: (IPopupItem) -> Unit = {}
    private var mAdapter: Adapter
    private val inflater = LayoutInflater.from(anchor.context)

    init {
        val container = inflater.inflate(R.layout.popup_list, null, false) as ViewGroup

        mAdapter = Adapter()

        (container.findViewById<RecyclerView>(R.id.rvList)).adapter = mAdapter
        setBackgroundDrawable(null)
        isOutsideTouchable = true
        contentView = container
    }

    fun show(items: List<IPopupItem>, onItemClick: (IPopupItem) -> Unit) {
        mOnItemClick = onItemClick
        mAdapter.submit(items)
        super.showAsDropDown(anchor)
    }

    private inner class Adapter : RecyclerAdapter<IPopupItem>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding = parent.bindingOf(ItemPopupBinding::inflate)

            return object : RecyclerHolder<IPopupItem>(binding) {

                @SuppressLint("MissingSuperCall")
                override fun bind(item: IPopupItem) {
                    super.bind(item)
                    val context = binding.root.context
                    val color = ContextCompat.getColorStateList(context, item.tintColorRes)
                    binding.text.text = item.text
                    binding.icon.setImageResource(item.iconRes)
//                    binding.text.setTextColor(color)
                    binding.icon.imageTintList = color
                    itemView.onClick {
                        mOnItemClick(item)
                        dismiss()
                    }
                }
            }
        }
    }
}