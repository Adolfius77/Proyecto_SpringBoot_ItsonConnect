/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dto.HobbyDTO;
import model.Hobby;
import service.IHobbyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jorge
 */
@RestController
@RequestMapping("api/hobbies")
public class HobbyController {
    
    private final IHobbyService hobbyService;
    @Autowired
    public HobbyController(IHobbyService hobbyService) {
        this.hobbyService = hobbyService;
    }

    // ------------------- Convertidores -------------------
    private HobbyDTO toDTO(Hobby hobby) {
        if (hobby == null) {
            return null;
        }
        HobbyDTO dto = new HobbyDTO();
        dto.setId(hobby.getId());
        dto.setNombre(hobby.getNombre());
        dto.setDescripcion(hobby.getDescripcion());
        return dto;
    }

    private Hobby toEntity(HobbyDTO dto) {
        if (dto == null) {
            return null;
        }
        Hobby hobby = new Hobby();
        hobby.setId(dto.getId());
        hobby.setNombre(dto.getNombre());
        hobby.setDescripcion(dto.getDescripcion());
        return hobby;
    }

    @GetMapping
    public List<HobbyDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return hobbyService.listarHobbies(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public HobbyDTO obtenerPorId(@PathVariable Long id) {
        return toDTO(hobbyService.obtenerHobby(id));
    }

    @PostMapping
    public HobbyDTO registrar(@RequestBody HobbyDTO dto) throws Exception {
        Hobby hobby = hobbyService.crearHobby(toEntity(dto));
        return toDTO(hobby);
    }

    @PutMapping("/{id}")
    public HobbyDTO actualizar(@PathVariable Long id, @RequestBody HobbyDTO dto) throws Exception {
        dto.setId(id);
        Hobby hobby = hobbyService.actualizarHobby(toEntity(dto));
        return toDTO(hobby);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) throws Exception {
        hobbyService.eliminarHobby(id);
        
    }
}
