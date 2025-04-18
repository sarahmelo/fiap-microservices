package br.com.fiap.aspersor.service;

import br.com.fiap.aspersor.dto.RegistrationSprinklerDTO;
import br.com.fiap.aspersor.dto.SprinklerDTO;
import br.com.fiap.aspersor.dto.UpdateSprinklerDTO;
import br.com.fiap.aspersor.model.Sprinkler;
import br.com.fiap.aspersor.model.User;
import br.com.fiap.aspersor.repository.SprinklerRepository;
import br.com.fiap.aspersor.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SprinklerService {
    @Autowired
    SprinklerRepository repository;

    @Autowired
    UserRepository userRepository;

    public SprinklerDTO save(RegistrationSprinklerDTO registrationSprinkler) {
        Sprinkler sprinkler = new Sprinkler();
        BeanUtils.copyProperties(registrationSprinkler, sprinkler);
        System.out.println("----> registrationSprinkler" + registrationSprinkler);
        System.out.println("----> sprinkler" + registrationSprinkler);

        User user = userRepository.findById(registrationSprinkler.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        sprinkler.setUser(user);

        Sprinkler savedSprinkler = repository.save(sprinkler);

        return new SprinklerDTO(savedSprinkler);
    }

    public SprinklerDTO findById(Long id) {
        Optional<Sprinkler> sprinkler =
                repository.findById(id);

        if (sprinkler.isPresent()) {
            return new SprinklerDTO(sprinkler.get());
        } else {
            throw new RuntimeException("Sprinkler not found.");
        }
    }

    public Page<SprinklerDTO> findAll(Pageable pagination) {
        return repository.findAll(pagination).map(SprinklerDTO::new);
    }

    public void delete(Long id) {
        Optional<Sprinkler> sprinkler = repository.findById(id);

        if (sprinkler.isPresent()) {
            repository.delete(sprinkler.get());
        } else {
            throw new RuntimeException("Sprinkler not found");
        }
    }

    public SprinklerDTO update(Long id, UpdateSprinklerDTO registrationSprinkler) {
        Optional<Sprinkler> optionalSprinkler = repository.findById(id);

        if (optionalSprinkler.isPresent()) {
            Sprinkler existingSprinkler = optionalSprinkler.get();
            BeanUtils.copyProperties(registrationSprinkler, existingSprinkler, "id"); // Excluir "id" para não sobrescrever

            Sprinkler updatedSprinkler = repository.save(existingSprinkler);

            return new SprinklerDTO(updatedSprinkler);
        } else {
            throw new RuntimeException("Sprinkler not found.");
        }
    }
}
