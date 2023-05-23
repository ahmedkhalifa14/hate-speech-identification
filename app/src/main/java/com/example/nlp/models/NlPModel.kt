package com.example.nlp.models

import android.content.res.AssetManager
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder

class NlpModel(private val assetManager: AssetManager) {
    private val inputSize = 75
    private val numClasses = 3
    private val interpreter: Interpreter

    init {
        val tfliteModel = loadModelFile()
//        val options = Interpreter.Options()
//        interpreter = Interpreter(tfliteModel, options)

        val options = Interpreter.Options()
        //options.setAllowFp16PrecisionForFp32(true)
       // options.setAllowBufferHandleOutput(true)
        //options.setUseNNAPI(false)
        options.setNumThreads(4)
        interpreter = Interpreter(tfliteModel, options)
    }

    private fun loadModelFile(): ByteBuffer {
        val modelFileDescriptor = assetManager.openFd("nlp.tflite")
        val inputStream = modelFileDescriptor.createInputStream()
        val modelData = inputStream.readBytes()
        inputStream.close()

        val buffer = ByteBuffer.allocateDirect(modelData.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(modelData)
        buffer.rewind()
        return buffer
    }

    private fun preprocessInputData(inputText: String): FloatArray {
        val vocabulary = loadVocabularyFile()
        val inputTokens = inputText.trim().toLowerCase().split(" ")

        val inputData = FloatArray(inputSize) { 0.0f }
        for (i in inputTokens.indices) {
            if (i >= inputSize) {
                break
            }
            val token = inputTokens[i]
            val tokenIndex = vocabulary.indexOf(token)
            if (tokenIndex != -1) {
                inputData[i] = tokenIndex.toFloat()
            }
        }
        return inputData
    }


    private fun loadVocabularyFile(): List<String> {
        val vocabularyFile = assetManager.open("output.txt")
        val vocabularyReader = BufferedReader(InputStreamReader(vocabularyFile))
        val vocabulary = mutableListOf<String>()
        var line: String? = vocabularyReader.readLine()
        while (line != null) {
            vocabulary.add(line)
            line = vocabularyReader.readLine()
        }
        vocabularyReader.close()
        return vocabulary
    }

    private fun getOutputClassIndex(outputData: Array<FloatArray>): Int {
        var maxIndex = 0
        var maxProbability = outputData[0][0]
        for (i in 1 until numClasses) {
            if (outputData[0][i] > maxProbability) {
                maxIndex = i
                maxProbability = outputData[0][i]
            }
        }
        return maxIndex
    }

    private fun getClassLabel(classIndex: Int): String {
        return when (classIndex) {
            0 -> "Hate"
            1 -> "Lang"
            else -> "Neither"
        }
    }

    fun runModel(inputText: String): String {
        val inputData = preprocessInputData(inputText)
        val outputData = Array(1) { FloatArray(numClasses) }
        interpreter.run(inputData, outputData)
        val outputClassIndex = getOutputClassIndex(outputData)
        return getClassLabel(outputClassIndex)
    }
}
