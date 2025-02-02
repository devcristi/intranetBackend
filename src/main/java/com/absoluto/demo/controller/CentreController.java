package com.absoluto.demo.controller;

import com.absoluto.demo.centre.Centre;
import com.absoluto.demo.service.CentreService;
import com.absoluto.demo.repository.CentreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/centres")
public class CentreController {

    @Autowired
    private CentreService centreService;

    // Injectează CentreRepository
    @Autowired
    private CentreRepository centreRepository;

    // Endpoint pentru crearea unui centru
    @PostMapping
    public ResponseEntity<Centre> createCentre(@RequestBody Centre centre) {
        Centre createdCentre = centreService.createCentre(centre);
        return ResponseEntity.ok(createdCentre);
    }

    // Endpoint pentru asignarea unui utilizator la un centru
    @PutMapping("/{centreId}/assign-user")
    public ResponseEntity<Centre> assignUserToCentre(
            @PathVariable Long centreId,
            @RequestParam Long userId) {
        Centre updatedCentre = centreService.assignUserToCentre(centreId, userId);
        return ResponseEntity.ok(updatedCentre);
    }

    // Endpoint GET pentru a prelua lista centrelor
    @GetMapping
    public ResponseEntity<List<Centre>> getCentres() {
        List<Centre> centres = centreRepository.findAll();
        return ResponseEntity.ok(centres);
    }
}
