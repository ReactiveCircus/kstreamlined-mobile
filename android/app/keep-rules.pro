# Remove Compose tracing strings
-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
   boolean isTraceInProgress();
   void traceEventStart(int,int,int,java.lang.String);
   void traceEventEnd();
}

# TODO: remove once https://issuetracker.google.com/issues/530633707 is fixed.
-keep,allowobfuscation,allowshrinking class com.fleeksoft.ksoup.nodes.** { *; }
