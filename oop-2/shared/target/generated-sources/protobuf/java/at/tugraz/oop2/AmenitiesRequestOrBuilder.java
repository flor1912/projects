// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mapservice.proto

package at.tugraz.oop2;

public interface AmenitiesRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:eventservice.AmenitiesRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string amenity = 1;</code>
   * @return Whether the amenity field is set.
   */
  boolean hasAmenity();
  /**
   * <code>optional string amenity = 1;</code>
   * @return The amenity.
   */
  java.lang.String getAmenity();
  /**
   * <code>optional string amenity = 1;</code>
   * @return The bytes for amenity.
   */
  com.google.protobuf.ByteString
      getAmenityBytes();

  /**
   * <code>double bbox_tl_x = 2;</code>
   * @return The bboxTlX.
   */
  double getBboxTlX();

  /**
   * <code>double bbox_tl_y = 3;</code>
   * @return The bboxTlY.
   */
  double getBboxTlY();

  /**
   * <code>double bbox_br_x = 4;</code>
   * @return The bboxBrX.
   */
  double getBboxBrX();

  /**
   * <code>double bbox_br_y = 5;</code>
   * @return The bboxBrY.
   */
  double getBboxBrY();

  /**
   * <code>double point_x = 6;</code>
   * @return The pointX.
   */
  double getPointX();

  /**
   * <code>double point_y = 7;</code>
   * @return The pointY.
   */
  double getPointY();

  /**
   * <code>double point_d = 8;</code>
   * @return The pointD.
   */
  double getPointD();

  /**
   * <code>optional int32 take = 9;</code>
   * @return Whether the take field is set.
   */
  boolean hasTake();
  /**
   * <code>optional int32 take = 9;</code>
   * @return The take.
   */
  int getTake();

  /**
   * <code>optional int32 page = 10;</code>
   * @return Whether the page field is set.
   */
  boolean hasPage();
  /**
   * <code>optional int32 page = 10;</code>
   * @return The page.
   */
  int getPage();
}
