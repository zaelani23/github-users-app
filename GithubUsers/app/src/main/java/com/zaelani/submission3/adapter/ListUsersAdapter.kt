package com.zaelani.submission3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.zaelani.submission3.databinding.ItemListUsersBinding
import com.zaelani.submission3.model.User

class ListUsersAdapter : RecyclerView.Adapter<ListUsersAdapter.ListViewHolder>(){

    private val listUser = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ListViewHolder(var binding: ItemListUsersBinding) :
            RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(holder.itemView.context)
                .load(user.avatar_url)
                .apply(RequestOptions().override(55, 55))
                .into(holder.binding.imgItemPhoto)

        holder.binding.tvItemUsername.text = user.login

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(user)

        }

    }

    override fun getItemCount() = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}