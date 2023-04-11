package com.example.jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/members")
@RestController
public class RestApiController {


    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<?> test(@PathVariable("member-id") long memberId,
            @RequestParam("phone") String phone) {
        return new ResponseEntity<String>("memberId::" + memberId + "phone::" + phone,
                HttpStatus.OK);
    }
}
