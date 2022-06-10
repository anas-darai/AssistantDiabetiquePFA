package com.anasdarai.assistant_diabtique.objs;

import java.io.Serializable;

public class Tache implements Serializable {

     public int id;
     public String title;
     public String description;
     public String short_description;
     public String time;
     public boolean isFinished;
     public Tache(){

     }
     public Tache(String title, String short_description, String description, String time, int permanent) {
          this.title = title;
          this.description = description;
          this.short_description = short_description;
          this.time = time;
          this.permanent = permanent;
     }

     public int permanent;

     public String toSQLInsert(){
          if (time!=null)
          return String.format("INSERT INTO taches (title, short_description,description, time,permanent) VALUES ('%s', '%s', '%s', '%s','%d');",title,short_description,description,time,permanent);
          else
               return String.format("INSERT INTO taches (title, short_description,description, time,permanent) VALUES ('%s', '%s', '%s',  datetime(),'%d');",title,short_description,description,permanent);
     }

}
