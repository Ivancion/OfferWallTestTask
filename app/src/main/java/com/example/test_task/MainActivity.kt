package com.example.test_task

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.test_task.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var buttonNext: Button
    private lateinit var textView: TextView
    private lateinit var handler: Handler
    private lateinit var webView: WebView
    private lateinit var imageView: ImageView
    private lateinit var repository: Repository

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        handler = Handler(Looper.getMainLooper())
        webView = findViewById(R.id.webView)
        imageView = findViewById(R.id.imageView)
        buttonNext = findViewById(R.id.buttonNext)

        repository = Repository()

        val coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch {
            val list = repository.getAllIds()
            var currIdPos = list[0]
            var data = repository.getDataById(currIdPos++)
            setDataByType(data)

            buttonNext.setOnClickListener {
                coroutineScope.launch {
                    println(currIdPos)
                    if(currIdPos < list.size) {
                        data = repository.getDataById(currIdPos++)
                        setVisibility()
                        setDataByType(data)
                    }
                    else {
                        currIdPos = list[0]
                        data = repository.getDataById(currIdPos++)
                        setVisibility()
                        setDataByType(data)
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setDataByType(jsonObject: JSONObject) {
        when(jsonObject.get("type")) {
            "text" -> handler.post {
                    textView.visibility = View.VISIBLE
                    textView.text = jsonObject.get("message").toString()
                }
            "webview" -> handler.post {
                webView.visibility = View.VISIBLE
                webView.settings.javaScriptEnabled = true
                webView.loadUrl(jsonObject.get("url").toString())
            }
            "image" -> handler.post {
                imageView.visibility = View.VISIBLE
                Glide.with(imageView)
                    .load(jsonObject.get("url").toString())
                    .centerCrop()
                    .into(imageView)
            }
            "game" -> {}
        }
    }
    private fun setVisibility() {
        handler.post {
            textView.visibility = View.GONE
            webView.visibility = View.GONE
            imageView.visibility = View.GONE
        }
    }
}