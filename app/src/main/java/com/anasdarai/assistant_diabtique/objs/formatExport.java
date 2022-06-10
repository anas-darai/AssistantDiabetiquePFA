package com.anasdarai.assistant_diabtique.objs;

public enum formatExport {
    XML("XML","xml");

    final public String name;
    final public String extention;
    formatExport(String name,String extention) {
        this.name=name;
        this.extention=extention;
    }

}
