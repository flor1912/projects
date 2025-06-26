package at.tugraz.oop2;

import io.grpc.*;
import lombok.Data;

import java.util.logging.Logger;

@Data
public class gRPCClient {
    public static EventServiceGrpc.EventServiceBlockingStub stub;
    private static final Logger logger = Logger.getLogger(gRPCClient.class.getName());
    gRPCClient(String backendTarget) {
        try {
            ManagedChannel channel = ManagedChannelBuilder.forTarget(backendTarget)
                    .usePlaintext()
                    .build();
            gRPCClient.stub = EventServiceGrpc.newBlockingStub(channel);
        }
        catch (Exception e) {
            throw new InternalError(e);
        }
    }

    public static Event getEventbyIdReqeust(Long id) {
        EventById eventById = EventById.newBuilder().setId(id).build();
        Event response = stub.eventById(eventById);
        if(response.getEvent().isEmpty()) {
            throw new NotFoundException();
        }
        return response;
    }

    public static Events getRoads(String name, double bbox_tl_x, double bbox_tl_y, double bbox_br_x, double bbox_br_y) {
        RoadRequest request = RoadRequest
                .newBuilder()
                .setRoad(name)
                .setBboxTlX(bbox_tl_x)
                .setBboxTlY(bbox_tl_y)
                .setBboxBrX(bbox_br_x)
                .setBboxBrY(bbox_br_y)
                .build();
        Events response = stub.getRoads(request);
        return response;
    }

    public static Events getAmenities(String name, double bbox_tl_x, double bbox_tl_y, double bbox_br_x, double bbox_br_y,
                                      double point_x, double point_y, double point_d) {
        AmenitiesRequest request = AmenitiesRequest
                .newBuilder()
                .setAmenity(name)
                .setBboxTlX(bbox_tl_x)
                .setBboxTlY(bbox_tl_y)
                .setBboxBrX(bbox_br_x)
                .setBboxBrY(bbox_br_y)
                .setPointX(point_x)
                .setPointY(point_y)
                .setPointD((point_d))
                .build();
        Events response = stub.getAmenities(request);
        return response;
    }

    public static LanduseResponse getLanduse(double bbox_tl_x, double bbox_tl_y, double bbox_br_x, double bbox_br_y) {
        LanduseRequest request = LanduseRequest
                .newBuilder()
                .setBboxTlX(bbox_tl_x)
                .setBboxTlY(bbox_tl_y)
                .setBboxBrX(bbox_br_x)
                .setBboxBrY(bbox_br_y)
                .build();
        LanduseResponse response = stub.getLanduse(request);
        return response;
    }
}

