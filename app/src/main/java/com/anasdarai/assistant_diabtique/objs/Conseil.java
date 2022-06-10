package com.anasdarai.assistant_diabtique.objs;

public class Conseil {
    final public int id;
    final public String title;
    final public String description;
    final public String emoji;

    public Conseil(int id,String icon, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.emoji=icon;
    }


}
