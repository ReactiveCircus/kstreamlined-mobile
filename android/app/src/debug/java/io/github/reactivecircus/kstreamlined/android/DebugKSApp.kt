package io.github.reactivecircus.kstreamlined.android

import leakcanary.AppWatcher

class DebugKSApp : KSApp() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.ENABLE_LEAK_CANARY) {
            AppWatcher.manualInstall(this)
        }
    }

    override fun initializeKermit() {
        // TODO
    }
}
