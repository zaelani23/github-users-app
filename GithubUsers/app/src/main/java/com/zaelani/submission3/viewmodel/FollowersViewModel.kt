package com.zaelani.submission3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaelani.submission3.model.User

class FollowersViewModel : ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<User>>()

    fun setListFollowers(data : ArrayList<User>) {
        listFollowers.postValue(data)
    }

    fun getListFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }
}