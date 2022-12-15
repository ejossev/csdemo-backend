package io.sevcik.csdemo.models;

import jakarta.persistence.*;
@Entity
@Table(name = "smoothies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
        })
@SecondaryTable(name = "nutritions", pkJoinColumns = @PrimaryKeyJoinColumn(name = "smoothie_id"))
public class Smoothie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @Column(table = "nutritions")
    private Integer calories;
    @Column(table = "nutritions")
    private Integer proteins;
    @Column(table = "nutritions")
    private Integer fat;
    @Column(table = "nutritions")
    private Integer carbs;

    public Smoothie() {}

    public Smoothie(String name, String description, Integer calories, Integer sugar, Integer fat, Integer carbs) {
        this.setName(name);
        this.setDescription(description);
        this.setCalories(calories);
        this.setProteins(sugar);
        this.setFat(fat);
        this.setCarbs(carbs);
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

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProteins() {
        return proteins;
    }

    public void setProteins(Integer sugar) {
        this.proteins = sugar;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }
}
