package com.anasdarai.assistant_diabtique.objs;

public enum typeDiabete {
    diabete_type_1(1,"Diabétique de type I"),diabete_type_2(2,"Diabétique de type II");
    private final int valeur;
    private final String txt;
     typeDiabete(int valeur,String txt) {
        this.valeur = valeur;
        this.txt=txt;
    }
    public int getValeur() {
        return this.valeur;
    }
    public String getText() {
         return txt;
    }
    public static typeDiabete typeFromVal(int v){
        for (typeDiabete db:typeDiabete.values()
             ) {
            if(db.valeur==v)
                return db;
        }
        return null;
    }

}
