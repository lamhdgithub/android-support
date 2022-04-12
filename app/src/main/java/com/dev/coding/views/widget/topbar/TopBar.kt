package com.dev.coding.views.widget.topbar

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dev.coding.R
import com.dev.coding.databinding.TopBarSimpleBinding
import com.dev.coding.databinding.TopBarTextCenterBinding


class SimpleTopBarState(
		@StringRes
		private val title: Int,
		@DrawableRes
		private val iconBack: Int = R.drawable.ic_baseline_arrow_white_24,
		private val hasDivider: Boolean = false,
		private val onBackClick: () -> Unit = {}
) : TopBarState() {
		override val stateBinding by bindingOf(TopBarSimpleBinding::inflate)
		
		override fun doApply() {
				with(stateBinding) {
						txtABTitle.setText(title)
						btnBack.setImageResource(iconBack)
						btnBack.setOnClickListener { onBackClick() }
						divider.visibility = if (hasDivider) View.VISIBLE else View.GONE
				}
		}
}

class TextCenterTopBarState(
		@StringRes
		private val title: Int,
		private val hasDivider: Boolean = false,
) : TopBarState() {
		override val stateBinding by bindingOf(TopBarTextCenterBinding::inflate)
		
		override fun doApply() {
				with(stateBinding) {
						txtABTitle.setText(title)
						divider.visibility = if (hasDivider) View.VISIBLE else View.GONE
				}
		}
}