---
name: pin-r8-version
description: Pin R8 to a specific published dev release or to a particular commit on R8's `main` branch.
---

## When to use this skill

Use this skill when overriding the R8 version bundled with AGP is necessary.

## Check the current R8 version

To check the current R8 version bundled by AGP:

1. Run the minify task:
   ```bash
   ./gradlew :app:minifyProdReleaseWithR8 --rerun-tasks
   ```
2. Inspect the mapping header:
   ```bash
   head -5 android/app/build/outputs/mapping/prodRelease/mapping.txt
   ```
   Look at `compiler_version` (e.g. `9.4.6-dev`, or `main`) and
   `compiler_hash` (a git commit SHA on R8's repo).

## Locating an R8 version

### Published dev releases (on a release branch)

R8 maintains release branches named `9.0`, `9.1`, …, `9.4`, etc. Each release
branch has commits like `Version 9.4.6-dev`, `Version 9.4.7-dev`, … Every such
tagged version is published to Maven at
`https://storage.googleapis.com/r8-releases/raw` under the coordinate
`com.android.tools:r8:<version>-dev`.

List available branches:

```bash
curl -s "https://r8.googlesource.com/r8/+refs/heads?format=JSON" \
  | tail -c +6 \
  | jq -r 'keys[] | select(test("^[0-9]"))'
```

List recent commits on a specific release branch (replace `9.4`):

```bash
curl -s "https://r8.googlesource.com/r8/+log/refs/heads/9.4?n=30&format=JSON" \
  | tail -c +6 \
  | jq -r '.log[] | "\(.commit[:12]) \(.message | split("\n")[0])"'
```

The `Version X.Y.Z-dev` commits mark released dev versions. Verify a specific
one is actually published (should return HTTP 200):

```bash
curl -sI "https://storage.googleapis.com/r8-releases/raw/com/android/tools/r8/9.4.7-dev/r8-9.4.7-dev.pom" | head -1
```

### Specific commits on `main`

R8 publishes every merged commit on `main` as a raw jar keyed by the **full
40-character commit SHA**. There is no maven POM — only the raw jar file.

List recent commits on `main`:

```bash
curl -s "https://r8.googlesource.com/r8/+log/refs/heads/main?n=30&format=JSON" \
  | tail -c +6 \
  | jq -r '.log[] | "\(.commit) \(.message | split("\n")[0])"'
```

Verify a specific commit's jar is published (should return HTTP 200):

```bash
curl -sI "https://storage.googleapis.com/r8-releases/raw/main/<FULL_40_CHAR_SHA>/r8.jar" | head -1
```

## Applying the pin

Add a `buildscript` block **after** the `dependencyResolutionManagement` block
in the root `settings.gradle.kts`.

### Option A — published dev release (Maven layout)

```kotlin
buildscript {
    repositories {
        maven("https://storage.googleapis.com/r8-releases/raw") {
            content { includeModule("com.android.tools", "r8") }
        }
    }
    dependencies {
        classpath("com.android.tools:r8:9.4.7-dev")
    }
}
```

### Option B — specific commit on `main` (raw jar via ivy)

Main-branch builds don't publish a POM, so an ivy repository with a custom
pattern is required. `[revision]` here is the full 40-character SHA.

```kotlin
buildscript {
    repositories {
        ivy("https://storage.googleapis.com/r8-releases/raw/main") {
            patternLayout {
                artifact("[revision]/[artifact].[ext]")
            }
            metadataSources { artifact() }
            content { includeModule("com.android.tools", "r8") }
        }
    }
    dependencies {
        classpath("com.android.tools:r8:297e591e1842089b281de5d5286760eca4f5c944")
    }
}
```

## Verifying the pin took effect

1. Re-run the minify task:

   ```bash
   ./gradlew :app:minifyProdReleaseWithR8 --rerun-tasks
   ```

2. Inspect the mapping header:

   ```bash
   head -5 android/app/build/outputs/mapping/prodRelease/mapping.txt
   ```

   Confirm both:
   - `compiler_version` — will be `X.Y.Z-dev` for Option A, or literally `main`
     for Option B.
   - `compiler_hash` — matches the release commit (Option A) or the exact SHA
     you pinned (Option B).

## Reference URLs

- R8 git browser: <https://r8.googlesource.com/r8/>
- R8 raw storage bucket root: <https://storage.googleapis.com/r8-releases/raw/>
