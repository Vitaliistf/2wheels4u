package org.vitaliistf.twowheels4u.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private List<String> errors;
    private LocalDateTime timestamp;
}
