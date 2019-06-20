package com.example.prilogulka.data.service

import com.example.prilogulka.data.GiftCard
import com.example.prilogulka.data.UserGiftCard
import retrofit2.Call
import retrofit2.http.*

interface GiftCardService {
    /**
     * Возвращает список всех карточек
     */
    @GET("api/v1/gift_cards")
    fun getAllGiftCards() : Call<List<GiftCard>>
    /**
     * Возвращает список всех карточек
     */
    @GET("api/v1/gift_cards/{id}")
    fun getGiftCard(@Path("id") cardId: Int) : Call<GiftCard>
    
    /**
     * Возвращает список купленных карт пользователя
     */
    @GET("/api/v1/users_gift_cards")
    fun getBoughtGiftCards(@Query("user_id") userId: Int) : Call<List<UserGiftCard>>

    @PATCH ("/api/v1/users_gift_cards/{id}")
//    fun makeCardUsed(@Body giftCard: UserGiftCard,
    fun makeCardUsed(@Body body: String,
                     @Path("id") id: Int)
    : Call<UserGiftCard>
    
    @GET("/api/v1/users_gift_cards/{id}")
    fun getUsersGiftCard(@Path("id") cardId: Int) : Call<UserGiftCard>
    
    @POST("/api/v1/users_gift_cards/get_gift_card")
    fun buyGiftCard(@QueryMap options: Map<String, String>) : Call<UserGiftCard>
}