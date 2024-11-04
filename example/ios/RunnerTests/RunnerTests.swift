import XCTest
@testable import screen_recorder_callback

class MockScreenRecordingCallbackApi: ScreenRecordingCallbackApiProtocol {
    var lastState: ScreenRecordingState?
    var error: PigeonError?

    func onScreenRecordingChange(state: ScreenRecordingState, completion: @escaping (Result<Void, PigeonError>) -> Void) {
        lastState = state
        if let error = error {
            completion(.failure(error))
        } else {
            completion(.success(()))
        }
    }
}

extension ScreenRecorderCallbackPlugin {
    func setMockCallbackApi(_ mockApi: ScreenRecordingCallbackApiProtocol) {
        self.callbackApi = mockApi
    }
}

class ScreenRecorderCallbackPluginTests: XCTestCase {
    var plugin: ScreenRecorderCallbackPlugin!
    var mockCallbackApi: MockScreenRecordingCallbackApi!

    override func setUp() {
        super.setUp()
        plugin = ScreenRecorderCallbackPlugin()
        mockCallbackApi = MockScreenRecordingCallbackApi()
        plugin.setMockCallbackApi(mockCallbackApi) // Inject mock API
    }

    /// Test that the initial recording state is correctly reported
    func testInitialRecordingState() {
        let expectation = self.expectation(description: "Initial recording state callback")

        plugin.recordingStateChangeListener = { isRecording in
            XCTAssertEqual(isRecording, UIScreen.main.isCaptured)
            expectation.fulfill()
        }

        do {
            try plugin.startListening()
        } catch {
            XCTFail("startListening threw an error: \(error)")
        }

        waitForExpectations(timeout: 1, handler: nil)
    }

    /// Test that startListening sends the initial recording state
    func testStartListeningSendsInitialRecordingState() throws {
        let expectation = self.expectation(description: "Start listening sends initial recording state")

        plugin.recordingStateChangeListener = { isRecording in
            XCTAssertEqual(isRecording, UIScreen.main.isCaptured)
            expectation.fulfill()
        }

        try plugin.startListening()
        waitForExpectations(timeout: 1, handler: nil)
    }

    /// Test that stopListening stops observing screen recording state changes
    func testStopListeningStopsObservingRecording() throws {
        let expectation = self.expectation(description: "Stop listening stops observing recording")
        
        var fulfilled = false // Flag to prevent multiple fulfill calls
        plugin.recordingStateChangeListener = { isRecording in
            if !isRecording && !fulfilled {
                expectation.fulfill()
                fulfilled = true
            }
        }

        // Start and then stop listening to simulate a recording state change
        try plugin.startListening()
        try plugin.stopListening()
        
        // Manually trigger the notification to simulate a recording change after stopListening
        NotificationCenter.default.post(name: UIScreen.capturedDidChangeNotification, object: nil)
        
        waitForExpectations(timeout: 1, handler: nil)
    }
}
