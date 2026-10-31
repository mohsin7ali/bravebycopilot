# Keep browser related classes
-keep class com.example.quickbrowser.** { *; }
-keep class androidx.webkit.** { *; }

# Keep native functions
-keepclasseswithmembernames class com.example.quickbrowser.NativeBlocker {
    native <methods>;
}
