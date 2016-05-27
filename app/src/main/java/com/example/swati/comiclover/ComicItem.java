package com.example.swati.comiclover;

/**
 * Created by Swati on 25-05-2016.
 */
public class ComicItem {
    private String title,des,image,digitalurl;
    private int id,digitalid;

    public ComicItem(){}

    public ComicItem(ComicItem ci){
        this.id=ci.id;
        this.digitalid=ci.digitalid;
        this.title=ci.title;
        this.des=ci.des;
        this.image=ci.image;
        this.digitalid=ci.digitalid;
        this.digitalurl=ci.digitalurl;
    }

    public ComicItem(int id, int digitalid, String title, String des, String image, String digitalurl){
        this.id=id;
        this.digitalid=digitalid;
        this.title=title;
        this.des=des;
        this.image=image;
        this.digitalid=digitalid;
        this.digitalurl=digitalurl;
    }

    public int getId(){return id;}
    public int getDigitalid(){return digitalid;}
    public String getTitle(){return title;}
    public String getImage(){return image;}
    public String getDes(){return des;}
    public String getDigitalurl(){return digitalurl;}


    public void setId(int id){this.id=id;}
    public void setDigitalid(int digitalid){this.digitalid=digitalid;}
    public void setTitle(String title){this.title=title;}
    public void setImage(String image) {this.image = image;}
    public void setDes(String des){this.des=des;}
    public void setDigitalurl(String digitalurl){this.digitalurl=digitalurl;}

}
