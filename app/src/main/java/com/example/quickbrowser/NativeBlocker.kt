package com.example.quickbrowser

class NativeBlocker {
    companion object {
        init {
            System.loadLibrary("native_blocker")
        }

        fun shouldBlock(url: String, resourceType: String): Boolean {
            return nativeShouldBlock(url, resourceType)
        }

        private external fun nativeShouldBlock(url: String, resourceType: String): Boolean
    }
}
