package io.sevcik.csdemo.controllers;

import io.sevcik.csdemo.models.Smoothie;
import io.sevcik.csdemo.payload.SmoothieDescription;
import io.sevcik.csdemo.payload.response.MessageResponse;
import io.sevcik.csdemo.repositories.SmoothieRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Smoothie API", description = "CRUD operations on the smoothie collection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/smoothie")
public class SmoothieController {
    @Autowired
    SmoothieRepository smoothieRepository;

    @ApiOperation("Get smoothie details (READ)")
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<?> getSmoothie(
            @ApiParam(value = "id", required = true)
            @PathVariable final Long id
    ) {
        Smoothie smoothie = smoothieRepository.findById(id).orElse(null);
        if (smoothie == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(SmoothieDescription.fromSmoothie(smoothie));
    }

    // TODO: add pagination
    @ApiOperation("Get smoothies list (READ)")
    @GetMapping("/smoothies")
    public ResponseEntity<?> getSmoothies() {
        class SmoothieListEntry {
            public String name;
            public Long id;

            SmoothieListEntry(Smoothie smoothie) {
                this.name = smoothie.getName();
                this.id = smoothie.getId();
            }
        }

        List<Smoothie> smoothies = smoothieRepository.findAll();
        smoothies.forEach((smoothie) -> new SmoothieListEntry(smoothie));

        return ResponseEntity.ok().body(smoothies);
    }


    @ApiOperation("Create smoothie (CREATE)")
    @PostMapping("")
    public ResponseEntity<?> createSmoothie(@Valid @RequestBody SmoothieDescription smoothieDescription) {
        if (smoothieRepository.existsByName(smoothieDescription.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Smoothie with given name is already taken!"));
        }

        Smoothie smoothie = new Smoothie(
                smoothieDescription.getName(),
                smoothieDescription.getDescription(),
                smoothieDescription.getNutritions().getCalories(),
                smoothieDescription.getNutritions().getProteins(),
                smoothieDescription.getNutritions().getFat(),
                smoothieDescription.getNutritions().getCarbs()
        );

        smoothieRepository.save(smoothie);
        return ResponseEntity.ok().body(SmoothieDescription.fromSmoothie(smoothie));
    }

    @ApiOperation("Update smoothie details (UPDATE)")
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<?> createSmoothie(
            @Valid @RequestBody SmoothieDescription smoothieDescription,
            @ApiParam(value = "id", required = true) @PathVariable final Long id
    ) {
        Smoothie smoothie = smoothieRepository.findById(id).orElse(null);
        if (smoothie == null)
            return ResponseEntity.notFound().build();

        String newName = smoothieDescription.getName();
        if (!smoothie.getName().equals(newName) && smoothieRepository.existsByName(newName)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Smoothie with given name is already taken!"));
        }

        if (smoothieDescription.getName() != null)
            smoothie.setName(smoothieDescription.getName());
        if (smoothieDescription.getDescription() != null)
            smoothie.setDescription(smoothieDescription.getDescription());
        if (smoothieDescription.getNutritions() != null) {
            if (smoothieDescription.getNutritions().getCalories() != null)
                smoothie.setCalories(smoothieDescription.getNutritions().getCalories());
            if (smoothieDescription.getNutritions().getProteins() != null)
                smoothie.setProteins(smoothieDescription.getNutritions().getProteins());
            if (smoothieDescription.getNutritions().getCarbs() != null)
                smoothie.setCarbs(smoothieDescription.getNutritions().getCarbs());
            if (smoothieDescription.getNutritions().getFat() != null)
                smoothie.setFat(smoothieDescription.getNutritions().getFat());
        }
        smoothieRepository.save(smoothie);

        return ResponseEntity.ok().body(SmoothieDescription.fromSmoothie(smoothie));
    }

    @ApiOperation("Delete given smoothie (DELETE)")
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<?> deleteSmoothie(
            @ApiParam(value = "id", required = true) @PathVariable final Long id
    ) {
        Smoothie smoothie = smoothieRepository.findById(id).orElse(null);
        if (!smoothieRepository.existsById(id))
            return ResponseEntity.notFound().build();
        smoothieRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
