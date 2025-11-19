# Remove Compose tracing strings
-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
   boolean isTraceInProgress();
   void traceEventStart(int,int,int,java.lang.String);
   void traceEventEnd();
}
