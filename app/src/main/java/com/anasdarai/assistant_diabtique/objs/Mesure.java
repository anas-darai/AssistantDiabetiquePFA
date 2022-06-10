package com.anasdarai.assistant_diabtique.objs;

import static com.anasdarai.assistant_diabtique.db_manager.dateFormatDb;

import java.util.Calendar;

public class Mesure {

    public int id;
    public int glycemie;
    public String time;

    public Mesure(){

    }
    public Mesure(int glycemie, Calendar time) {
        this.glycemie = glycemie;
        this.time=dateFormatDb.format(time.getTime());

    }

    public String toSQLInsert(){
        if (time==null)
        return String.format("INSERT INTO mesures (glycemie) VALUES ('%d');",glycemie);
        else
            return String.format("INSERT INTO mesures (glycemie,time) VALUES ('%d','%s');",glycemie,time);
    }

    public String exportString(formatExport format){
        switch (format){
            case XML:
             return String.format("\t<Mesure id=\"%d\">\n" +
                     "\t\t<time>%s</time>\n" +
                     "\t\t<glycemie>%d</glycemie>\n" +
                     "\t</Mesure>\n",id,time,glycemie
                     );
        }
return "";
    }
}
