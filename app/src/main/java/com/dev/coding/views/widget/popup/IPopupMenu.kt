package com.dev.coding.views.widget.popup

import android.annotation.SuppressLint
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes

interface IPopupMenu {
    fun show(onClick: (id: Int) -> Unit)
}

object PopupFactory {
    fun create(view: View, @MenuRes menuRes: Int): IPopupMenu {
        return SimplePopupMenu(view, menuRes)
    }

    @SuppressLint("RestrictedApi")
    fun create(view: View, items: List<IPopupItem>): IPopupMenu {
        val popup = ListPopupWindow(view)
        return object : IPopupMenu {
            override fun show(onClick: (id: Int) -> Unit) {
                popup.show(items) { onClick(it.iconRes) }
            }
        }
    }

    class SimplePopupMenu(view: View, menuRes: Int) : IPopupMenu {
        private val addPopup = PopupMenu(view.context, view)

        init {
            addPopup.inflate(menuRes)
        }

        override fun show(onClick: (id: Int) -> Unit) {
            addPopup.setOnMenuItemClickListener {
                onClick(it.itemId)
                true
            }
            addPopup.show()
        }
    }
}