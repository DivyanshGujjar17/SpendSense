package com.MoneyManager.SpendSense.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/status","/health"})
public class HomeController {
@GetMapping
    public String healthchecking(){
    return "Application is Running Successfully";
}
}
