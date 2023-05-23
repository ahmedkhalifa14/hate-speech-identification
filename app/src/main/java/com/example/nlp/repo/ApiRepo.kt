package com.example.nlp.repo

import com.example.nlp.models.Input
import com.example.nlp.models.Response
import com.example.nlp.utils.Resource

interface ApiRepo {
    suspend fun postNewData(input: Input): Resource<Response>
}