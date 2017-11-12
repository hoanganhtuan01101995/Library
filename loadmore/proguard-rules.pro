-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose


-keep class com.hoanganhtuan01101995.loadmore.** { *; }

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