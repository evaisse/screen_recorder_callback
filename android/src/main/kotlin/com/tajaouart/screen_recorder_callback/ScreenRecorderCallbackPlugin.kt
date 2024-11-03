package com.tajaouart.screen_recorder_callback

import android.os.Build
import android.util.Log
import android.view.WindowManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import java.util.function.Consumer

/**
 * ScreenRecorderCallbackPlugin
 *
 * A Flutter plugin to detect screen recording events and notify the Flutter layer of state changes.
 */
class ScreenRecorderCallbackPlugin : FlutterPlugin, ActivityAware,
    ScreenRecorderCallbackApi.ScreenRecorderControlApi {


    private var recordingCallbackApi: ScreenRecorderCallbackApi.ScreenRecordingCallbackApi? =
        null
    private var activityBinding: ActivityPluginBinding? = null

    // Callback to handle screen recording state change
    private var recordingCallback: Consumer<Int>? = null
    private var windowManager: WindowManager? = null

    /**
     * Called when the plugin is attached to the Flutter engine.
     * Initializes the necessary resources and sets up the callback API.
     */
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        // Set up Pigeon APIs for communication between Flutter and native Android
        ScreenRecorderCallbackApi.ScreenRecorderControlApi.setUp(
            flutterPluginBinding.binaryMessenger,
            this
        )
        recordingCallbackApi =
            ScreenRecorderCallbackApi.ScreenRecordingCallbackApi(flutterPluginBinding.binaryMessenger)

        // Initialize window manager if the Android version supports screen recording detection
        if (Build.VERSION.SDK_INT >= 34) {
            windowManager =
                flutterPluginBinding.applicationContext.getSystemService(WindowManager::class.java)
            initializeScreenRecordingCallback()
        } else {
            Log.w(
                "ScreenRecorderCallback",
                "Screen recording detection not supported on this Android version."
            )
        }
    }

    /**
     * Initializes the screen recording callback to detect and handle screen recording state changes.
     */
    private fun initializeScreenRecordingCallback() {
        recordingCallback = Consumer { state ->
            // Only proceed if the Android version supports screen recording detection
            if (Build.VERSION.SDK_INT >= 35) {
                val isRecording = (state == WindowManager.SCREEN_RECORDING_STATE_VISIBLE)
                val screenRecordingState = ScreenRecorderCallbackApi.ScreenRecordingState.Builder()
                    .setIsRecording(isRecording)
                    .build()

                // Notify Flutter of the recording state change
                recordingCallbackApi?.onScreenRecordingChange(
                    screenRecordingState,
                    object : ScreenRecorderCallbackApi.VoidResult {
                        override fun success() {
                            Log.d(
                                "ScreenRecorderCallback",
                                "Recording state successfully sent to Flutter."
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

    /**
     * Starts listening for screen recording state changes and immediately sends the initial state to Flutter.
     */
    override fun startListening() {
        if (Build.VERSION.SDK_INT >= 35) {
            recordingCallback?.let { callback ->
                try {
                    val executor = activityBinding?.activity?.mainExecutor
                    if (executor != null) {
                        val initialState =
                            windowManager?.addScreenRecordingCallback(executor, callback)
                        callback.accept(
                            initialState ?: WindowManager.SCREEN_RECORDING_STATE_NOT_VISIBLE
                        )

                        // Send the initial state to Flutter as soon as listening starts
                        sendInitialStateToFlutter(initialState == WindowManager.SCREEN_RECORDING_STATE_VISIBLE)
                    } else {
                        Log.e(
                            "ScreenRecorderCallback",
                            "Main executor is null, cannot add callback."
                        )
                    }
                } catch (e: Exception) {
                    Log.e(
                        "ScreenRecorderCallback",
                        "Error starting screen recording callback: ${e.message}",
                        e
                    )
                }
            }
        } else {
            Log.w(
                "ScreenRecorderCallback",
                "Screen recording callback requires Android 15 or higher."
            )
        }
    }

    /**
     * Sends the initial recording state to Flutter to provide an immediate state on start.
     * @param isRecording Boolean indicating if the screen recording is active.
     */
    private fun sendInitialStateToFlutter(isRecording: Boolean) {
        val screenRecordingState = ScreenRecorderCallbackApi.ScreenRecordingState.Builder()
            .setIsRecording(isRecording)
            .build()
        recordingCallbackApi?.onScreenRecordingChange(
            screenRecordingState,
            object : ScreenRecorderCallbackApi.VoidResult {
                override fun success() {
                    Log.d("ScreenRecorderCallback", "Initial recording state sent successfully.")
                }

                override fun error(error: Throwable) {
                    Log.e(
                        "ScreenRecorderCallback",
                        "Error sending initial recording state: ${error.message}",
                        error
                    )
                }
            })
    }

    /**
     * Stops listening to screen recording state changes.
     */
    override fun stopListening() {
        if (Build.VERSION.SDK_INT >= 35) {
            recordingCallback?.let {
                try {
                    windowManager?.removeScreenRecordingCallback(it)
                } catch (e: Exception) {
                    Log.e(
                        "ScreenRecorderCallback",
                        "Error removing screen recording callback: ${e.message}",
                        e
                    )
                }
            }
        } else {
            Log.w(
                "ScreenRecorderCallback",
                "Stop listening called but requires Android 15 or higher."
            )
        }
    }

    /**
     * Cleans up resources and resets variables when the plugin is detached from the Flutter engine.
     */
    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        ScreenRecorderCallbackApi.ScreenRecorderControlApi.setUp(binding.binaryMessenger, null)
        recordingCallback = null
        windowManager = null
        recordingCallbackApi = null
    }

    // ActivityAware interface implementations
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