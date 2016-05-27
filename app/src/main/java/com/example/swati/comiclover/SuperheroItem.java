package com.example.swati.comiclover;



/**
 * Created by Swati on 25-05-2016.
 */
public class SuperheroItem {
    private String name, image;
    private int id;

    public SuperheroItem(){}

    public SuperheroItem(SuperheroItem si){
        this.name=si.getName();
        this.image=si.getImage();
        this.id=si.getId();
    }

    public SuperheroItem(int id,String name, String image){
        this.id=id;
        this.name=name;
        this.image=image;
    }

    public int getId(){return id;}
    public String getName(){return name;}
    public String getImage(){return image;}

    public void setId(int id){this.id=id;}
    public void setName(String name){
        this.name=name;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
