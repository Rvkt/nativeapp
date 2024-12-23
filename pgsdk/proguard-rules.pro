# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep public class com.softmintindia.pgsdk.** { *; }
-keepclassmembers public class com.softmintindia.pgsdk.** { *; }

# Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlin.** { *; }
-dontwarn androidx.compose.**
-dontwarn androidx.lifecycle.**
-dontwarn kotlin.**
-dontwarn kotlinx.coroutines.**


# Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp3.**
-dontwarn javax.annotation.**

# ZXing
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.** { *; }
-dontwarn com.google.zxing.**
-dontwarn com.journeyapps.**

-keepattributes SourceFile,LineNumberTable

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn org.jetbrains.annotations.**



