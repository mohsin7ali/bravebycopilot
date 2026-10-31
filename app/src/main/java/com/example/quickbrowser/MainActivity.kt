package com.example.quickbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlInput: EditText
    private lateinit var btnBack: Button
    private lateinit var btnForward: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnShield: Button
    private lateinit var shieldLabel: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var blockedCountText: TextView

    private var shieldsEnabled = true
    private var blockedCount = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        urlInput = findViewById(R.id.urlInput)
        btnBack = findViewById(R.id.btnBack)
        btnForward = findViewById(R.id.btnForward)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnShield = findViewById(R.id.btnShield)
        shieldLabel = findViewById(R.id.shieldLabel)
        progressBar = findViewById(R.id.progressBar)
        blockedCountText = findViewById(R.id.blockedCountText)

        setupWebView()
        setupToolbar()
        updateShieldState()

        urlInput.setText(getString(R.string.default_url))
        webView.loadUrl(getString(R.string.default_url))
    }

    private fun setupWebView() {
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.allowFileAccess = false
        settings.allowContentAccess = false
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.loadsImagesAutomatically = true
        settings.supportZoom()
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

        webView.webViewClient = BrowserWebClient(this)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress >= 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupToolbar() {
        btnBack.setOnClickListener {
            if (webView.canGoBack()) webView.goBack()
        }

        btnForward.setOnClickListener {
            if (webView.canGoForward()) webView.goForward()
        }

        btnRefresh.setOnClickListener {
            webView.reload()
        }

        btnShield.setOnClickListener {
            shieldsEnabled = !shieldsEnabled
            updateShieldState()
        }

        urlInput.setOnEditorActionListener { _, _, _ ->
            val raw = urlInput.text.toString().trim()
            val url = normalizeUrl(raw)
            urlInput.setText(url)
            webView.loadUrl(url)
            true
        }
    }

    private fun normalizeUrl(raw: String): String {
        if (raw.isBlank()) return "https://example.com"
        if (raw.startsWith("http://") || raw.startsWith("https://")) {
            return raw
        }
        return if (raw.contains(".")) {
            "https://$raw"
        } else {
            "https://www.google.com/search?q=${raw.replace(" ", "+")}" 
        }
    }

    fun updateAddressBar(url: String) {
        urlInput.setText(url)
    }

    fun updateLoadingState(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun incrementBlocked() {
        blockedCount += 1
        blockedCountText.text = "Blocked: $blockedCount"
    }

    private fun updateShieldState() {
        if (shieldsEnabled) {
            shieldLabel.text = getString(R.string.shield_on)
            btnShield.text = "Shields: ON"
        } else {
            shieldLabel.text = getString(R.string.shield_off)
            btnShield.text = "Shields: OFF"
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
