package com.zaelani.submission3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaelani.submission3.model.User

class UsersViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setUser(data : ArrayList<User>) {
        listUser.postValue(data)
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUser
    }
}