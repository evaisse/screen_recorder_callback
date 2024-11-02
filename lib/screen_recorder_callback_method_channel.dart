import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'screen_recorder_callback_platform_interface.dart';

/// An implementation of [ScreenRecorderCallbackPlatform] that uses method channels.
class MethodChannelScreenRecorderCallback extends ScreenRecorderCallbackPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('screen_recorder_callback');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
