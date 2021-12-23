package com.zaelani.submission3.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.zaelani.submission3.database.Favorite
import com.zaelani.submission3.repository.FavoriteRepository

class AddDeleteFavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
}
