package com.MoneyManager.SpendSense.Controller;

import com.MoneyManager.SpendSense.Service.DashboardServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {
    private final DashboardServices dashboardServices;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getDashboardData(){
       Map<String,Object> dashboarddata= dashboardServices.getDashboardData();
       return ResponseEntity.ok(dashboarddata);
    }
}
