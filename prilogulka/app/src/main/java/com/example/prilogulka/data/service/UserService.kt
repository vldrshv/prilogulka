package com.example.prilogulka.data.service

import com.example.prilogulka.data.userData.User
import com.example.prilogulka.data.userData.UserInfo
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    /**
     * Возвращает пользователя в формате JSON
     */
    @GET("api/v1/users/email?")
    fun getUserByEmail(@Query("email") email: String) : Call<UserInfo>
    /**
    * Возвращает пользователя в формате JSON
    */
    @GET("api/v1/users/{id}")
    fun getUserById(@Path("id") id: Int) : Call<UserInfo>
    
    /**
     * Отправляем пользователя в формате JSON
     */
    @POST("/api/v1/users/")
    fun sendUser(@Body user: User) : Call<UserInfo>
    
    /**
     * Запрос к серверу для "напоминания" пароля на почту
     */
    @GET("/api/v1/users/email_recovery?")
    fun resendPasswordTo(@Query("email") email: String) : Call<UserInfo>
    
    @PATCH("/api/v1/users/{id}")
    fun updateUserInfo(@Body user: User, @Path("id") id: Int) : Call<UserInfo>
}
