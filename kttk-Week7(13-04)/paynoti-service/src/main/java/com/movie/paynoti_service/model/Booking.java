package com.movie.paynoti_service.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private Long id;
    private String username;
    private String movie;
    private int seats;
    private String paymentMethod;
    private String status;
}
