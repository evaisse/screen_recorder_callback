
import 'screen_recorder_callback_platform_interface.dart';

class ScreenRecorderCallback {
  Future<String?> getPlatformVersion() {
    return ScreenRecorderCallbackPlatform.instance.getPlatformVersion();
  }
}
