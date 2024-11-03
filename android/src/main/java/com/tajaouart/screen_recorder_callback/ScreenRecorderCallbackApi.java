// Autogenerated from Pigeon (v22.6.0), do not edit directly.
// See also: https://pub.dev/packages/pigeon

package com.tajaouart.screen_recorder_callback;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.common.StandardMessageCodec;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Generated class from Pigeon. */
@SuppressWarnings({"unused", "unchecked", "CodeBlock2Expr", "RedundantSuppression", "serial"})
public class ScreenRecorderCallbackApi {

  /** Error class for passing custom error details to Flutter via a thrown PlatformException. */
  public static class FlutterError extends RuntimeException {

    /** The error code. */
    public final String code;

    /** The error details. Must be a datatype supported by the api codec. */
    public final Object details;

    public FlutterError(@NonNull String code, @Nullable String message, @Nullable Object details) 
    {
      super(message);
      this.code = code;
      this.details = details;
    }
  }

  @NonNull
  protected static ArrayList<Object> wrapError(@NonNull Throwable exception) {
    ArrayList<Object> errorList = new ArrayList<>(3);
    if (exception instanceof FlutterError) {
      FlutterError error = (FlutterError) exception;
      errorList.add(error.code);
      errorList.add(error.getMessage());
      errorList.add(error.details);
    } else {
      errorList.add(exception.toString());
      errorList.add(exception.getClass().getSimpleName());
      errorList.add(
        "Cause: " + exception.getCause() + ", Stacktrace: " + Log.getStackTraceString(exception));
    }
    return errorList;
  }

  @NonNull
  protected static FlutterError createConnectionError(@NonNull String channelName) {
    return new FlutterError("channel-error",  "Unable to establish connection on channel: " + channelName + ".", "");
  }

  @Target(METHOD)
  @Retention(CLASS)
  @interface CanIgnoreReturnValue {}

  /** Generated class from Pigeon that represents data sent in messages. */
  public static final class ScreenRecordingState {
    private @NonNull Boolean isRecording;

    public @NonNull Boolean getIsRecording() {
      return isRecording;
    }

    public void setIsRecording(@NonNull Boolean setterArg) {
      if (setterArg == null) {
        throw new IllegalStateException("Nonnull field \"isRecording\" is null.");
      }
      this.isRecording = setterArg;
    }

    /** Constructor is non-public to enforce null safety; use Builder. */
    ScreenRecordingState() {}

    @Override
    public boolean equals(Object o) {
      if (this == o) { return true; }
      if (o == null || getClass() != o.getClass()) { return false; }
      ScreenRecordingState that = (ScreenRecordingState) o;
      return isRecording.equals(that.isRecording);
    }

    @Override
    public int hashCode() {
      return Objects.hash(isRecording);
    }

    public static final class Builder {

      private @Nullable Boolean isRecording;

      @CanIgnoreReturnValue
      public @NonNull Builder setIsRecording(@NonNull Boolean setterArg) {
        this.isRecording = setterArg;
        return this;
      }

      public @NonNull ScreenRecordingState build() {
        ScreenRecordingState pigeonReturn = new ScreenRecordingState();
        pigeonReturn.setIsRecording(isRecording);
        return pigeonReturn;
      }
    }

    @NonNull
    ArrayList<Object> toList() {
      ArrayList<Object> toListResult = new ArrayList<>(1);
      toListResult.add(isRecording);
      return toListResult;
    }

    static @NonNull ScreenRecordingState fromList(@NonNull ArrayList<Object> pigeonVar_list) {
      ScreenRecordingState pigeonResult = new ScreenRecordingState();
      Object isRecording = pigeonVar_list.get(0);
      pigeonResult.setIsRecording((Boolean) isRecording);
      return pigeonResult;
    }
  }

  private static class PigeonCodec extends StandardMessageCodec {
    public static final PigeonCodec INSTANCE = new PigeonCodec();

    private PigeonCodec() {}

    @Override
    protected Object readValueOfType(byte type, @NonNull ByteBuffer buffer) {
      switch (type) {
        case (byte) 129:
          return ScreenRecordingState.fromList((ArrayList<Object>) readValue(buffer));
        default:
          return super.readValueOfType(type, buffer);
      }
    }

    @Override
    protected void writeValue(@NonNull ByteArrayOutputStream stream, Object value) {
      if (value instanceof ScreenRecordingState) {
        stream.write(129);
        writeValue(stream, ((ScreenRecordingState) value).toList());
      } else {
        super.writeValue(stream, value);
      }
    }
  }


  /** Asynchronous error handling return type for non-nullable API method returns. */
  public interface Result<T> {
    /** Success case callback method for handling returns. */
    void success(@NonNull T result);

    /** Failure case callback method for handling errors. */
    void error(@NonNull Throwable error);
  }
  /** Asynchronous error handling return type for nullable API method returns. */
  public interface NullableResult<T> {
    /** Success case callback method for handling returns. */
    void success(@Nullable T result);

    /** Failure case callback method for handling errors. */
    void error(@NonNull Throwable error);
  }
  /** Asynchronous error handling return type for void API method returns. */
  public interface VoidResult {
    /** Success case callback method for handling returns. */
    void success();

    /** Failure case callback method for handling errors. */
    void error(@NonNull Throwable error);
  }
  /** Generated interface from Pigeon that represents a handler of messages from Flutter. */
  public interface ScreenRecorderControlApi {

    void startListening();

    void stopListening();

    /** The codec used by ScreenRecorderControlApi. */
    static @NonNull MessageCodec<Object> getCodec() {
      return PigeonCodec.INSTANCE;
    }
    /**Sets up an instance of `ScreenRecorderControlApi` to handle messages through the `binaryMessenger`. */
    static void setUp(@NonNull BinaryMessenger binaryMessenger, @Nullable ScreenRecorderControlApi api) {
      setUp(binaryMessenger, "", api);
    }
    static void setUp(@NonNull BinaryMessenger binaryMessenger, @NonNull String messageChannelSuffix, @Nullable ScreenRecorderControlApi api) {
      messageChannelSuffix = messageChannelSuffix.isEmpty() ? "" : "." + messageChannelSuffix;
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(
                binaryMessenger, "dev.flutter.pigeon.screen_recorder_callback.ScreenRecorderControlApi.startListening" + messageChannelSuffix, getCodec());
        if (api != null) {
          channel.setMessageHandler(
              (message, reply) -> {
                ArrayList<Object> wrapped = new ArrayList<>();
                try {
                  api.startListening();
                  wrapped.add(0, null);
                }
 catch (Throwable exception) {
                  wrapped = wrapError(exception);
                }
                reply.reply(wrapped);
              });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(
                binaryMessenger, "dev.flutter.pigeon.screen_recorder_callback.ScreenRecorderControlApi.stopListening" + messageChannelSuffix, getCodec());
        if (api != null) {
          channel.setMessageHandler(
              (message, reply) -> {
                ArrayList<Object> wrapped = new ArrayList<>();
                try {
                  api.stopListening();
                  wrapped.add(0, null);
                }
 catch (Throwable exception) {
                  wrapped = wrapError(exception);
                }
                reply.reply(wrapped);
              });
        } else {
          channel.setMessageHandler(null);
        }
      }
    }
  }
  /** Generated class from Pigeon that represents Flutter messages that can be called from Java. */
  public static class ScreenRecordingCallbackApi {
    private final @NonNull BinaryMessenger binaryMessenger;
    private final String messageChannelSuffix;

    public ScreenRecordingCallbackApi(@NonNull BinaryMessenger argBinaryMessenger) {
      this(argBinaryMessenger, "");
    }
    public ScreenRecordingCallbackApi(@NonNull BinaryMessenger argBinaryMessenger, @NonNull String messageChannelSuffix) {
      this.binaryMessenger = argBinaryMessenger;
      this.messageChannelSuffix = messageChannelSuffix.isEmpty() ? "" : "." + messageChannelSuffix;
    }

    /**
     * Public interface for sending reply.
     * The codec used by ScreenRecordingCallbackApi.
     */
    static @NonNull MessageCodec<Object> getCodec() {
      return PigeonCodec.INSTANCE;
    }
    public void onScreenRecordingChange(@NonNull ScreenRecordingState stateArg, @NonNull VoidResult result) {
      final String channelName = "dev.flutter.pigeon.screen_recorder_callback.ScreenRecordingCallbackApi.onScreenRecordingChange" + messageChannelSuffix;
      BasicMessageChannel<Object> channel =
          new BasicMessageChannel<>(
              binaryMessenger, channelName, getCodec());
      channel.send(
          new ArrayList<>(Collections.singletonList(stateArg)),
          channelReply -> {
            if (channelReply instanceof List) {
              List<Object> listReply = (List<Object>) channelReply;
              if (listReply.size() > 1) {
                result.error(new FlutterError((String) listReply.get(0), (String) listReply.get(1), listReply.get(2)));
              } else {
                result.success();
              }
            }  else {
              result.error(createConnectionError(channelName));
            } 
          });
    }
  }
}
