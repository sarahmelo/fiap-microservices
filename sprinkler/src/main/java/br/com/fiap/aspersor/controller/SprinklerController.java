package br.com.fiap.aspersor.controller;

import br.com.fiap.aspersor.dto.RegistrationSprinklerDTO;
import br.com.fiap.aspersor.dto.SprinklerDTO;
import br.com.fiap.aspersor.dto.UpdateSprinklerDTO;
import br.com.fiap.aspersor.service.SprinklerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SprinklerController {
    @Autowired
    private SprinklerService service;

    @PostMapping("/sprinkler")
    @ResponseStatus(HttpStatus.CREATED)
    public SprinklerDTO save(@RequestBody RegistrationSprinklerDTO sprinkler) {
        return service.save(sprinkler);
    }

    @GetMapping("/sprinkler/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SprinklerDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/sprinkler")
    @ResponseStatus(HttpStatus.OK)
    public Page<SprinklerDTO> findAll(@PageableDefault(size = 20, page = 0) Pageable pagination) {
        return service.findAll(pagination);
    }

    @PutMapping("/sprinkler/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SprinklerDTO> update(@PathVariable Long id, @RequestBody UpdateSprinklerDTO sprinkler) {
        try {
            SprinklerDTO updatedSprinkler = service.update(id, sprinkler);
            return ResponseEntity.ok(updatedSprinkler);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/sprinkler/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
