package com.zaelani.submission3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zaelani.submission3.database.Favorite
import com.zaelani.submission3.databinding.ItemListUsersBinding
import com.zaelani.submission3.helper.FavoriteDiffCallback
import com.zaelani.submission3.ui.DetailUserActivity

class ListFavoriteAdapter : RecyclerView.Adapter<ListFavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorites = ArrayList<Favorite>()
    fun setListFavorites(listFavorites: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorites, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(listFavorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int {
        return listFavorites.size
    }

    inner class FavoriteViewHolder(private val binding: ItemListUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            with(binding) {
                tvItemUsername.text = favorite.username
                Glide.with(itemView.context)
                        .load(favorite.avatar)
                        .apply(RequestOptions().override(55, 55))
                        .into(imgItemPhoto)
                itemView.setOnClickListener {
                    Intent(itemView.context, DetailUserActivity::class.java).also {
                        it.putExtra(DetailUserActivity.EXTRA_USERNAME, favorite.username)
                        it.putExtra(DetailUserActivity.EXTRA_ISFAV, true)
                        itemView.context.startActivity(it)
                    }
                }
            }
        }
    }
}