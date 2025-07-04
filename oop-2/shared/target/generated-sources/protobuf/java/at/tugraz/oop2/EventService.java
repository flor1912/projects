// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mapservice.proto

package at.tugraz.oop2;

/**
 * Protobuf service {@code eventservice.EventService}
 */
public  abstract class EventService
    implements com.google.protobuf.Service {
  protected EventService() {}

  public interface Interface {
    /**
     * <code>rpc eventById(.eventservice.EventById) returns (.eventservice.Event);</code>
     */
    public abstract void eventById(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.EventById request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done);

    /**
     * <code>rpc getAmenities(.eventservice.AmenitiesRequest) returns (.eventservice.Events);</code>
     */
    public abstract void getAmenities(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.AmenitiesRequest request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done);

    /**
     * <code>rpc getRoads(.eventservice.RoadRequest) returns (.eventservice.Events);</code>
     */
    public abstract void getRoads(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.RoadRequest request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done);

    /**
     * <code>rpc getLanduse(.eventservice.LanduseRequest) returns (.eventservice.LanduseResponse);</code>
     */
    public abstract void getLanduse(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.LanduseRequest request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.LanduseResponse> done);

    /**
     * <code>rpc eventCreation(.eventservice.Event) returns (.eventservice.Event);</code>
     */
    public abstract void eventCreation(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.Event request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done);

  }

  public static com.google.protobuf.Service newReflectiveService(
      final Interface impl) {
    return new EventService() {
      @java.lang.Override
      public  void eventById(
          com.google.protobuf.RpcController controller,
          at.tugraz.oop2.EventById request,
          com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done) {
        impl.eventById(controller, request, done);
      }

      @java.lang.Override
      public  void getAmenities(
          com.google.protobuf.RpcController controller,
          at.tugraz.oop2.AmenitiesRequest request,
          com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done) {
        impl.getAmenities(controller, request, done);
      }

      @java.lang.Override
      public  void getRoads(
          com.google.protobuf.RpcController controller,
          at.tugraz.oop2.RoadRequest request,
          com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done) {
        impl.getRoads(controller, request, done);
      }

      @java.lang.Override
      public  void getLanduse(
          com.google.protobuf.RpcController controller,
          at.tugraz.oop2.LanduseRequest request,
          com.google.protobuf.RpcCallback<at.tugraz.oop2.LanduseResponse> done) {
        impl.getLanduse(controller, request, done);
      }

      @java.lang.Override
      public  void eventCreation(
          com.google.protobuf.RpcController controller,
          at.tugraz.oop2.Event request,
          com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done) {
        impl.eventCreation(controller, request, done);
      }

    };
  }

  public static com.google.protobuf.BlockingService
      newReflectiveBlockingService(final BlockingInterface impl) {
    return new com.google.protobuf.BlockingService() {
      public final com.google.protobuf.Descriptors.ServiceDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }

      public final com.google.protobuf.Message callBlockingMethod(
          com.google.protobuf.Descriptors.MethodDescriptor method,
          com.google.protobuf.RpcController controller,
          com.google.protobuf.Message request)
          throws com.google.protobuf.ServiceException {
        if (method.getService() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "Service.callBlockingMethod() given method descriptor for " +
            "wrong service type.");
        }
        switch(method.getIndex()) {
          case 0:
            return impl.eventById(controller, (at.tugraz.oop2.EventById)request);
          case 1:
            return impl.getAmenities(controller, (at.tugraz.oop2.AmenitiesRequest)request);
          case 2:
            return impl.getRoads(controller, (at.tugraz.oop2.RoadRequest)request);
          case 3:
            return impl.getLanduse(controller, (at.tugraz.oop2.LanduseRequest)request);
          case 4:
            return impl.eventCreation(controller, (at.tugraz.oop2.Event)request);
          default:
            throw new java.lang.AssertionError("Can't get here.");
        }
      }

      public final com.google.protobuf.Message
          getRequestPrototype(
          com.google.protobuf.Descriptors.MethodDescriptor method) {
        if (method.getService() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "Service.getRequestPrototype() given method " +
            "descriptor for wrong service type.");
        }
        switch(method.getIndex()) {
          case 0:
            return at.tugraz.oop2.EventById.getDefaultInstance();
          case 1:
            return at.tugraz.oop2.AmenitiesRequest.getDefaultInstance();
          case 2:
            return at.tugraz.oop2.RoadRequest.getDefaultInstance();
          case 3:
            return at.tugraz.oop2.LanduseRequest.getDefaultInstance();
          case 4:
            return at.tugraz.oop2.Event.getDefaultInstance();
          default:
            throw new java.lang.AssertionError("Can't get here.");
        }
      }

      public final com.google.protobuf.Message
          getResponsePrototype(
          com.google.protobuf.Descriptors.MethodDescriptor method) {
        if (method.getService() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "Service.getResponsePrototype() given method " +
            "descriptor for wrong service type.");
        }
        switch(method.getIndex()) {
          case 0:
            return at.tugraz.oop2.Event.getDefaultInstance();
          case 1:
            return at.tugraz.oop2.Events.getDefaultInstance();
          case 2:
            return at.tugraz.oop2.Events.getDefaultInstance();
          case 3:
            return at.tugraz.oop2.LanduseResponse.getDefaultInstance();
          case 4:
            return at.tugraz.oop2.Event.getDefaultInstance();
          default:
            throw new java.lang.AssertionError("Can't get here.");
        }
      }

    };
  }

  /**
   * <code>rpc eventById(.eventservice.EventById) returns (.eventservice.Event);</code>
   */
  public abstract void eventById(
      com.google.protobuf.RpcController controller,
      at.tugraz.oop2.EventById request,
      com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done);

  /**
   * <code>rpc getAmenities(.eventservice.AmenitiesRequest) returns (.eventservice.Events);</code>
   */
  public abstract void getAmenities(
      com.google.protobuf.RpcController controller,
      at.tugraz.oop2.AmenitiesRequest request,
      com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done);

  /**
   * <code>rpc getRoads(.eventservice.RoadRequest) returns (.eventservice.Events);</code>
   */
  public abstract void getRoads(
      com.google.protobuf.RpcController controller,
      at.tugraz.oop2.RoadRequest request,
      com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done);

  /**
   * <code>rpc getLanduse(.eventservice.LanduseRequest) returns (.eventservice.LanduseResponse);</code>
   */
  public abstract void getLanduse(
      com.google.protobuf.RpcController controller,
      at.tugraz.oop2.LanduseRequest request,
      com.google.protobuf.RpcCallback<at.tugraz.oop2.LanduseResponse> done);

  /**
   * <code>rpc eventCreation(.eventservice.Event) returns (.eventservice.Event);</code>
   */
  public abstract void eventCreation(
      com.google.protobuf.RpcController controller,
      at.tugraz.oop2.Event request,
      com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done);

  public static final
      com.google.protobuf.Descriptors.ServiceDescriptor
      getDescriptor() {
    return at.tugraz.oop2.EventServiceProto.getDescriptor().getServices().get(0);
  }
  public final com.google.protobuf.Descriptors.ServiceDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }

  public final void callMethod(
      com.google.protobuf.Descriptors.MethodDescriptor method,
      com.google.protobuf.RpcController controller,
      com.google.protobuf.Message request,
      com.google.protobuf.RpcCallback<
        com.google.protobuf.Message> done) {
    if (method.getService() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "Service.callMethod() given method descriptor for wrong " +
        "service type.");
    }
    switch(method.getIndex()) {
      case 0:
        this.eventById(controller, (at.tugraz.oop2.EventById)request,
          com.google.protobuf.RpcUtil.<at.tugraz.oop2.Event>specializeCallback(
            done));
        return;
      case 1:
        this.getAmenities(controller, (at.tugraz.oop2.AmenitiesRequest)request,
          com.google.protobuf.RpcUtil.<at.tugraz.oop2.Events>specializeCallback(
            done));
        return;
      case 2:
        this.getRoads(controller, (at.tugraz.oop2.RoadRequest)request,
          com.google.protobuf.RpcUtil.<at.tugraz.oop2.Events>specializeCallback(
            done));
        return;
      case 3:
        this.getLanduse(controller, (at.tugraz.oop2.LanduseRequest)request,
          com.google.protobuf.RpcUtil.<at.tugraz.oop2.LanduseResponse>specializeCallback(
            done));
        return;
      case 4:
        this.eventCreation(controller, (at.tugraz.oop2.Event)request,
          com.google.protobuf.RpcUtil.<at.tugraz.oop2.Event>specializeCallback(
            done));
        return;
      default:
        throw new java.lang.AssertionError("Can't get here.");
    }
  }

  public final com.google.protobuf.Message
      getRequestPrototype(
      com.google.protobuf.Descriptors.MethodDescriptor method) {
    if (method.getService() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "Service.getRequestPrototype() given method " +
        "descriptor for wrong service type.");
    }
    switch(method.getIndex()) {
      case 0:
        return at.tugraz.oop2.EventById.getDefaultInstance();
      case 1:
        return at.tugraz.oop2.AmenitiesRequest.getDefaultInstance();
      case 2:
        return at.tugraz.oop2.RoadRequest.getDefaultInstance();
      case 3:
        return at.tugraz.oop2.LanduseRequest.getDefaultInstance();
      case 4:
        return at.tugraz.oop2.Event.getDefaultInstance();
      default:
        throw new java.lang.AssertionError("Can't get here.");
    }
  }

  public final com.google.protobuf.Message
      getResponsePrototype(
      com.google.protobuf.Descriptors.MethodDescriptor method) {
    if (method.getService() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "Service.getResponsePrototype() given method " +
        "descriptor for wrong service type.");
    }
    switch(method.getIndex()) {
      case 0:
        return at.tugraz.oop2.Event.getDefaultInstance();
      case 1:
        return at.tugraz.oop2.Events.getDefaultInstance();
      case 2:
        return at.tugraz.oop2.Events.getDefaultInstance();
      case 3:
        return at.tugraz.oop2.LanduseResponse.getDefaultInstance();
      case 4:
        return at.tugraz.oop2.Event.getDefaultInstance();
      default:
        throw new java.lang.AssertionError("Can't get here.");
    }
  }

  public static Stub newStub(
      com.google.protobuf.RpcChannel channel) {
    return new Stub(channel);
  }

  public static final class Stub extends at.tugraz.oop2.EventService implements Interface {
    private Stub(com.google.protobuf.RpcChannel channel) {
      this.channel = channel;
    }

    private final com.google.protobuf.RpcChannel channel;

    public com.google.protobuf.RpcChannel getChannel() {
      return channel;
    }

    public  void eventById(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.EventById request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(0),
        controller,
        request,
        at.tugraz.oop2.Event.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          at.tugraz.oop2.Event.class,
          at.tugraz.oop2.Event.getDefaultInstance()));
    }

    public  void getAmenities(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.AmenitiesRequest request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(1),
        controller,
        request,
        at.tugraz.oop2.Events.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          at.tugraz.oop2.Events.class,
          at.tugraz.oop2.Events.getDefaultInstance()));
    }

    public  void getRoads(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.RoadRequest request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Events> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(2),
        controller,
        request,
        at.tugraz.oop2.Events.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          at.tugraz.oop2.Events.class,
          at.tugraz.oop2.Events.getDefaultInstance()));
    }

    public  void getLanduse(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.LanduseRequest request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.LanduseResponse> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(3),
        controller,
        request,
        at.tugraz.oop2.LanduseResponse.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          at.tugraz.oop2.LanduseResponse.class,
          at.tugraz.oop2.LanduseResponse.getDefaultInstance()));
    }

    public  void eventCreation(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.Event request,
        com.google.protobuf.RpcCallback<at.tugraz.oop2.Event> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(4),
        controller,
        request,
        at.tugraz.oop2.Event.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          at.tugraz.oop2.Event.class,
          at.tugraz.oop2.Event.getDefaultInstance()));
    }
  }

  public static BlockingInterface newBlockingStub(
      com.google.protobuf.BlockingRpcChannel channel) {
    return new BlockingStub(channel);
  }

  public interface BlockingInterface {
    public at.tugraz.oop2.Event eventById(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.EventById request)
        throws com.google.protobuf.ServiceException;

    public at.tugraz.oop2.Events getAmenities(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.AmenitiesRequest request)
        throws com.google.protobuf.ServiceException;

    public at.tugraz.oop2.Events getRoads(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.RoadRequest request)
        throws com.google.protobuf.ServiceException;

    public at.tugraz.oop2.LanduseResponse getLanduse(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.LanduseRequest request)
        throws com.google.protobuf.ServiceException;

    public at.tugraz.oop2.Event eventCreation(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.Event request)
        throws com.google.protobuf.ServiceException;
  }

  private static final class BlockingStub implements BlockingInterface {
    private BlockingStub(com.google.protobuf.BlockingRpcChannel channel) {
      this.channel = channel;
    }

    private final com.google.protobuf.BlockingRpcChannel channel;

    public at.tugraz.oop2.Event eventById(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.EventById request)
        throws com.google.protobuf.ServiceException {
      return (at.tugraz.oop2.Event) channel.callBlockingMethod(
        getDescriptor().getMethods().get(0),
        controller,
        request,
        at.tugraz.oop2.Event.getDefaultInstance());
    }


    public at.tugraz.oop2.Events getAmenities(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.AmenitiesRequest request)
        throws com.google.protobuf.ServiceException {
      return (at.tugraz.oop2.Events) channel.callBlockingMethod(
        getDescriptor().getMethods().get(1),
        controller,
        request,
        at.tugraz.oop2.Events.getDefaultInstance());
    }


    public at.tugraz.oop2.Events getRoads(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.RoadRequest request)
        throws com.google.protobuf.ServiceException {
      return (at.tugraz.oop2.Events) channel.callBlockingMethod(
        getDescriptor().getMethods().get(2),
        controller,
        request,
        at.tugraz.oop2.Events.getDefaultInstance());
    }


    public at.tugraz.oop2.LanduseResponse getLanduse(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.LanduseRequest request)
        throws com.google.protobuf.ServiceException {
      return (at.tugraz.oop2.LanduseResponse) channel.callBlockingMethod(
        getDescriptor().getMethods().get(3),
        controller,
        request,
        at.tugraz.oop2.LanduseResponse.getDefaultInstance());
    }


    public at.tugraz.oop2.Event eventCreation(
        com.google.protobuf.RpcController controller,
        at.tugraz.oop2.Event request)
        throws com.google.protobuf.ServiceException {
      return (at.tugraz.oop2.Event) channel.callBlockingMethod(
        getDescriptor().getMethods().get(4),
        controller,
        request,
        at.tugraz.oop2.Event.getDefaultInstance());
    }

  }

  // @@protoc_insertion_point(class_scope:eventservice.EventService)
}

