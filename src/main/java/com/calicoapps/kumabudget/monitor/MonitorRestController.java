package com.calicoapps.kumabudget.monitor;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class MonitorRestController {

    @GetMapping()
    @Operation(hidden = true)
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("hello")
    @Operation(summary = "Hello world")
    public ResponseEntity<String> isAlive() {
        return ResponseEntity.ok("Hello world ! I'm alive !");
    }

    @GetMapping("testSecurity")
    @Operation(summary = "Hello world but secured")
    public ResponseEntity testSecurity() {
        return ResponseEntity.ok().build();
    }

}
