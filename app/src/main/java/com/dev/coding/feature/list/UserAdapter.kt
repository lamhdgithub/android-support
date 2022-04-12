package com.dev.coding.feature.list

import android.support.core.view.bindingOf
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.coding.databinding.ItemViewUserBinding
import com.dev.coding.extension.onClick
import com.dev.coding.model.ui.IUser
import com.dev.coding.views.widget.SimpleRecyclerAdapter

class UserAdapter(view: RecyclerView) : SimpleRecyclerAdapter<IUser, ItemViewUserBinding>(view) {
		var onItemClickListener: ((IUser) -> Unit)? = null
		
		override fun onCreateBinding(parent: ViewGroup): ItemViewUserBinding {
				return parent.bindingOf(ItemViewUserBinding::inflate)
		}
		
		override fun onBindHolder(item: IUser, binding: ItemViewUserBinding) {
				binding.run {
						root.onClick{
								onItemClickListener?.invoke(item)
						}
						imgAvatar.setImageUrl(item.avatar)
						txtName.text = item.fullName
						txtEmail.text = item.email
						txtPhone.text = item.phone
				}
		}
}
