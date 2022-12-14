package io.sevcik.csdemo.controllers;

import io.sevcik.csdemo.models.Smoothie;
import io.sevcik.csdemo.payload.request.DescribeSmoothieRequest;
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
        return ResponseEntity.ok().body(smoothie);
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
    public ResponseEntity<?> createSmoothie(@Valid @RequestBody DescribeSmoothieRequest describeSmoothieRequest) {
        if (smoothieRepository.existsByName(describeSmoothieRequest.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Smoothie with given name is already taken!"));
        }

        Smoothie smoothie = new Smoothie(
                describeSmoothieRequest.getName(),
                describeSmoothieRequest.getDescription(),
                describeSmoothieRequest.getNutritions()
        );

        smoothieRepository.save(smoothie);
        return ResponseEntity.ok().body(smoothie);
    }

    @ApiOperation("Update smoothie details (UPDATE)")
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<?> createSmoothie(
            @Valid @RequestBody DescribeSmoothieRequest describeSmoothieRequest,
            @ApiParam(value = "id", required = true) @PathVariable final Long id
    ) {
        Smoothie smoothie = smoothieRepository.findById(id).orElse(null);
        if (smoothie == null)
            return ResponseEntity.notFound().build();

        String newName = describeSmoothieRequest.getName();
        if (!smoothie.getName().equals(newName) && smoothieRepository.existsByName(newName)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Smoothie with given name is already taken!"));
        }

        if (describeSmoothieRequest.getName() != null)
            smoothie.setName(describeSmoothieRequest.getName());
        if (describeSmoothieRequest.getDescription() != null)
            smoothie.setDescription(describeSmoothieRequest.getDescription());
        if (describeSmoothieRequest.getNutritions() != null)
            smoothie.setNutritions(describeSmoothieRequest.getNutritions());
        smoothieRepository.save(smoothie);

        return ResponseEntity.ok().body(smoothie);
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
