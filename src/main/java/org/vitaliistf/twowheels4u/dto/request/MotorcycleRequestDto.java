package org.vitaliistf.twowheels4u.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MotorcycleRequestDto {
    private String model;
    private String manufacturer;
    private BigDecimal fee;
    private String type;
    private Integer inventory;
}
