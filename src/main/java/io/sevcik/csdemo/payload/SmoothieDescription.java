package io.sevcik.csdemo.payload;

import io.sevcik.csdemo.models.Smoothie;
import jakarta.validation.constraints.NotBlank;


public class SmoothieDescription {
    @NotBlank
    private String name;
    private String description;
    private Nutritions nutritions;

    public static class Nutritions {
        private Integer calories;

        private Integer proteins;

        private Integer fat;

        private Integer carbs;

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

        public Integer getCarbs() {
            return carbs;
        }

        public void setCarbs(Integer carbs) {
            this.carbs = carbs;
        }

        public Integer getFat() {
            return fat;
        }

        public void setFat(Integer fats) {
            this.fat = fats;
        }
    }

    public Nutritions getNutritions() {
        return nutritions;
    }

    public void setNutritions(Nutritions nutritions) {
        this.nutritions = nutritions;
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

    static public SmoothieDescription fromSmoothie(Smoothie smoothie) {
        SmoothieDescription rv = new SmoothieDescription();
        Nutritions n = new Nutritions();
        n.setCalories(smoothie.getCalories());
        n.setProteins(smoothie.getProteins());
        n.setFat(smoothie.getFat());
        n.setCarbs(smoothie.getCarbs());
        rv.setNutritions(n);
        rv.setName(smoothie.getName());
        rv.setDescription(smoothie.getDescription());
        return rv;
    }

}