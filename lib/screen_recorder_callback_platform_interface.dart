import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'screen_recorder_callback_method_channel.dart';

abstract class ScreenRecorderCallbackPlatform extends PlatformInterface {
  /// Constructs a ScreenRecorderCallbackPlatform.
  ScreenRecorderCallbackPlatform() : super(token: _token);

  static final Object _token = Object();

  static ScreenRecorderCallbackPlatform _instance = MethodChannelScreenRecorderCallback();

  /// The default instance of [ScreenRecorderCallbackPlatform] to use.
  ///
  /// Defaults to [MethodChannelScreenRecorderCallback].
  static ScreenRecorderCallbackPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ScreenRecorderCallbackPlatform] when
  /// they register themselves.
  static set instance(ScreenRecorderCallbackPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
