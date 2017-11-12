-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

-keepattributes Signature

-keep class com.hoanganhtuan01101995.update.Update { *; }

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