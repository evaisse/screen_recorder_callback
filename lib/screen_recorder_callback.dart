import 'dart:async';

import 'package:screen_recorder_callback/screen_recorder_api.dart';

class ScreenRecorderCallback extends ScreenRecordingCallbackApi {
  final ScreenRecorderControlApi _controlApi;
  final StreamController<bool> _controller;

  // Expose a stream for Flutter to listen to recording changes
  Stream<bool> get onScreenRecordingChangeStream => _controller.stream;

  // Constructor allows optional injection of controlApi for testing, with setup included
  ScreenRecorderCallback({ScreenRecorderControlApi? controlApi})
      : _controlApi = controlApi ?? ScreenRecorderControlApi(),
        _controller = StreamController<bool>.broadcast() {
    ScreenRecordingCallbackApi.setUp(this);
  }

  // Called by the native side to notify of screen recording state changes
  @override
  void onScreenRecordingChange(ScreenRecordingState state) {
    if (!_controller.isClosed) {
      _controller.add(state.isRecording); // Add the new state to the stream
    }
  }

  // Starts listening for screen recording changes
  Future<void> startListening() async {
    try {
      await _controlApi.startListening();
    } catch (e) {
      _controller.addError("Error starting listener: $e");
    }
  }

  // Stops listening and closes the stream
  Future<void> stopListening() async {
    try {
      await _controlApi.stopListening();
    } catch (e) {
      _controller.addError("Error stopping listener: $e");
    } finally {
      await _controller.close(); // Close the stream
    }
  }
}
