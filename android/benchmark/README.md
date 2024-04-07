# Benchmarks and Baseline Profiles

## Baseline and startup profiles generation

To generate new baseline and startup profiles:

Run `./gradlew :app:generateBaselineProfile --no-configuration-cache`.

Generated profiles are located at `android/app/src/main/generated/baselineProfiles/`.

## Startup benchmark

To run startup benchmark:

1. Make sure a physical device with API 28+ is connected.
2. Run `./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest --no-configuration-cache`
3. Results are available at `android/benchmark/build/outputs/androidTest-results/connected/benchmarkRelease/`
4. Run `Import Tests from file` from Android Studio and select `android/benchmark/build/outputs/androidTest-results/connected/benchmarkRelease/test-result.pb`

Benchmark data and perfetto traces are available at `android/benchmark/build/outputs/connected_android_test_additional_output/benchmarkRelease/connected/<device-name>`.
