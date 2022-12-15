package io.sevcik.csdemo.repositories;

import io.sevcik.csdemo.models.Smoothie;
import io.sevcik.csdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmoothieRepository extends JpaRepository<Smoothie, Long> {
    @Query(
            value = "SELECT " +
                    "s.id as id, " +
                    "s.name as name, " +
                    "s.description as description, " +
                    "n.calories as calories, " +
                    "n.proteins as proteins, " +
                    "n.fat as fat, " +
                    "n.carbs as carbs " +
                    "FROM smoothies s LEFT JOIN nutritions n " +
                    "ON s.id=n.smoothie_id WHERE s.name= ?1",
            nativeQuery = true)
    Optional<Smoothie> findByName(String name);
    @Query(value = "SELECT CASE WHEN EXISTS ( SELECT 1  FROM smoothies WHERE name= ?1 ) THEN 'true' ELSE 'false' END",
            nativeQuery = true)
    boolean existsByName(String name);
}
