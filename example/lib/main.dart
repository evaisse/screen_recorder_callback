import 'dart:async';

import 'package:flutter/material.dart';
import 'package:screen_recorder_callback/screen_recorder_callback.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String screenBeingRecorded = 'false';
  final ScreenRecorderCallback _screenRecorderApi = ScreenRecorderCallback();
  StreamSubscription<bool>? _recordingSubscription;

  @override
  void initState() {
    super.initState();
    initScreenRecordingCallback();
  }

  // Initializes the screen recording callback.
  Future<void> initScreenRecordingCallback() async {
    try {
      // Start listening to screen recording changes
      await _screenRecorderApi.startListening();

      // Set up a StreamController for recording state changes
      _recordingSubscription = _screenRecorderApi.onScreenRecordingChangeStream
          .listen((isRecording) {
        setState(() {
          screenBeingRecorded = isRecording.toString();
        });
      });
    } catch (e) {
      setState(() {
        screenBeingRecorded = 'Failed to add callback';
      });
    }
  }

  @override
  void dispose() {
    // Stop listening to screen recording changes when widget is disposed
    _screenRecorderApi.stopListening();
    _recordingSubscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Screen Recorder Callback Demo'),
        ),
        body: Center(
          child: Text('Screen being recorded: $screenBeingRecorded\n'),
        ),
      ),
    );
  }
}
