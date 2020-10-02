package edu.cnm.deepdive.animals.model;

import com.google.gson.annotations.SerializedName;

public class Animals {
  private String name;
  private Taxonomy taxonomy;
  private String location;
  private Speed speed;
  private String diet;
  private String lifespan;
  private String imageUrl;

  public String getName() {
    return name;
  }

  public Taxonomy getTaxonomy() {
    return taxonomy;
  }

  public String getLocation() {
    return location;
  }

  public Speed getSpeed() {
    return speed;
  }

  public String getDiet() {
    return diet;
  }

  public String getLifespan() {
    return lifespan;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  @SerializedName("image")
  public void setName(String name) {
    this.name = name;
  }

  public void setTaxonomy(Taxonomy taxonomy) {
    this.taxonomy = taxonomy;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setSpeed(Speed speed) {
    this.speed = speed;
  }

  public void setDiet(String diet) {
    this.diet = diet;
  }

  public void setLifespan(String lifespan) {
    this.lifespan = lifespan;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
