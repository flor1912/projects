# Mapping Service – RESTful Geographic Information System

A modular, Java-based mapping service that exposes geospatial data over a RESTful API. Built using Spring Boot and gRPC, this project was developed as part of a university course in software engineering and management.

## Features

- **REST API Endpoints** to retrieve:
  - Amenities (points of interest)
  - Roads with computed geometry
- **Filtering** by:
  - Bounding boxes (latitude/longitude)
  - Point-radius queries
- **Pagination** support with `take` and `skip` parameters
- **Error Handling** with meaningful JSON responses and appropriate HTTP status codes
- **GeoJSON Output** for integration with mapping tools

## Technologies

- Java 17  
- Spring Boot  
- gRPC + Protocol Buffers  
- Java Topology Suite (JTS) for spatial operations  
- Maven for build management  
- Docker / Docker Compose for testing  
- Insomnia/Postman for API debugging  

## Architecture
Frontend (HTML)
↓
Middleware (Spring Boot REST API)
↓
Backend (Java gRPC Server with OSM Parser)


## Dataset
Filtered OpenStreetMap data covering Graz, Austria and surrounding areas. Parsed using Java DOM/SAX, processed with JTS, and served via gRPC.
