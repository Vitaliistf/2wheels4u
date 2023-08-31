package org.vitaliistf.twowheels4u.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vitaliistf.twowheels4u.dto.request.RentalRequestDto;
import org.vitaliistf.twowheels4u.dto.response.RentalResponseDto;
import org.vitaliistf.twowheels4u.mapper.RentalMapper;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.NotificationService;
import org.vitaliistf.twowheels4u.service.RentalService;
import org.vitaliistf.twowheels4u.service.UserService;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final UserService userService;
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Endpoint to add a rental.")
    public RentalResponseDto add(
            Authentication authentication,
            @Parameter(schema = @Schema(implementation = RentalRequestDto.class))
            @RequestBody RentalRequestDto rentalRequestDto) {
        Rental rentalForSave = rentalMapper.toModel(rentalRequestDto);
        User user = userService.findByEmail(authentication.getName());
        rentalForSave.setUser(user);
        Rental savedRental = rentalService.save(rentalForSave);

        notificationService.sendSuccessfulRentMessage(savedRental);

        return rentalMapper.toDto(savedRental);
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Endpoint to return a motorcycle from a rental.",
            description = "Only managers are permitted, as the actual return date will be used "
                    + "to calculate the price to pay.")
    public RentalResponseDto returnRental(
            @Parameter(description = "Rental ID")
            @PathVariable Long id) {
        notificationService.sendMessageToAdmin("Car was returned by id: " + id);
        return rentalMapper.toDto(rentalService.returnRental(id));
    }

    @GetMapping
    @Operation(summary = "Endpoint to find rentals by user ID and status.")
    public List<RentalResponseDto> findAllByUserIdAndIsAlive(
            @Parameter(description = "User ID.")
            @RequestParam Long userId,
            @Parameter(description = "Status (true or false).")
            @RequestParam boolean status) {
        return rentalService.findByIdAndIsActive(userId, status).stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint to get a rental by ID.")
    public RentalResponseDto findById(
            @Parameter(description = "Rental ID.")
            @PathVariable Long id) {
        return rentalMapper.toDto(rentalService.findById(id));
    }

    @GetMapping("/my-rentals")
    @Operation(summary = "Endpoint to get all rentals for the current user.")
    public List<RentalResponseDto> findAllMyRentals(Authentication authentication) {
        return rentalService.findAll().stream()
                .filter(r -> r.getUser().getEmail().equals(authentication.getName()))
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }
}
