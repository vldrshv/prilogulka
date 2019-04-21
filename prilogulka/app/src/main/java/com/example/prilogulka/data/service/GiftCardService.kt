package com.example.prilogulka.data.service

import com.example.prilogulka.data.GiftCardK
import retrofit2.Call
import retrofit2.http.GET

interface GiftCardService {
    /**
     * Возвращает пользователя в формате JSON
     */
    @GET("api/v1/gift_cards")
    fun getAllGiftCards() : Call<List<GiftCardK>>
}