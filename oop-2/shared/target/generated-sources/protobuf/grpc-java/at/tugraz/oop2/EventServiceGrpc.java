package at.tugraz.oop2;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.56.1)",
    comments = "Source: mapservice.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class EventServiceGrpc {

  private EventServiceGrpc() {}

  public static final String SERVICE_NAME = "eventservice.EventService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<at.tugraz.oop2.EventById,
      at.tugraz.oop2.Event> getEventByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "eventById",
      requestType = at.tugraz.oop2.EventById.class,
      responseType = at.tugraz.oop2.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<at.tugraz.oop2.EventById,
      at.tugraz.oop2.Event> getEventByIdMethod() {
    io.grpc.MethodDescriptor<at.tugraz.oop2.EventById, at.tugraz.oop2.Event> getEventByIdMethod;
    if ((getEventByIdMethod = EventServiceGrpc.getEventByIdMethod) == null) {
      synchronized (EventServiceGrpc.class) {
        if ((getEventByIdMethod = EventServiceGrpc.getEventByIdMethod) == null) {
          EventServiceGrpc.getEventByIdMethod = getEventByIdMethod =
              io.grpc.MethodDescriptor.<at.tugraz.oop2.EventById, at.tugraz.oop2.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "eventById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.EventById.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.Event.getDefaultInstance()))
              .setSchemaDescriptor(new EventServiceMethodDescriptorSupplier("eventById"))
              .build();
        }
      }
    }
    return getEventByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<at.tugraz.oop2.AmenitiesRequest,
      at.tugraz.oop2.Events> getGetAmenitiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAmenities",
      requestType = at.tugraz.oop2.AmenitiesRequest.class,
      responseType = at.tugraz.oop2.Events.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<at.tugraz.oop2.AmenitiesRequest,
      at.tugraz.oop2.Events> getGetAmenitiesMethod() {
    io.grpc.MethodDescriptor<at.tugraz.oop2.AmenitiesRequest, at.tugraz.oop2.Events> getGetAmenitiesMethod;
    if ((getGetAmenitiesMethod = EventServiceGrpc.getGetAmenitiesMethod) == null) {
      synchronized (EventServiceGrpc.class) {
        if ((getGetAmenitiesMethod = EventServiceGrpc.getGetAmenitiesMethod) == null) {
          EventServiceGrpc.getGetAmenitiesMethod = getGetAmenitiesMethod =
              io.grpc.MethodDescriptor.<at.tugraz.oop2.AmenitiesRequest, at.tugraz.oop2.Events>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAmenities"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.AmenitiesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.Events.getDefaultInstance()))
              .setSchemaDescriptor(new EventServiceMethodDescriptorSupplier("getAmenities"))
              .build();
        }
      }
    }
    return getGetAmenitiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<at.tugraz.oop2.RoadRequest,
      at.tugraz.oop2.Events> getGetRoadsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRoads",
      requestType = at.tugraz.oop2.RoadRequest.class,
      responseType = at.tugraz.oop2.Events.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<at.tugraz.oop2.RoadRequest,
      at.tugraz.oop2.Events> getGetRoadsMethod() {
    io.grpc.MethodDescriptor<at.tugraz.oop2.RoadRequest, at.tugraz.oop2.Events> getGetRoadsMethod;
    if ((getGetRoadsMethod = EventServiceGrpc.getGetRoadsMethod) == null) {
      synchronized (EventServiceGrpc.class) {
        if ((getGetRoadsMethod = EventServiceGrpc.getGetRoadsMethod) == null) {
          EventServiceGrpc.getGetRoadsMethod = getGetRoadsMethod =
              io.grpc.MethodDescriptor.<at.tugraz.oop2.RoadRequest, at.tugraz.oop2.Events>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getRoads"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.RoadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.Events.getDefaultInstance()))
              .setSchemaDescriptor(new EventServiceMethodDescriptorSupplier("getRoads"))
              .build();
        }
      }
    }
    return getGetRoadsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<at.tugraz.oop2.LanduseRequest,
      at.tugraz.oop2.LanduseResponse> getGetLanduseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getLanduse",
      requestType = at.tugraz.oop2.LanduseRequest.class,
      responseType = at.tugraz.oop2.LanduseResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<at.tugraz.oop2.LanduseRequest,
      at.tugraz.oop2.LanduseResponse> getGetLanduseMethod() {
    io.grpc.MethodDescriptor<at.tugraz.oop2.LanduseRequest, at.tugraz.oop2.LanduseResponse> getGetLanduseMethod;
    if ((getGetLanduseMethod = EventServiceGrpc.getGetLanduseMethod) == null) {
      synchronized (EventServiceGrpc.class) {
        if ((getGetLanduseMethod = EventServiceGrpc.getGetLanduseMethod) == null) {
          EventServiceGrpc.getGetLanduseMethod = getGetLanduseMethod =
              io.grpc.MethodDescriptor.<at.tugraz.oop2.LanduseRequest, at.tugraz.oop2.LanduseResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getLanduse"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.LanduseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.LanduseResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventServiceMethodDescriptorSupplier("getLanduse"))
              .build();
        }
      }
    }
    return getGetLanduseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<at.tugraz.oop2.Event,
      at.tugraz.oop2.Event> getEventCreationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "eventCreation",
      requestType = at.tugraz.oop2.Event.class,
      responseType = at.tugraz.oop2.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<at.tugraz.oop2.Event,
      at.tugraz.oop2.Event> getEventCreationMethod() {
    io.grpc.MethodDescriptor<at.tugraz.oop2.Event, at.tugraz.oop2.Event> getEventCreationMethod;
    if ((getEventCreationMethod = EventServiceGrpc.getEventCreationMethod) == null) {
      synchronized (EventServiceGrpc.class) {
        if ((getEventCreationMethod = EventServiceGrpc.getEventCreationMethod) == null) {
          EventServiceGrpc.getEventCreationMethod = getEventCreationMethod =
              io.grpc.MethodDescriptor.<at.tugraz.oop2.Event, at.tugraz.oop2.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "eventCreation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.Event.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  at.tugraz.oop2.Event.getDefaultInstance()))
              .setSchemaDescriptor(new EventServiceMethodDescriptorSupplier("eventCreation"))
              .build();
        }
      }
    }
    return getEventCreationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EventServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventServiceStub>() {
        @java.lang.Override
        public EventServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventServiceStub(channel, callOptions);
        }
      };
    return EventServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EventServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventServiceBlockingStub>() {
        @java.lang.Override
        public EventServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventServiceBlockingStub(channel, callOptions);
        }
      };
    return EventServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EventServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventServiceFutureStub>() {
        @java.lang.Override
        public EventServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventServiceFutureStub(channel, callOptions);
        }
      };
    return EventServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void eventById(at.tugraz.oop2.EventById request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEventByIdMethod(), responseObserver);
    }

    /**
     */
    default void getAmenities(at.tugraz.oop2.AmenitiesRequest request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Events> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAmenitiesMethod(), responseObserver);
    }

    /**
     */
    default void getRoads(at.tugraz.oop2.RoadRequest request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Events> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRoadsMethod(), responseObserver);
    }

    /**
     */
    default void getLanduse(at.tugraz.oop2.LanduseRequest request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.LanduseResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetLanduseMethod(), responseObserver);
    }

    /**
     */
    default void eventCreation(at.tugraz.oop2.Event request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEventCreationMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service EventService.
   */
  public static abstract class EventServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return EventServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service EventService.
   */
  public static final class EventServiceStub
      extends io.grpc.stub.AbstractAsyncStub<EventServiceStub> {
    private EventServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventServiceStub(channel, callOptions);
    }

    /**
     */
    public void eventById(at.tugraz.oop2.EventById request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEventByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAmenities(at.tugraz.oop2.AmenitiesRequest request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Events> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAmenitiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRoads(at.tugraz.oop2.RoadRequest request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Events> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRoadsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getLanduse(at.tugraz.oop2.LanduseRequest request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.LanduseResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetLanduseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void eventCreation(at.tugraz.oop2.Event request,
        io.grpc.stub.StreamObserver<at.tugraz.oop2.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEventCreationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service EventService.
   */
  public static final class EventServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<EventServiceBlockingStub> {
    private EventServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public at.tugraz.oop2.Event eventById(at.tugraz.oop2.EventById request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEventByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public at.tugraz.oop2.Events getAmenities(at.tugraz.oop2.AmenitiesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAmenitiesMethod(), getCallOptions(), request);
    }

    /**
     */
    public at.tugraz.oop2.Events getRoads(at.tugraz.oop2.RoadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRoadsMethod(), getCallOptions(), request);
    }

    /**
     */
    public at.tugraz.oop2.LanduseResponse getLanduse(at.tugraz.oop2.LanduseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetLanduseMethod(), getCallOptions(), request);
    }

    /**
     */
    public at.tugraz.oop2.Event eventCreation(at.tugraz.oop2.Event request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEventCreationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service EventService.
   */
  public static final class EventServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<EventServiceFutureStub> {
    private EventServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<at.tugraz.oop2.Event> eventById(
        at.tugraz.oop2.EventById request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEventByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<at.tugraz.oop2.Events> getAmenities(
        at.tugraz.oop2.AmenitiesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAmenitiesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<at.tugraz.oop2.Events> getRoads(
        at.tugraz.oop2.RoadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRoadsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<at.tugraz.oop2.LanduseResponse> getLanduse(
        at.tugraz.oop2.LanduseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetLanduseMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<at.tugraz.oop2.Event> eventCreation(
        at.tugraz.oop2.Event request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEventCreationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_EVENT_BY_ID = 0;
  private static final int METHODID_GET_AMENITIES = 1;
  private static final int METHODID_GET_ROADS = 2;
  private static final int METHODID_GET_LANDUSE = 3;
  private static final int METHODID_EVENT_CREATION = 4;

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
        case METHODID_EVENT_BY_ID:
          serviceImpl.eventById((at.tugraz.oop2.EventById) request,
              (io.grpc.stub.StreamObserver<at.tugraz.oop2.Event>) responseObserver);
          break;
        case METHODID_GET_AMENITIES:
          serviceImpl.getAmenities((at.tugraz.oop2.AmenitiesRequest) request,
              (io.grpc.stub.StreamObserver<at.tugraz.oop2.Events>) responseObserver);
          break;
        case METHODID_GET_ROADS:
          serviceImpl.getRoads((at.tugraz.oop2.RoadRequest) request,
              (io.grpc.stub.StreamObserver<at.tugraz.oop2.Events>) responseObserver);
          break;
        case METHODID_GET_LANDUSE:
          serviceImpl.getLanduse((at.tugraz.oop2.LanduseRequest) request,
              (io.grpc.stub.StreamObserver<at.tugraz.oop2.LanduseResponse>) responseObserver);
          break;
        case METHODID_EVENT_CREATION:
          serviceImpl.eventCreation((at.tugraz.oop2.Event) request,
              (io.grpc.stub.StreamObserver<at.tugraz.oop2.Event>) responseObserver);
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
          getEventByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              at.tugraz.oop2.EventById,
              at.tugraz.oop2.Event>(
                service, METHODID_EVENT_BY_ID)))
        .addMethod(
          getGetAmenitiesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              at.tugraz.oop2.AmenitiesRequest,
              at.tugraz.oop2.Events>(
                service, METHODID_GET_AMENITIES)))
        .addMethod(
          getGetRoadsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              at.tugraz.oop2.RoadRequest,
              at.tugraz.oop2.Events>(
                service, METHODID_GET_ROADS)))
        .addMethod(
          getGetLanduseMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              at.tugraz.oop2.LanduseRequest,
              at.tugraz.oop2.LanduseResponse>(
                service, METHODID_GET_LANDUSE)))
        .addMethod(
          getEventCreationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              at.tugraz.oop2.Event,
              at.tugraz.oop2.Event>(
                service, METHODID_EVENT_CREATION)))
        .build();
  }

  private static abstract class EventServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EventServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return at.tugraz.oop2.EventServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EventService");
    }
  }

  private static final class EventServiceFileDescriptorSupplier
      extends EventServiceBaseDescriptorSupplier {
    EventServiceFileDescriptorSupplier() {}
  }

  private static final class EventServiceMethodDescriptorSupplier
      extends EventServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    EventServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (EventServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EventServiceFileDescriptorSupplier())
              .addMethod(getEventByIdMethod())
              .addMethod(getGetAmenitiesMethod())
              .addMethod(getGetRoadsMethod())
              .addMethod(getGetLanduseMethod())
              .addMethod(getEventCreationMethod())
              .build();
        }
      }
    }
    return result;
  }
}
