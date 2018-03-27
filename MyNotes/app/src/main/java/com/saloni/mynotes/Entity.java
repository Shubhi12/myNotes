package com.saloni.mynotes;

/**
 * Created by SALONI on 04-07-2017.
 */

public class Entity {

    public String date;
    public String title;
    public String remark;
    public int id;

    public Entity(){
        super();
    }

    public Entity( int id,String title, String date,String remark) {
        super();
        this.date = date;
        this.title = title;
        this.remark = remark;
        this.id=id;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate(){
      return  date;
    }

    public  String getTitle(){
        return  title;
    }
    public String getRemark()
    {
        return remark;
    }
    public void setRemark(String remark){
        this.remark=remark;
    }
    public void setID(int id){
        this.id=id;
    }
    public int getID(){
        return id;
    }
}
