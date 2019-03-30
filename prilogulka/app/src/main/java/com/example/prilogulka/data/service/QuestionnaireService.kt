package com.example.prilogulka.data.service

import com.example.prilogulka.data.userData.QuestionnaireInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface QuestionnaireService {
    /**
     * Отправляем пользователя в формате JSON
     */
    @POST("/api/v1/questionnaires")
    fun sendUser(@Body questionnaire: QuestionnaireInfo): Call<QuestionnaireInfo>
    
}