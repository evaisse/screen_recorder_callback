/*
import 'package:flutter_test/flutter_test.dart';
import 'package:screen_recorder_callback/screen_recorder_callback.dart';
import 'package:screen_recorder_callback/screen_recorder_callback_platform_interface.dart';
import 'package:screen_recorder_callback/screen_recorder_callback_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockScreenRecorderCallbackPlatform
    with MockPlatformInterfaceMixin
    implements ScreenRecorderCallbackPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ScreenRecorderCallbackPlatform initialPlatform = ScreenRecorderCallbackPlatform.instance;

  test('$MethodChannelScreenRecorderCallback is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelScreenRecorderCallback>());
  });

  test('getPlatformVersion', () async {
    ScreenRecorderCallback screenRecorderCallbackPlugin = ScreenRecorderCallback();
    MockScreenRecorderCallbackPlatform fakePlatform = MockScreenRecorderCallbackPlatform();
    ScreenRecorderCallbackPlatform.instance = fakePlatform;

    expect(await screenRecorderCallbackPlugin.getPlatformVersion(), '42');
  });
}
*/
