package com.mycompany.itsonconnect;

import Repository.HobbyRepository;
import java.util.Arrays;
import java.util.List;
import model.Hobby;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import service.ICarreraService;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.mycompany.itsonconnect",
    "controller",
    "service",
    "Config"
})
@EnableJpaRepositories(basePackages = "Repository")
@EntityScan(basePackages = "model")
public class ItsonConnectApplication {

    public static void main(String[] args) {

        SpringApplication.run(ItsonConnectApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(HobbyRepository hobbyRepository,ICarreraService carreraService) {
        return (args) -> {
            List<String> hobbiesIniciales = Arrays.asList(
                    "Gaming",
                    "Musica",
                    "Deportes",
                    "Viajes",
                    "Lectura",
                    "Cantar",
                    "Codificar"
            );
            for (String nombreHobby : hobbiesIniciales) {
                if (hobbyRepository.findByNombre(nombreHobby).isEmpty()) {
                    Hobby hobby = new Hobby();
                    hobby.setNombre(nombreHobby);
                    hobby.setDescripcion("Interes en : " + nombreHobby);
                    hobbyRepository.save(hobby);
                    System.out.println("hobby precargado " + nombreHobby);

                }
            }
            carreraService.precargarCarreras();
        };
    }

}
