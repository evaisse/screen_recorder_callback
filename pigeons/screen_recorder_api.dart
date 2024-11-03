import 'package:pigeon/pigeon.dart';

class ScreenRecordingState {
  bool isRecording;

  ScreenRecordingState({required this.isRecording});
}

@HostApi()
abstract class ScreenRecorderControlApi {
  // Start listening for screen recording changes
  void startListening();

  // Stop listening for screen recording changes
  void stopListening();
}

@FlutterApi()
abstract class ScreenRecordingCallbackApi {
  // Called when screen recording status changes, as a callback from native
  void onScreenRecordingChange(ScreenRecordingState state);
}
