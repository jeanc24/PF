package urlrn;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Servicio
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: UrlRn.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UrlRnGrpc {

  private UrlRnGrpc() {}

  public static final java.lang.String SERVICE_NAME = "urlrn.UrlRn";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlRequest,
      urlrn.UrlRnOuterClass.UrlResponse> getGetUrlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUrl",
      requestType = urlrn.UrlRnOuterClass.UrlRequest.class,
      responseType = urlrn.UrlRnOuterClass.UrlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlRequest,
      urlrn.UrlRnOuterClass.UrlResponse> getGetUrlMethod() {
    io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlRequest, urlrn.UrlRnOuterClass.UrlResponse> getGetUrlMethod;
    if ((getGetUrlMethod = UrlRnGrpc.getGetUrlMethod) == null) {
      synchronized (UrlRnGrpc.class) {
        if ((getGetUrlMethod = UrlRnGrpc.getGetUrlMethod) == null) {
          UrlRnGrpc.getGetUrlMethod = getGetUrlMethod =
              io.grpc.MethodDescriptor.<urlrn.UrlRnOuterClass.UrlRequest, urlrn.UrlRnOuterClass.UrlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUrl"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UrlRnMethodDescriptorSupplier("GetUrl"))
              .build();
        }
      }
    }
    return getGetUrlMethod;
  }

  private static volatile io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlCreateRequest,
      urlrn.UrlRnOuterClass.UrlResponse> getCreateUrlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateUrl",
      requestType = urlrn.UrlRnOuterClass.UrlCreateRequest.class,
      responseType = urlrn.UrlRnOuterClass.UrlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlCreateRequest,
      urlrn.UrlRnOuterClass.UrlResponse> getCreateUrlMethod() {
    io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlCreateRequest, urlrn.UrlRnOuterClass.UrlResponse> getCreateUrlMethod;
    if ((getCreateUrlMethod = UrlRnGrpc.getCreateUrlMethod) == null) {
      synchronized (UrlRnGrpc.class) {
        if ((getCreateUrlMethod = UrlRnGrpc.getCreateUrlMethod) == null) {
          UrlRnGrpc.getCreateUrlMethod = getCreateUrlMethod =
              io.grpc.MethodDescriptor.<urlrn.UrlRnOuterClass.UrlCreateRequest, urlrn.UrlRnOuterClass.UrlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateUrl"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlCreateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UrlRnMethodDescriptorSupplier("CreateUrl"))
              .build();
        }
      }
    }
    return getCreateUrlMethod;
  }

  private static volatile io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlUpdateRequest,
      urlrn.UrlRnOuterClass.UrlResponse> getUpdateUrlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateUrl",
      requestType = urlrn.UrlRnOuterClass.UrlUpdateRequest.class,
      responseType = urlrn.UrlRnOuterClass.UrlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlUpdateRequest,
      urlrn.UrlRnOuterClass.UrlResponse> getUpdateUrlMethod() {
    io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlUpdateRequest, urlrn.UrlRnOuterClass.UrlResponse> getUpdateUrlMethod;
    if ((getUpdateUrlMethod = UrlRnGrpc.getUpdateUrlMethod) == null) {
      synchronized (UrlRnGrpc.class) {
        if ((getUpdateUrlMethod = UrlRnGrpc.getUpdateUrlMethod) == null) {
          UrlRnGrpc.getUpdateUrlMethod = getUpdateUrlMethod =
              io.grpc.MethodDescriptor.<urlrn.UrlRnOuterClass.UrlUpdateRequest, urlrn.UrlRnOuterClass.UrlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateUrl"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlUpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UrlRnMethodDescriptorSupplier("UpdateUrl"))
              .build();
        }
      }
    }
    return getUpdateUrlMethod;
  }

  private static volatile io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlBorrado,
      urlrn.UrlRnOuterClass.UrlBorrado> getDeleteUrlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteUrl",
      requestType = urlrn.UrlRnOuterClass.UrlBorrado.class,
      responseType = urlrn.UrlRnOuterClass.UrlBorrado.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlBorrado,
      urlrn.UrlRnOuterClass.UrlBorrado> getDeleteUrlMethod() {
    io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.UrlBorrado, urlrn.UrlRnOuterClass.UrlBorrado> getDeleteUrlMethod;
    if ((getDeleteUrlMethod = UrlRnGrpc.getDeleteUrlMethod) == null) {
      synchronized (UrlRnGrpc.class) {
        if ((getDeleteUrlMethod = UrlRnGrpc.getDeleteUrlMethod) == null) {
          UrlRnGrpc.getDeleteUrlMethod = getDeleteUrlMethod =
              io.grpc.MethodDescriptor.<urlrn.UrlRnOuterClass.UrlBorrado, urlrn.UrlRnOuterClass.UrlBorrado>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteUrl"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlBorrado.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.UrlBorrado.getDefaultInstance()))
              .setSchemaDescriptor(new UrlRnMethodDescriptorSupplier("DeleteUrl"))
              .build();
        }
      }
    }
    return getDeleteUrlMethod;
  }

  private static volatile io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.Empty,
      urlrn.UrlRnOuterClass.ListaUrl> getGetAllMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAll",
      requestType = urlrn.UrlRnOuterClass.Empty.class,
      responseType = urlrn.UrlRnOuterClass.ListaUrl.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.Empty,
      urlrn.UrlRnOuterClass.ListaUrl> getGetAllMethod() {
    io.grpc.MethodDescriptor<urlrn.UrlRnOuterClass.Empty, urlrn.UrlRnOuterClass.ListaUrl> getGetAllMethod;
    if ((getGetAllMethod = UrlRnGrpc.getGetAllMethod) == null) {
      synchronized (UrlRnGrpc.class) {
        if ((getGetAllMethod = UrlRnGrpc.getGetAllMethod) == null) {
          UrlRnGrpc.getGetAllMethod = getGetAllMethod =
              io.grpc.MethodDescriptor.<urlrn.UrlRnOuterClass.Empty, urlrn.UrlRnOuterClass.ListaUrl>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAll"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.UrlRnOuterClass.ListaUrl.getDefaultInstance()))
              .setSchemaDescriptor(new UrlRnMethodDescriptorSupplier("GetAll"))
              .build();
        }
      }
    }
    return getGetAllMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UrlRnStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UrlRnStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UrlRnStub>() {
        @java.lang.Override
        public UrlRnStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UrlRnStub(channel, callOptions);
        }
      };
    return UrlRnStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UrlRnBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UrlRnBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UrlRnBlockingStub>() {
        @java.lang.Override
        public UrlRnBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UrlRnBlockingStub(channel, callOptions);
        }
      };
    return UrlRnBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UrlRnFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UrlRnFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UrlRnFutureStub>() {
        @java.lang.Override
        public UrlRnFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UrlRnFutureStub(channel, callOptions);
        }
      };
    return UrlRnFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Servicio
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void getUrl(urlrn.UrlRnOuterClass.UrlRequest request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUrlMethod(), responseObserver);
    }

    /**
     */
    default void createUrl(urlrn.UrlRnOuterClass.UrlCreateRequest request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateUrlMethod(), responseObserver);
    }

    /**
     */
    default void updateUrl(urlrn.UrlRnOuterClass.UrlUpdateRequest request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateUrlMethod(), responseObserver);
    }

    /**
     */
    default void deleteUrl(urlrn.UrlRnOuterClass.UrlBorrado request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlBorrado> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteUrlMethod(), responseObserver);
    }

    /**
     */
    default void getAll(urlrn.UrlRnOuterClass.Empty request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.ListaUrl> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UrlRn.
   * <pre>
   * Servicio
   * </pre>
   */
  public static abstract class UrlRnImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UrlRnGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UrlRn.
   * <pre>
   * Servicio
   * </pre>
   */
  public static final class UrlRnStub
      extends io.grpc.stub.AbstractAsyncStub<UrlRnStub> {
    private UrlRnStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UrlRnStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UrlRnStub(channel, callOptions);
    }

    /**
     */
    public void getUrl(urlrn.UrlRnOuterClass.UrlRequest request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUrlMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createUrl(urlrn.UrlRnOuterClass.UrlCreateRequest request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateUrlMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateUrl(urlrn.UrlRnOuterClass.UrlUpdateRequest request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateUrlMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteUrl(urlrn.UrlRnOuterClass.UrlBorrado request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlBorrado> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteUrlMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAll(urlrn.UrlRnOuterClass.Empty request,
        io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.ListaUrl> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UrlRn.
   * <pre>
   * Servicio
   * </pre>
   */
  public static final class UrlRnBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UrlRnBlockingStub> {
    private UrlRnBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UrlRnBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UrlRnBlockingStub(channel, callOptions);
    }

    /**
     */
    public urlrn.UrlRnOuterClass.UrlResponse getUrl(urlrn.UrlRnOuterClass.UrlRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUrlMethod(), getCallOptions(), request);
    }

    /**
     */
    public urlrn.UrlRnOuterClass.UrlResponse createUrl(urlrn.UrlRnOuterClass.UrlCreateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUrlMethod(), getCallOptions(), request);
    }

    /**
     */
    public urlrn.UrlRnOuterClass.UrlResponse updateUrl(urlrn.UrlRnOuterClass.UrlUpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateUrlMethod(), getCallOptions(), request);
    }

    /**
     */
    public urlrn.UrlRnOuterClass.UrlBorrado deleteUrl(urlrn.UrlRnOuterClass.UrlBorrado request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteUrlMethod(), getCallOptions(), request);
    }

    /**
     */
    public urlrn.UrlRnOuterClass.ListaUrl getAll(urlrn.UrlRnOuterClass.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UrlRn.
   * <pre>
   * Servicio
   * </pre>
   */
  public static final class UrlRnFutureStub
      extends io.grpc.stub.AbstractFutureStub<UrlRnFutureStub> {
    private UrlRnFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UrlRnFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UrlRnFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<urlrn.UrlRnOuterClass.UrlResponse> getUrl(
        urlrn.UrlRnOuterClass.UrlRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUrlMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<urlrn.UrlRnOuterClass.UrlResponse> createUrl(
        urlrn.UrlRnOuterClass.UrlCreateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateUrlMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<urlrn.UrlRnOuterClass.UrlResponse> updateUrl(
        urlrn.UrlRnOuterClass.UrlUpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateUrlMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<urlrn.UrlRnOuterClass.UrlBorrado> deleteUrl(
        urlrn.UrlRnOuterClass.UrlBorrado request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteUrlMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<urlrn.UrlRnOuterClass.ListaUrl> getAll(
        urlrn.UrlRnOuterClass.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_URL = 0;
  private static final int METHODID_CREATE_URL = 1;
  private static final int METHODID_UPDATE_URL = 2;
  private static final int METHODID_DELETE_URL = 3;
  private static final int METHODID_GET_ALL = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_URL:
          serviceImpl.getUrl((urlrn.UrlRnOuterClass.UrlRequest) request,
              (io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse>) responseObserver);
          break;
        case METHODID_CREATE_URL:
          serviceImpl.createUrl((urlrn.UrlRnOuterClass.UrlCreateRequest) request,
              (io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse>) responseObserver);
          break;
        case METHODID_UPDATE_URL:
          serviceImpl.updateUrl((urlrn.UrlRnOuterClass.UrlUpdateRequest) request,
              (io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlResponse>) responseObserver);
          break;
        case METHODID_DELETE_URL:
          serviceImpl.deleteUrl((urlrn.UrlRnOuterClass.UrlBorrado) request,
              (io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.UrlBorrado>) responseObserver);
          break;
        case METHODID_GET_ALL:
          serviceImpl.getAll((urlrn.UrlRnOuterClass.Empty) request,
              (io.grpc.stub.StreamObserver<urlrn.UrlRnOuterClass.ListaUrl>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetUrlMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              urlrn.UrlRnOuterClass.UrlRequest,
              urlrn.UrlRnOuterClass.UrlResponse>(
                service, METHODID_GET_URL)))
        .addMethod(
          getCreateUrlMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              urlrn.UrlRnOuterClass.UrlCreateRequest,
              urlrn.UrlRnOuterClass.UrlResponse>(
                service, METHODID_CREATE_URL)))
        .addMethod(
          getUpdateUrlMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              urlrn.UrlRnOuterClass.UrlUpdateRequest,
              urlrn.UrlRnOuterClass.UrlResponse>(
                service, METHODID_UPDATE_URL)))
        .addMethod(
          getDeleteUrlMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              urlrn.UrlRnOuterClass.UrlBorrado,
              urlrn.UrlRnOuterClass.UrlBorrado>(
                service, METHODID_DELETE_URL)))
        .addMethod(
          getGetAllMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              urlrn.UrlRnOuterClass.Empty,
              urlrn.UrlRnOuterClass.ListaUrl>(
                service, METHODID_GET_ALL)))
        .build();
  }

  private static abstract class UrlRnBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UrlRnBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return urlrn.UrlRnOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UrlRn");
    }
  }

  private static final class UrlRnFileDescriptorSupplier
      extends UrlRnBaseDescriptorSupplier {
    UrlRnFileDescriptorSupplier() {}
  }

  private static final class UrlRnMethodDescriptorSupplier
      extends UrlRnBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UrlRnMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UrlRnGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UrlRnFileDescriptorSupplier())
              .addMethod(getGetUrlMethod())
              .addMethod(getCreateUrlMethod())
              .addMethod(getUpdateUrlMethod())
              .addMethod(getDeleteUrlMethod())
              .addMethod(getGetAllMethod())
              .build();
        }
      }
    }
    return result;
  }
}
