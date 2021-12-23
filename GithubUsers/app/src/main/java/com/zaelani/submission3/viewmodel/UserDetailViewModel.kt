package com.zaelani.submission3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaelani.submission3.model.UserDetailResponse

class UserDetailViewModel : ViewModel() {

    val user = MutableLiveData<UserDetailResponse>()

    fun setUserDetail(userData: UserDetailResponse) {
        user.postValue(userData)
    }

    fun getUserDetail(): LiveData<UserDetailResponse> {
        return user
    }
}