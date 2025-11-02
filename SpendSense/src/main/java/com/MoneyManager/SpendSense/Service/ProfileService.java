package com.MoneyManager.SpendSense.Service;

import com.MoneyManager.SpendSense.Dto.AuthDTO;
import com.MoneyManager.SpendSense.Dto.profileDTO;
import com.MoneyManager.SpendSense.Entity.ProfileEntity;
import com.MoneyManager.SpendSense.Repositiory.ProfileRepository;
import com.MoneyManager.SpendSense.Util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.AbstractUpdateOrDeleteStatement;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    public profileDTO registerprofile(profileDTO profileDto){
        ProfileEntity newprofile=toEntity(profileDto);
        newprofile.setActivationToken(UUID.randomUUID().toString());
      newprofile= profileRepository.save(newprofile);
      String ActivationLink="http://localhost:8080/api/v1.0/activate?token="+newprofile.getActivationToken();
      String subject="Activate Your Money Manager Account";
      String Body="Click on the following link to activate your account:"+ActivationLink;
      emailService.sendEmail(newprofile.getEmail(),subject,Body);
return todto(newprofile);
    }
    private profileDTO todto(ProfileEntity newprofile) {
        return profileDTO.builder().id(newprofile.getId()).
        fullName(newprofile.getFullName()).
        email(newprofile.getEmail()).
        ProfileImageUrl(newprofile.getProfileImageUrl()).
        createdAt(newprofile.getCreatedAt()).
        updatedAt(newprofile.getUpdatedAt()).
                build();
    }
    public ProfileEntity toEntity(profileDTO profileDto){
        return ProfileEntity.builder().id(profileDto.getId()).
                fullName(profileDto.getFullName()).
                email(profileDto.getEmail()).
                password((passwordEncoder.encode(profileDto.getPassword()))).
                ProfileImageUrl(profileDto.getProfileImageUrl()).
                createdAt(profileDto.getCreatedAt()).
                updatedAt(profileDto.getUpdatedAt()).
                build();
    }
    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true); // Correct Lombok setter
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }
    public boolean isAccountActive(String email){
        return profileRepository.findByEmail(email).map(ProfileEntity::getIsActive).orElse(false);

    }
    public ProfileEntity getcurrentprofile(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
String email=authentication.getName();
return  profileRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with this email"));
    }
    public profileDTO getpublicprofile(String email) {
        ProfileEntity currentuser = null;
        if (email == null) {
            currentuser = getcurrentprofile();
        } else {
           currentuser= profileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("profile not found with email"+email));
        }
return profileDTO.builder().id(currentuser.getId()).
            fullName(currentuser.getFullName()).
        email(currentuser.getEmail()).
        ProfileImageUrl(currentuser.getProfileImageUrl()).
        createdAt(currentuser.getCreatedAt()).
        updatedAt(currentuser.getUpdatedAt())
        .build();
    }

    public Map<String, Object> authenticateandGenerateToken(AuthDTO authDTO) {
        try {
 authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
    String token=jwtUtils.generateToken(authDTO.getEmail());
     return Map.of("token",token,
             "user",getpublicprofile(authDTO.getEmail()));
        }
        catch (Exception e){
            throw new RuntimeException("Invalid email or password");
        }
    }
}
