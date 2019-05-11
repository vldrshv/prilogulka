package com.example.prilogulka.data.service

import com.example.prilogulka.data.Video
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoService {
    /**
     * Возвращает видео в формате JSON
     */
    @GET("api/v1/videos")
    fun getAllVideos() : Call<List<Video>>
    
    /**
     * возвращает видео по анкете
     */
    @GET("/api/v1/videos/videos_pool?")
    fun getVideosByQuestionnaire(@Query("user_id") userId: Int) : Call<Video>
    
    /**
     * возвращает видео по id
     */
    @GET("/api/v1/videos/{id}")
    fun getVideoById(@Path("id") videoId: Int) : Call<Video>
}
