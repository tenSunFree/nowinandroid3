package com.samples.nowinandroid_demo.di

import android.app.Activity
import android.util.Log
import android.view.Window
import androidx.metrics.performance.JankStats
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.util.concurrent.Executor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@Module
@InstallIn(ActivityComponent::class)
object JankStatsModule {
    @Provides
    fun providesOnFrameListener(): JankStats.OnFrameListener {
        return JankStats.OnFrameListener { frameData ->
            // Make sure to only log janky frames.
            if (frameData.isJank) {
                // We're currently logging this but would better report it to a backend.
                Log.v("NiA Jank", frameData.toString())
            }
        }
    }

    @Provides
    fun providesWindow(activity: Activity): Window {
        return activity.window
    }

    @Provides
    fun providesDefaultExecutor(): Executor {
        return Dispatchers.Default.asExecutor()
    }

    @Provides
    fun providesJankStats(
        window: Window,
        executor: Executor,
        frameListener: JankStats.OnFrameListener
    ): JankStats {
        return JankStats.createAndTrack(window, executor, frameListener)
    }
}
