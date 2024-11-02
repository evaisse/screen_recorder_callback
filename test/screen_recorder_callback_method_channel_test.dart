import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:screen_recorder_callback/screen_recorder_callback_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelScreenRecorderCallback platform = MethodChannelScreenRecorderCallback();
  const MethodChannel channel = MethodChannel('screen_recorder_callback');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
