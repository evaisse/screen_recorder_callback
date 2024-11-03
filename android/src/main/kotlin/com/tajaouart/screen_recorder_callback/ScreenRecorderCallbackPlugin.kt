package com.tajaouart.screen_recorder_callback

import android.os.Build
import android.util.Log
import android.view.WindowManager
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import java.util.function.Consumer

/** ScreenRecorderCallbackPlugin */
class ScreenRecorderCallbackPlugin : FlutterPlugin, ActivityAware,
    ScreenRecorderCallbackApi.ScreenRecorderControlApi {

    private var screenRecordingCallback: Consumer<Int>? = null
    private var windowManager: WindowManager? = null
    private var activityBinding: ActivityPluginBinding? = null
    private var screenRecordingCallbackApi: ScreenRecorderCallbackApi.ScreenRecordingCallbackApi? =
        null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        // Set up Pigeon APIs to handle messages from Flutter
        ScreenRecorderCallbackApi.ScreenRecorderControlApi.setUp(
            flutterPluginBinding.binaryMessenger,
            this
        )
        screenRecordingCallbackApi =
            ScreenRecorderCallbackApi.ScreenRecordingCallbackApi(flutterPluginBinding.binaryMessenger)

        if (Build.VERSION.SDK_INT >= 34) { // Android 15+
            windowManager =
                flutterPluginBinding.applicationContext.getSystemService(WindowManager::class.java)
            initializeScreenRecordingCallback()
        } else {
            Log.d(
                "ScreenRecorderCallback",
                "Screen recording detection not supported on this version."
            )
        }
    }

    private fun initializeScreenRecordingCallback() {
        screenRecordingCallback = Consumer { state ->
            if (Build.VERSION.SDK_INT >= 35) { // Check Android 15+
                val isRecording = (state == WindowManager.SCREEN_RECORDING_STATE_VISIBLE)
                val screenRecordingState = ScreenRecorderCallbackApi.ScreenRecordingState.Builder()
                    .setIsRecording(isRecording)
                    .build()

                // Notify Flutter of the recording state change
                screenRecordingCallbackApi?.onScreenRecordingChange(
                    screenRecordingState,
                    object : ScreenRecorderCallbackApi.VoidResult {
                        override fun success() {
                            Log.d(
                                "ScreenRecorderCallback",
                                "Successfully sent recording state to Flutter"
                            )
                        }

                        override fun error(error: Throwable) {
                            Log.e(
                                "ScreenRecorderCallback",
                                "Error sending recording state to Flutter: ${error.message}"
                            )
                        }
                    })
            }
        }
    }

    override fun startListening() {
        if (Build.VERSION.SDK_INT >= 35) { // Android 15+
            screenRecordingCallback?.let {
                try {
                    val executor = activityBinding?.activity?.mainExecutor
                    if (executor != null) {
                        val initialState = windowManager?.addScreenRecordingCallback(executor, it)
                        it.accept(initialState ?: WindowManager.SCREEN_RECORDING_STATE_NOT_VISIBLE)
                    } else {
                        Log.e(
                            "ScreenRecorderCallback",
                            "mainExecutor is null; cannot add callback."
                        )
                    }
                } catch (e: Exception) {
                    Log.e("ScreenRecorderCallback", "Error initializing callback: ${e.message}", e)
                }
            }
        }
    }

    override fun stopListening() {
        if (Build.VERSION.SDK_INT >= 35) { // Android 15+
            screenRecordingCallback?.let {
                try {
                    windowManager?.removeScreenRecordingCallback(it)
                } catch (e: Exception) {
                    Log.e("ScreenRecorderCallback", "Error removing callback: ${e.message}", e)
                }
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        ScreenRecorderCallbackApi.ScreenRecorderControlApi.setUp(binding.binaryMessenger, null)
        screenRecordingCallback = null
        windowManager = null
        screenRecordingCallbackApi = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activityBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activityBinding = binding
    }

    override fun onDetachedFromActivity() {
        activityBinding = null
    }
}