package com.ozhan.mustafa.pokemongoeye.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import com.ozhan.mustafa.pokemongoeye.R
import kotlinx.android.synthetic.main.activity_browser.*


import android.webkit.WebViewClient


class BrowserActivity : AppCompatActivity() {
    companion object {
        val URL = "https://pokemon.gameinfo.io/en/tools/evolution-calculator"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        webView.webViewClient = MyWebClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = false
        webView.loadUrl(URL)


    }

    @Suppress("OverridingDeprecatedMember")
    class MyWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            true
        } else
            super.onKeyUp(keyCode, event)
    }

}
