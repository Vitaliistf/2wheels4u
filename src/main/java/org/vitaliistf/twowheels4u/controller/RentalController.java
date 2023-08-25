package org.vitaliistf.twowheels4u.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
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
import org.vitaliistf.twowheels4u.service.RentalService;
import org.vitaliistf.twowheels4u.service.UserService;

@RestController
@RequestMapping("/rentals")
@AllArgsConstructor
public class RentalController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final UserService userService;

    @PostMapping
    public RentalResponseDto add(Authentication authentication,
                                 @RequestBody RentalRequestDto rentalRequestDto) {
        Rental rentalForSave = rentalMapper.toModel(rentalRequestDto);
        User user = userService.findByEmail(authentication.getName());
        rentalForSave.setUser(user);
        Rental savedRental = rentalService.save(rentalForSave);
        return rentalMapper.toDto(savedRental);
    }

    @PutMapping("/{id}/return")
    public RentalResponseDto returnRental(@PathVariable Long id) {
        return rentalMapper.toDto(rentalService.returnRental(id));
    }

    @GetMapping
    public List<RentalResponseDto> findAllByUserIdAndIsAlive(@RequestParam Long userId,
                                                             @RequestParam boolean status) {
        return rentalService.findByIdAndIsActive(userId, status).stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RentalResponseDto findById(@PathVariable Long id) {
        return rentalMapper.toDto(rentalService.findById(id));
    }

    @GetMapping("/my-rentals")
    public List<RentalResponseDto> findAllMyRentals(Authentication authentication) {
        return rentalService.findAll().stream()
                .filter(r -> r.getUser().getEmail().equals(authentication.getName()))
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }
}
