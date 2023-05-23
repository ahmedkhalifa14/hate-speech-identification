package com.example.nlp.repo

import com.example.nlp.data.ModelApi
import com.example.nlp.models.Input
import com.example.nlp.models.Response
import com.example.nlp.utils.Resource
import com.example.nlp.utils.Utils.tryCatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepoImpl
@Inject constructor(private val apiService: ModelApi) : ApiRepo {
    override suspend fun postNewData(input: Input): Resource<Response> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val result = apiService.postNewData(input)
                Resource.Success(result)
            }
        }
}