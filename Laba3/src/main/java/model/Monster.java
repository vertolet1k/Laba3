/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author vika
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Monster {
    private int id;
    private String name;
    private String description;
    
    @JsonProperty("danger_level")
    private int dangerLevel;
    private String habitat;
    
    @JsonProperty("first_mention")
    private String firstMention;
    
    @JacksonXmlElementWrapper(localName = "immunities")
    @JacksonXmlProperty(localName = "immunity")
    private List<String> immunities = new ArrayList<>();
    
    @JacksonXmlElementWrapper(localName = "vulnerabilities")
    @JacksonXmlProperty(localName = "vulnerability")
    private List<String> vulnerabilities = new ArrayList<>();
    
    private String height;
    private String weight;
    private String activity;
    private Recipe recipe;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recipe {
        private String type;
        
        @JacksonXmlElementWrapper(localName = "ingredients")
        @JacksonXmlProperty(localName = "ingredient")
        private List<String> ingredients = new ArrayList<>();
        
        @JsonProperty("preparation_time")
        private String preparationTime;
        private String effectiveness;

        public String getType() { 
            return type; 
        }
        public void setType(String type) { 
            this.type = type; 
        }
        public List<String> getIngredients() { 
            return ingredients; 
        }
        public void setIngredients(List<String> ingredients) { 
            this.ingredients = ingredients != null ? ingredients : new ArrayList<>(); 
        }
        public String getPreparationTime() { 
            return preparationTime; 
        }
        public void setPreparationTime(String preparationTime) { 
            this.preparationTime = preparationTime; 
        }
        public String getEffectiveness() { 
            return effectiveness; 
        }
        public void setEffectiveness(String effectiveness) { 
            this.effectiveness = effectiveness; 
        }
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
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
    public int getDangerLevel() { 
        return dangerLevel; 
    }
    public void setDangerLevel(int dangerLevel) { 
        this.dangerLevel = dangerLevel; 
    }
    public String getHabitat() { 
        return habitat; 
    }
    public void setHabitat(String habitat) { 
        this.habitat = habitat; 
    }
    public String getFirstMention() { 
        return firstMention; 
    }
    public void setFirstMention(String firstMention) { 
        this.firstMention = firstMention; 
    }
    public List<String> getImmunities() { 
        return immunities; 
    }
    public void setImmunities(List<String> immunities) { 
        this.immunities = immunities != null ? immunities : new ArrayList<>(); 
    }
    public List<String> getVulnerabilities() { 
        return vulnerabilities; 
    }
    public void setVulnerabilities(List<String> vulnerabilities) { 
        this.vulnerabilities = vulnerabilities != null ? vulnerabilities : new ArrayList<>(); 
    }
    public String getHeight() { 
        return height; 
    }
    public void setHeight(String height) { 
        this.height = height; 
    }
    public String getWeight() { 
        return weight; 
    }
    public void setWeight(String weight) { 
        this.weight = weight; 
    }
    public String getActivity() { 
        return activity; 
    }
    public void setActivity(String activity) { 
        this.activity = activity; 
    }
    public Recipe getRecipe() { 
        return recipe; 
    }
    public void setRecipe(Recipe recipe) { 
        this.recipe = recipe; 
    }
    @Override
    public String toString() {
        return name;
    }
}