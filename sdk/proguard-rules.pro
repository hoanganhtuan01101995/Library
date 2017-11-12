-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

#Retrofit2
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn retrofit2.Platform$Java8

#Picasso
-dontwarn com.squareup.okhttp.**

#Greendao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
 }
-keep class **$Properties
-dontwarn org.greenrobot.greendao.database.**
-dontwarn rx.**

#Firebase
-keepattributes Signature

-keep class com.hoanganhtuan01101995.sdk.Sdk { *; }

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