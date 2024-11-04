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
  bool isRecording = false;
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
      _recordingSubscription =
          _screenRecorderApi.onScreenRecordingChangeStream.listen((recording) {
        setState(() {
          isRecording = recording;
        });
      });

      await _screenRecorderApi.startListening();
    } catch (e) {
      setState(() {
        isRecording = false; // Show "Not Recording" on error
      });
    }
  }

  @override
  void dispose() {
    _screenRecorderApi.stopListening();
    _recordingSubscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        backgroundColor: isRecording ? Colors.red : Colors.white,
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(
                isRecording ? Icons.videocam : Icons.videocam_off,
                size: 80,
                color: isRecording ? Colors.white : Colors.black54,
              ),
              const SizedBox(height: 20),
              Text(
                isRecording ? 'Recording...' : 'Not Recording',
                style: TextStyle(
                  color: isRecording ? Colors.white : Colors.black54,
                  fontSize: 24,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
