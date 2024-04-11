package com.calicoapps.kumabudget.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class MonitorRestController {

    @GetMapping("isAlive")
    public ResponseEntity<String> helloWorld() {
        return new ResponseEntity<>("Hello World :)" , HttpStatus.OK);
    }

    @GetMapping("testSecurity")
    public ResponseEntity<String> testSecurity() {
        return new ResponseEntity<>("OK passed" , HttpStatus.OK);
    }

}
