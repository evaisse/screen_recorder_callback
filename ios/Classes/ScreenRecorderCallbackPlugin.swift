import Flutter
import UIKit

public class ScreenRecorderCallbackPlugin: NSObject, FlutterPlugin, ScreenRecorderControlApi {
    var callbackApi: ScreenRecordingCallbackApiProtocol?
    
    // Listener callback for recording state changes
    public var recordingStateChangeListener: ((Bool) -> Void)?

    private var isRecording = UIScreen.main.isCaptured {
        didSet {
            recordingStateChangeListener?(isRecording)
        }
    }

    public static func register(with registrar: FlutterPluginRegistrar) {
        let instance = ScreenRecorderCallbackPlugin()
        ScreenRecorderControlApiSetup.setUp(binaryMessenger: registrar.messenger(), api: instance)
        instance.callbackApi = ScreenRecordingCallbackApi(binaryMessenger: registrar.messenger())
        instance.startObservingScreenRecording()
    }

    private func startObservingScreenRecording() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(screenCaptureStatusChanged),
            name: UIScreen.capturedDidChangeNotification,
            object: nil
        )
    }

    private func stopObservingScreenRecording() {
        NotificationCenter.default.removeObserver(self, name: UIScreen.capturedDidChangeNotification, object: nil)
    }

    @objc private func screenCaptureStatusChanged() {
        DispatchQueue.main.async {
            self.isRecording = UIScreen.main.isCaptured
            self.notifyFlutterOfStateChange()
        }
    }

    func notifyFlutterOfStateChange() {
        let state = ScreenRecordingState(isRecording: isRecording)
        print("Notifying Flutter of recording state change: \(isRecording)")
        callbackApi?.onScreenRecordingChange(state: state) { result in
            switch result {
            case .success:
                print("Successfully sent recording state \(self.isRecording) to Flutter")
            case .failure(let error):
                print("Error sending recording state to Flutter: \(error.localizedDescription)")
            }
        }
    }

    public func startListening() throws {
        isRecording = UIScreen.main.isCaptured
        notifyFlutterOfStateChange()
    }

    public func stopListening() throws {
        stopObservingScreenRecording()
        isRecording = false
    }

    deinit {
        stopObservingScreenRecording()
    }
}
