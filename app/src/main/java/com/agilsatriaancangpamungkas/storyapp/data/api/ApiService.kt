package com.agilsatriaancangpamungkas.storyapp.data.api

import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseAddStories
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseDetailStory
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseLogin
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseRegister
import com.agilsatriaancangpamungkas.storyapp.data.response.ResponseStoryAll
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register (
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : ResponseRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun login (
        @Field("email") email: String,
        @Field("password") password: String
    ) : ResponseLogin

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ResponseStoryAll

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id:String
    ) : ResponseDetailStory

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat : Float? = null,
        @Part("lon") lon : Float? = null
    ): ResponseAddStories

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): ResponseStoryAll
}