package com.MoneyManager.SpendSense.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class profileDTO {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String ProfileImageUrl;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
