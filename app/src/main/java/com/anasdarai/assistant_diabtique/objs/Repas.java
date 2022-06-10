package com.anasdarai.assistant_diabtique.objs;

public class Repas {
    public int id;
    public String name;
    public int quantity;
    public String time;

    public Repas(){

    }

    public Repas(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String toSQLInsert(){
        return String.format("INSERT INTO repas (name, quantity) VALUES ('%s', '%d');",name,quantity);
    }
    public String exportString(formatExport format){
        switch (format){
            case XML:
                return String.format("\t<rep id=\"%d\">\n" +
                        "\t\t<time>%s</time>\n" +
                        "\t\t<name>%s</name>\n" +
                        "\t\t<quantity>%d</quantity>\n" +
                        "\t</rep>\n",id,time,name,quantity
                );
        }
        return "";
    }
}
