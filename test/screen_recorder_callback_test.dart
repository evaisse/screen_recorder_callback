import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:screen_recorder_callback/screen_recorder_api.dart';
import 'package:screen_recorder_callback/screen_recorder_callback.dart';

class MockScreenRecorderControlApi extends ScreenRecorderControlApi {
  bool isListeningStarted = false;
  bool isListeningStopped = false;

  @override
  Future<void> startListening() async {
    isListeningStarted = true;
  }

  @override
  Future<void> stopListening() async {
    isListeningStopped = true;
  }
}

class ErrorMockScreenRecorderControlApi extends ScreenRecorderControlApi {
  final Exception startError;
  final Exception stopError;

  ErrorMockScreenRecorderControlApi(
      {required this.startError, required this.stopError});

  @override
  Future<void> startListening() async {
    throw startError;
  }

  @override
  Future<void> stopListening() async {
    throw stopError;
  }
}

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  late MockScreenRecorderControlApi mockControlApi;
  late ScreenRecorderCallback screenRecorderCallback;

  setUp(() {
    mockControlApi = MockScreenRecorderControlApi();
    screenRecorderCallback = ScreenRecorderCallback(controlApi: mockControlApi);
  });

  test('startListening calls startListening on control API', () async {
    await screenRecorderCallback.startListening();
    expect(mockControlApi.isListeningStarted, isTrue);
  });

  test('stopListening calls stopListening on control API', () async {
    await screenRecorderCallback.stopListening();
    expect(mockControlApi.isListeningStopped, isTrue);
  });

  test('onScreenRecordingChangeStream emits recording state change', () async {
    bool? isRecording;
    final streamSubscription =
        screenRecorderCallback.onScreenRecordingChangeStream.listen(
      (value) => isRecording = value,
    );

    screenRecorderCallback
        .onScreenRecordingChange(ScreenRecordingState(isRecording: true));

    await Future.delayed(Duration.zero); // Allow the stream to emit
    expect(isRecording, isTrue);

    await streamSubscription.cancel();
  });

  test('startListening adds error to stream on failure', () async {
    final errorApi = ErrorMockScreenRecorderControlApi(
      startError: Exception('Start listener failed'),
      stopError: Exception('Stop listener failed'),
    );
    final recorder = ScreenRecorderCallback(controlApi: errorApi);

    final startErrorCompleter = Completer<String>();
    final subscription = recorder.onScreenRecordingChangeStream.listen(
      null,
      onError: (error) {
        if (!startErrorCompleter.isCompleted) {
          startErrorCompleter.complete(error.toString());
        }
      },
    );

    try {
      await recorder.startListening();
    } catch (_) {}

    expect(await startErrorCompleter.future, contains('Start listener failed'));

    await subscription.cancel();
  });

  test('stopListening adds error to stream on failure', () async {
    final errorApi = ErrorMockScreenRecorderControlApi(
      startError: Exception('Start listener failed'),
      stopError: Exception('Stop listener failed'),
    );
    final recorder = ScreenRecorderCallback(controlApi: errorApi);

    final stopErrorCompleter = Completer<String>();
    final subscription = recorder.onScreenRecordingChangeStream.listen(
      null,
      onError: (error) {
        if (!stopErrorCompleter.isCompleted) {
          stopErrorCompleter.complete(error.toString());
        }
      },
    );

    // Skip startListening and call stopListening directly
    try {
      // Directly call stop to trigger stop error
      await recorder.stopListening();
    } catch (_) {}

    expect(await stopErrorCompleter.future, contains('Stop listener failed'));

    await subscription.cancel();
  });
}
