package org.samrad.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ParkingRecord {
    private long id;
    private String licensePlate;
    private String vehicleType;
    private int spotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal parkingFee;

}
