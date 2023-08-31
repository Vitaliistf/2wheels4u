package org.vitaliistf.twowheels4u.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vitaliistf.twowheels4u.dto.request.MotorcycleRequestDto;
import org.vitaliistf.twowheels4u.dto.response.MotorcycleResponseDto;
import org.vitaliistf.twowheels4u.mapper.MotorcycleMapper;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.service.MotorcycleService;
import org.vitaliistf.twowheels4u.service.NotificationService;
import org.vitaliistf.twowheels4u.util.RequestParamParser;

@RestController
@RequestMapping("/motorcycles")
@RequiredArgsConstructor
public class MotorcycleController {
    private final MotorcycleService motorcycleService;
    private final MotorcycleMapper motorcycleMapper;
    private final RequestParamParser requestParamParser;
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Endpoint for adding a motorcycle.",
            description = "Only managers are permitted.")
    public MotorcycleResponseDto add(
            @Parameter(schema = @Schema(implementation = MotorcycleRequestDto.class))
            @RequestBody MotorcycleRequestDto motorcycleRequestDto) {
        Motorcycle motorcycle = motorcycleService.save(
                motorcycleMapper.toModel(motorcycleRequestDto)
        );

        notificationService.sendMessageToAdmin(
                "New motorcycle was created with id: " + motorcycle.getId()
        );

        return motorcycleMapper.toDto(motorcycle);
    }

    @GetMapping
    @Operation(summary = "Endpoint to get all motorcycles with pagination and sorting.")
    public List<MotorcycleResponseDto> findAll(
            @Parameter(description = "Number of cars per page.")
            @RequestParam(defaultValue = "10") Integer count,
            @Parameter(description = "Page number.")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Sorting type (ASC or DESC).")
            @RequestParam(defaultValue = "id") String sortBy) {
        Sort sort = Sort.by(requestParamParser.toSortOrders(sortBy));
        Pageable pageable = PageRequest.of(page, count, sort);
        return motorcycleService.findAll(pageable).stream()
                .map(motorcycleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-params")
    @Operation(summary = "Endpoint to get motorcycles by parameters.")
    public List<MotorcycleResponseDto> findAllByParams(
            @Parameter(description = "Filtering by fields. For example: model=S1000RR.")
            @RequestParam Map<String, String> params) {
        return motorcycleService.findAllByParams(params).stream()
                .map(motorcycleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint to get a motorcycle by its ID.")
    public MotorcycleResponseDto getById(
            @Parameter(description = "Motorcycle ID.")
            @PathVariable Long id) {
        return motorcycleMapper.toDto(motorcycleService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint to update a motorcycle.",
            description = "Only managers are permitted")
    public MotorcycleResponseDto updateInfo(
            @Parameter(description = "Motorcycle ID.")
            @PathVariable Long id,
            @Parameter(schema = @Schema(implementation = MotorcycleRequestDto.class))
            @Valid @RequestBody MotorcycleRequestDto motorcycleRequestDto) {
        Motorcycle updatedMotorcycle = motorcycleService.update(
                id, motorcycleMapper.toModel(motorcycleRequestDto)
        );

        notificationService.sendMessageToAdmin(
                "Motorcycle was updated by id: " + updatedMotorcycle.getId()
        );

        return motorcycleMapper.toDto(updatedMotorcycle);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint to delete a motorcycle.",
            description = "Only managers are permitted.")
    public void delete(
            @Parameter(description = "Motorcycle ID.")
            @PathVariable Long id) {
        motorcycleService.delete(id);

        notificationService.sendMessageToAdmin("Motorcycle was deleted by id: " + id);
    }

    @PostMapping("/add/{id}")
    @Operation(summary = "Endpoint to add a motorcycle to the inventory.",
            description = "Only managers are permitted.")
    public MotorcycleResponseDto addMotorcycleToInventory(
            @Parameter(description = "Motorcycle ID.")
            @PathVariable Long id) {
        notificationService.sendMessageToAdmin("Motorcycle was add to inventory by id: " + id);

        return motorcycleMapper.toDto(motorcycleService.addMotorcycleToInventory(id));
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "Endpoint to remove a motorcycle from the inventory.",
            description = "Only managers are permitted.")
    public MotorcycleResponseDto removeMotorcycleFromInventory(
            @Parameter(description = "Motorcycle ID.")
            @PathVariable Long id) {
        notificationService.sendMessageToAdmin(
                "Motorcycle was removed from inventory by id: " + id
        );

        return motorcycleMapper.toDto(motorcycleService.removeMotorcycleFromInventory(id));
    }
}
