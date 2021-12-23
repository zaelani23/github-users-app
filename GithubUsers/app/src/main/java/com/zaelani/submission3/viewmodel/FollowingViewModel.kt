package com.zaelani.submission3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaelani.submission3.model.User

class FollowingViewModel : ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setListFollowing(data : ArrayList<User>) {
        listFollowing.postValue(data)
    }

    fun getListFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }
}