package com.example.nlp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nlp.databinding.ActivityMainBinding
import com.example.nlp.models.Input
import com.example.nlp.ui.NLPViewModel
import com.example.nlp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // private lateinit var nlpModel: NlpModel
    private val nlpViewModel: NLPViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
//        try {
//            nlpModel= NlpModel(applicationContext.assets)
//            Toast.makeText(this,  nlpModel.runModel("fuck you"), Toast.LENGTH_LONG).show()
//            Log.d("tryhere",nlpModel.runModel("fuck you"))
//        } catch (ex: Exception){
//            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
//            Log.d("here",ex.message.toString())
//            ex.printStackTrace()
//        }
        binding.predictBtn.setOnClickListener {
            val text = binding.inputEt.text.toString()
            nlpViewModel.predict(Input(text))
        }

        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                nlpViewModel.responseState.collect(
                    EventObserver(
                        onLoading = {

                        },
                        onSuccess = { prediction ->
                            binding.predictionTv.text = prediction.prediction
                        },
                        onError = { error ->
                            Log.d("error", error)
                            binding.predictionTv.text = error
                        }
                    )
                )
            }
        }

    }


}