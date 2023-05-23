package com.example.nlp.data

import com.example.nlp.models.Input
import com.example.nlp.models.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ModelApi {

    @POST("/hate-speech-detection")
    suspend fun postNewData(
        @Body input: Input
    ): Response

}