# Benchmarks and Baseline Profiles

## Baseline and startup profiles generation

To generate new baseline and startup profiles:

Run `./gradlew :app:generateBaselineProfile`.

Generated profiles are located at `android/app/src/main/generated/baselineProfiles/`.

## Benchmarks

To run all benchmarks:

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest
```

Individual benchmarks can also be run from Android Studio or command line:

#### Startup benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.startup.StartupBenchmark
```

#### Home feed scrolling benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.home.HomeFeedScrollingBenchmark
```

#### Home feed recomposition benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.home.HomeFeedRecompositionBenchmark
```

#### Feed selection benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.feedselection.FeedSelectionBenchmark
```

#### Content viewer benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.contentviewer.ContentViewerBenchmark
```

#### Kotlin YouTube video benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.kotlinyoutubevideo.KotlinYouTubeVideoBenchmark
```

#### Talking Kotlin episode benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.talkingkotlinepisode.TalkingKotlinEpisodeBenchmark
```

#### Kotlin Weekly issue benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.kotlinweeklyissue.KotlinWeeklyIssueBenchmark
```

#### TLDR summary benchmark

```
./gradlew :benchmark:connectedBenchmarkReleaseAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=io.github.reactivecircus.kstreamlined.android.benchmark.tldrsummary.TldrSummaryBenchmark
```

### Benchmark results

Results are available at `android/benchmark/build/outputs/androidTest-results/connected/benchmarkRelease/`.

To import results to Android Studio, run `Import Tests from file` and select the `test-result.pb` file.

Benchmark data and perfetto traces are available at `android/benchmark/build/outputs/connected_android_test_additional_output/benchmarkRelease/connected/<device-name>`.
