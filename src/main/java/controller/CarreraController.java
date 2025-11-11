package controller;

import dto.CarreraDTO; 
import java.util.List;
import java.util.stream.Collectors;
import model.Carrera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.ICarreraService;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    @Autowired
    private ICarreraService carreraService;

    private CarreraDTO toDTO(Carrera carrera) {
        CarreraDTO dto = new CarreraDTO();
        dto.setId(carrera.getId());
        dto.setNombre(carrera.getNombre());
        return dto;
    }

    @GetMapping
    public ResponseEntity<Page<CarreraDTO>> obtenerCarreras(
            @RequestParam(required = false) String nombre,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        
        Page<Carrera> paginaCarreras = carreraService.listarCarreras(nombre, pageable);
        
        Page<CarreraDTO> paginaDTOs = paginaCarreras.map(this::toDTO);
        
        return ResponseEntity.ok(paginaDTOs);
    }
    
    @GetMapping("/nombres")
    public ResponseEntity<List<String>> obtenerNombresCarreras() {
        // Pedimos todas las carreras 
        Pageable pageable = PageRequest.of(0, 100); 
        Page<Carrera> carrerasPage = carreraService.listarCarreras(null, pageable);
        
        // Convertimos la lista de Carrera a una lista de String (solo nombres)
        List<String> nombres = carrerasPage.getContent().stream()
                .map(Carrera::getNombre)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(nombres);
    }
}