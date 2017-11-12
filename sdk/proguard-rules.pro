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