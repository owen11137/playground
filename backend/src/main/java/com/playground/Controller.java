package com.playground;
import org.springframework.web.bind.annotation.*;import org.springframework.http.HttpStatus;import java.util.*;
@RestController @RequestMapping("/api/simulation")
public class Controller {
 private final Simulator simulator;public Controller(Simulator simulator){this.simulator=simulator;}
 @GetMapping("/orders") public List<Simulator.Order> list(){return simulator.list();}
 @PostMapping("/orders") public Simulator.Order create(@RequestBody Simulator.Request r){return simulator.create(r);}
 @PostMapping("/stop") public Map<String,String> stop(){simulator.stop();return Map.of("status","STOPPED");}
 @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST) public Map<String,String> invalid(IllegalArgumentException e){return Map.of("message",e.getMessage());}
}
