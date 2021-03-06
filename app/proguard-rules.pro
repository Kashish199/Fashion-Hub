-dontwarn android.support.v7.**
-dontwarn android.support.v4.**
-dontwarn android.support.design.**
-dontnote sun.misc.Unsafe
-keep class com.google.**
-dontwarn com.google.**
-keep class javax.**
-dontwarn javax.**
-keep class java.**
-dontwarn java.**
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn org.slf4j.**
-dontwarn io.netty.**
-dontwarn org.jetbrains.**
-dontusemixedcaseclassnames
-allowaccessmodification
-dontpreverify
-mergeinterfacesaggressively
-repackageclasses ''
-optimizations class/marking/final, class/unboxing/enum, class/merging/vertical, class/merging/horizontal
-optimizations method/marking/static, method/marking/private, method/marking/final, method/removal/parameter, method/inlining/short, method/inlining/unique
-optimizations code/merging, code/simplification/variable, code/simplification/string, !code/simplification/field, !code/simplification/arithmetic, code/simplification/branch, code/simplification/advanced, code/removal/variable, code/removal/simple, code/removal/advanced, code/removal/exception, code/allocation/variable
-optimizations field/marking/private
-optimizationpasses 5
-keepattributes !LocalVariableTable,!LocalVariableTypeTable
-keepattributes Exceptions, Signature, InnerClasses