package io.sevcik.csdemo.repositories;

import io.sevcik.csdemo.models.Smoothie;
import io.sevcik.csdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmoothieRepository extends JpaRepository<Smoothie, Long> {
    Optional<Smoothie> findByName(String name);
    boolean existsByName(String name);
}