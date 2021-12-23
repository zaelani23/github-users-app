package com.zaelani.submission3.network

import com.zaelani.submission3.model.User
import com.zaelani.submission3.model.UserDetailResponse
import com.zaelani.submission3.model.UserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUser(
            @Query("q") query: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
            @Path("username") username: String?
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
            @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
            @Path("username") username: String
    ): Call<ArrayList<User>>
}