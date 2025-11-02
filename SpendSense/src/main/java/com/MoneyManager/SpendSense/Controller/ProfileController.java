package com.MoneyManager.SpendSense.Controller;

import com.MoneyManager.SpendSense.Dto.AuthDTO;
import com.MoneyManager.SpendSense.Dto.profileDTO;
import com.MoneyManager.SpendSense.Service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/register")
    public ResponseEntity<profileDTO> registerprofile(@RequestBody  profileDTO profileDto){
        profileDTO registeredProfile=profileService.registerprofile(profileDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }
    @GetMapping("/activate")
    public ResponseEntity<String> Activatetheuserlink(@RequestParam String token){
        boolean isactivated=profileService.activateProfile(token);
        if(isactivated){
            return ResponseEntity.ok("profile Activated Successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation Token Not Found");
        }

    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO){
        try {
            if(!profileService.isAccountActive(authDTO.getEmail())){

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message",
                        "Account is not activated activate it first"));
            }
            Map<String,Object> response=profileService.authenticateandGenerateToken(authDTO);
return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",
                    e.getMessage()));
        }

    }
    @GetMapping("/test")
    public String test(){
        return "Test Successfully";
    }


}
