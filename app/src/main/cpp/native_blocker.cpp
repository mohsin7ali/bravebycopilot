#include <jni.h>
#include <string>
#include <vector>
#include <algorithm>
#include <cctype>

static std::string toLower(std::string value) {
    std::transform(value.begin(), value.end(), value.begin(),
                   [](unsigned char c) { return static_cast<char>(std::tolower(c)); });
    return value;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_quickbrowser_NativeBlocker_nativeShouldBlock(
    JNIEnv* env,
    jclass,
    jstring url,
    jstring resourceType
) {
    const char* cUrl = env->GetStringUTFChars(url, nullptr);
    const char* cType = env->GetStringUTFChars(resourceType, nullptr);

    if (cUrl == nullptr || cType == nullptr) {
        if (cUrl != nullptr) env->ReleaseStringUTFChars(url, cUrl);
        if (cType != nullptr) env->ReleaseStringUTFChars(resourceType, cType);
        return false;
    }

    std::string urlString(cUrl);
    std::string typeString(cType);

    env->ReleaseStringUTFChars(url, cUrl);
    env->ReleaseStringUTFChars(resourceType, cType);

    urlString = toLower(urlString);
    typeString = toLower(typeString);

    if (typeString == "main_frame" || typeString == "document" || typeString == "sub_frame") {
        return false;
    }

    std::vector<std::string> blockedDomains = {
        "doubleclick.net",
        "googlesyndication.com",
        "google-analytics.com",
        "googletagmanager.com",
        "googletagservices.com",
        "facebook.net",
        "scorecardresearch.com",
        "quantserve.com",
        "moatads.com",
        "adnxs.com",
        "taboola.com",
        "outbrain.com",
        "analytics",
        "tracking",
        "beacon",
        "pixel",
        "adsystem"
    };

    for (const auto& rule : blockedDomains) {
        if (urlString.find(rule) != std::string::npos) {
            return true;
        }
    }

    return false;
}
