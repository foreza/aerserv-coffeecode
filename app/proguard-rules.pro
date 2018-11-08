-dontpreverify

-repackageclasses 

-allowaccessmodification

-optimizations !code/simplification/arithmetic

-keepattributes *Annotation*

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class android.support.v7.** {
    public protected *;
}

-dontwarn com.google.android.gms.**
-dontwarn javax.xml.**

# AerServ SDK
-keep class com.aerserv.** { *; }
-keepclassmembers class com.aerserv.** { *; }

# For Moat
-dontwarn com.moat.**

# For Adcolony
-dontwarn android.webkit.**
-dontwarn com.adcolony.**
-keep class com.adcolony.sdk.* { *; }

# For Admob by Google
-keep class com.google.android.gms.ads.** { *; }

# For Applovin
-dontwarn com.applovin.**
-keep class com.applovin.** { *; }

# For AppNext
-keep class com.appnext.** { *; }
-dontwarn com.appnext.**

# For Amazon Publisher Services
-dontwarn com.amazon.device.ads.**
-keep class com.amazon.device.ads.** { *; }

# For Chartboost
-dontwarn com.chartboost.sdk.**
-keep class com.chartboost.sdk.** { *; }

# For Audience Network by Facebook
-dontwarn com.facebook.ads.**
-keep class com.facebook.ads.** { *; }

# For InMobi
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.inmobi.**
-keep class com.inmobi.** { *; }

# For Millennial Media by AOL
-dontwarn com.millennialmedia.**
-keep class com.millennialmedia.** { *; }

# For Mopub by Twitter
-dontwarn com.mopub.**
-keep class com.mopub.** { *; }

# For RhythmOne
-dontwarn com.rhythmone.ad.sdk.**
-keep class com.rhythmone.ad.sdk.** { *; }

# For Tremor
-dontwarn com.tremorvideo.sdk.**
-keep class com.tremorvideo.sdk.android.videoad.** { *; }
-dontwarn com.doubleverify.dvsdk.termor.**
-keep class com.doubleverify.dvsdk.termor.** { *; }

# For Unity Ads
-dontwarn com.unity3d.ads.**
-keep class com.unity3d.ads.** { *; }

# For Vungle
-dontwarn com.vungle.publisher.**
-keep class com.vungle.publisher.** { *; }

# For Flurry by Yahoo!
-dontwarn android.support.customtabs.**
-dontwarn com.flurry.android.**
-keep class com.flurry.** { *; }

# For MyTarget		
-dontwarn com.my.target.**		
-keep class com.my.target.** { *; }			