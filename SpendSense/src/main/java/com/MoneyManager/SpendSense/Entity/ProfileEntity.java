package com.MoneyManager.SpendSense.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private String ProfileImageUrl;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Boolean isActive;
    private String activationToken;
    @PrePersist
    public void prepersist(){
        if(this.isActive==null){
            isActive=false;
        }
    }
}
