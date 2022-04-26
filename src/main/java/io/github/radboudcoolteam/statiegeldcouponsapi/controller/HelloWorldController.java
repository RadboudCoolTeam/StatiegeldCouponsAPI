package io.github.radboudcoolteam.statiegeldcouponsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("helloworld")
public class HelloWorldController {

    @GetMapping("/")
    public ResponseEntity<String> getHelloWorld() {
        return ResponseEntity.accepted()
                .body("Are you alone?");
    }

    @GetMapping("/urmom/{name}")
    public ResponseEntity<String> getMomsName(@PathVariable String name) {
        return ResponseEntity.accepted()
                .body("Ur moms name is: " + name);
    }
}
