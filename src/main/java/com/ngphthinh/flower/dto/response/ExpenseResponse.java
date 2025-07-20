package com.ngphthinh.flower.dto.response;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponse {
    private Long id;
    private String title;
    private double amount;
    private LocalDateTime date;
}
