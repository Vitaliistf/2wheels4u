package org.vitaliistf.twowheels4u.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MotorcycleResponseDto {
    private Long id;
    private String model;
    private String manufacturer;
    private BigDecimal fee;
    private String type;
    private Integer inventory;
    private boolean deleted;
}
