package io.sevcik.csdemo.models;

import jakarta.persistence.*;
@Entity
@Table(name = "smoothies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
        })
public class Smoothie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String nutritions;

    public Smoothie() {}

    public Smoothie(String name, String description, String nutritions) {
        this.setName(name);
        this.setDescription(description);
        this.setNutritions(nutritions);
    }

    public Smoothie(Long id, String username, String passhash) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setNutritions(nutritions);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNutritions() {
        return nutritions;
    }

    public void setNutritions(String nutritions) {
        this.nutritions = nutritions;
    }

}
