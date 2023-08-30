package org.vitaliistf.twowheels4u.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MotorcycleController {
    private final MotorcycleService motorcycleService;
    private final MotorcycleMapper motorcycleMapper;
    private final RequestParamParser requestParamParser;
    private final NotificationService notificationService;

    @PostMapping
    public MotorcycleResponseDto add(@RequestBody MotorcycleRequestDto motorcycleRequestDto) {
        Motorcycle motorcycle = motorcycleService.save(
                motorcycleMapper.toModel(motorcycleRequestDto)
        );

        notificationService.sendMessageToAdmin(
                "New motorcycle was created with id: " + motorcycle.getId()
        );

        return motorcycleMapper.toDto(motorcycle);
    }

    @GetMapping
    public List<MotorcycleResponseDto> findAll(
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "id") String sortBy) {
        Sort sort = Sort.by(requestParamParser.toSortOrders(sortBy));
        Pageable pageable = PageRequest.of(page, count, sort);
        return motorcycleService.findAll(pageable).stream()
                .map(motorcycleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-params")
    public List<MotorcycleResponseDto> findAllByParams(@RequestParam Map<String, String> params) {
        return motorcycleService.findAllByParams(params).stream()
                .map(motorcycleMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MotorcycleResponseDto getById(@PathVariable Long id) {
        return motorcycleMapper.toDto(motorcycleService.findById(id));
    }

    @PutMapping("/{id}")
    public MotorcycleResponseDto updateInfo(
            @PathVariable Long id,
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
    public void delete(@PathVariable Long id) {
        motorcycleService.delete(id);

        notificationService.sendMessageToAdmin("Motorcycle was deleted by id: " + id);
    }

    @PostMapping("/add/{id}")
    public MotorcycleResponseDto addMotorcycleToInventory(@PathVariable Long id) {
        notificationService.sendMessageToAdmin("Motorcycle was add to inventory by id: " + id);

        return motorcycleMapper.toDto(motorcycleService.addMotorcycleToInventory(id));
    }

    @DeleteMapping("/remove/{id}")
    public MotorcycleResponseDto removeMotorcycleFromInventory(@PathVariable Long id) {
        notificationService.sendMessageToAdmin(
                "Motorcycle was removed from inventory by id: " + id
        );

        return motorcycleMapper.toDto(motorcycleService.removeMotorcycleFromInventory(id));
    }
}
