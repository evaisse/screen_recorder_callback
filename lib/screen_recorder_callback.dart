import 'dart:async';

import 'package:screen_recorder_callback/screen_recorder_api.dart';

class ScreenRecorderCallback extends ScreenRecordingCallbackApi {
  final _controller = StreamController<bool>.broadcast();
  final ScreenRecorderControlApi _controlApi = ScreenRecorderControlApi();

  // Expose a stream for Flutter to listen to recording changes
  Stream<bool> get onScreenRecordingChangeStream => _controller.stream;

  ScreenRecorderCallback() {
    // Set up this class as the callback handler
    ScreenRecordingCallbackApi.setUp(this);
  }

  // Called by the native side to notify of screen recording state changes
  @override
  void onScreenRecordingChange(ScreenRecordingState state) {
    _controller.add(state.isRecording); // Add the new state to the stream
  }

  Future<void> startListening() async {
    await _controlApi.startListening(); // Start listening on the native side
  }

  Future<void> stopListening() async {
    await _controlApi.stopListening(); // Stop listening on the native side
    _controller.close(); // Clean up the stream controller if needed
  }
}
