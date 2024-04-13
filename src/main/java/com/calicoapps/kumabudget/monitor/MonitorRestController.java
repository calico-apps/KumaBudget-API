package com.calicoapps.kumabudget.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class MonitorRestController {

    @GetMapping()
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("isAlive")
    public ResponseEntity<String> isAlive() {
        return ResponseEntity.ok("Yes I'm alive !");
    }

    @GetMapping("testSecurity")
    public ResponseEntity testSecurity() {
        return ResponseEntity.ok().build();
    }

}
