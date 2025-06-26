package at.tugraz.oop2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<HashMap<String,String>> entity (InvalidRequestException ex) {
        return new ResponseEntity<>(new HashMap<>(){{put("message", "invalid or missing parameter");}}, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HashMap<String,String>> entity (NotFoundException ex) {
        return new ResponseEntity<>(new HashMap<>(){{put("message", "requested item not found");}}, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<HashMap<String,String>> entity (ServerErrorException ex) {
        return new ResponseEntity<>(new HashMap<>(){{put("message", "internal issue");}}, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
