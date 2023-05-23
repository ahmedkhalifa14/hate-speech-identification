package com.example.nlp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nlp.models.Input
import com.example.nlp.models.Response
import com.example.nlp.repo.ApiRepoImpl
import com.example.nlp.utils.Event
import com.example.nlp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NLPViewModel @Inject constructor(
    private val repo :ApiRepoImpl):ViewModel() {

    private val  _responseState =
        MutableStateFlow<Event<Resource<Response>>>(Event(Resource.Init()))
    val responseState:MutableStateFlow<Event<Resource<Response>>> =
        _responseState
    fun predict(input: Input){
        viewModelScope.launch (Dispatchers.Main){
            _responseState.emit(Event(Resource.Loading()))
            val result =repo.postNewData(input)
            _responseState.emit(Event(result))
        }
    }
}