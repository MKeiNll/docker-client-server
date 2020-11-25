package org.example.client.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/")
public class ClientController {

    @GetMapping()
    public String getHello() {
        return "Hello, I'm client";
    }

}