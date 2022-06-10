package com.anasdarai.assistant_diabtique;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.preference.PreferenceManager;

import com.anasdarai.assistant_diabtique.objs.Conseil;
import com.anasdarai.assistant_diabtique.objs.Mesure;
import com.anasdarai.assistant_diabtique.objs.Repas;
import com.anasdarai.assistant_diabtique.objs.Tache;
import com.anasdarai.assistant_diabtique.objs.TacheStat;
import com.anasdarai.assistant_diabtique.objs.typeDiabete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class db_manager extends SQLiteOpenHelper {


    public static final SimpleDateFormat dateFormatDb = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH);
    private static final String CREATE_table_taches = "CREATE TABLE taches (" +
            "id INTEGER," +
            "title TEXT NOT NULL," +
            "short_description TEXT," +
            "description TEXT," +
            "time TEXT," +
            "permanent	INTEGER DEFAULT 1," +
            "PRIMARY KEY(id AUTOINCREMENT));";
    private static final String CREATE_table_taches_logs = "CREATE TABLE taches_logs (" +
            "id_tache INTEGER," +
            "time TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY(id_tache) REFERENCES taches(id)" +
            ")";
    private static final String CREATE_table_mesures = "CREATE TABLE mesures (" +
            "id INTEGER," +
            "glycemie INTEGER," +
            "time TEXT DEFAULT CURRENT_TIMESTAMP," +
            "PRIMARY KEY(id AUTOINCREMENT)" +
            ")";
    private static final String CREATE_table_repas = "CREATE TABLE repas (" +
            "id INTEGER," +
            "name TEXT," +
            "quantity INTEGER," +
            "time TEXT DEFAULT CURRENT_TIMESTAMP," +
            "PRIMARY KEY(id AUTOINCREMENT)" +
            ")";
    private static final String CREATE_conseils = "CREATE TABLE Conseils (" +
            "id INTEGER," +
            "icon TEXT," +
            "title TEXT," +
            "description TEXT," +
            "PRIMARY KEY(id AUTOINCREMENT)" +
            ")";
    private static final String DATABASE_NAME = "userdata.db";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    public db_manager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_table_taches);
        database.execSQL(CREATE_table_taches_logs);

        database.execSQL(CREATE_table_repas);

        database.execSQL(CREATE_table_mesures);

        database.execSQL(CREATE_conseils);


        ArrayList<String> sqlRequests = new ArrayList<>();


        sqlRequests.add(new Tache("⚽Faire 30 minutes de sport.",
                "Une activité physique régulière améliore l''évolution du diabète\uD83D\uDE00",
                "", "", 1).toSQLInsert());

        // for (int i = 0; i < 50; i++)
        sqlRequests.add(new Tache("\uD83D\uDC68\u200D⚕Visiter votre médecin", "Trois mois se sont écoulés depuis votre dernière visite!", "", "91", 1).toSQLInsert());


        final typeDiabete typediabete = typeDiabete.typeFromVal(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("type_biabete", "1")));

        switch (typediabete) {
            case diabete_type_1:
                sqlRequests.add(new Tache("\uD83D\uDC89Prendre l''insuline basale.",
                        "L''insuline basale correspond à une ou deux injections d''insuline lente (elle couvre les besoins de la journée).",
                        "", "", 1).toSQLInsert());
                break;
            case diabete_type_2:
                sqlRequests.add("INSERT INTO taches (title, short_description, description, time) VALUES ('\uD83D\uDC8APrendre le médicament de matin.', '', '', '0.33');");
                sqlRequests.add("INSERT INTO taches (title, short_description, description, time) VALUES ('\uD83D\uDC8APrendre le médicament de nuit.', '', '', '0.875');");

                break;
        }


        for (String sql : sqlRequests
        ) {
            database.execSQL(sql);
        }


        insertAstuce(database, "\uD83D\uDC42", "Se méfier des produits raffinés",
                "Plutôt que des sucres et des farines raffinées qui font grimper la glycémie, privilégiez les aliments complets : leurs glucides se libèrent plus lentement. Le pain blanc, les céréales sucrées du petit-déjeuner, les biscuits et tous les aliments transformés sont donc à proscrire si l’on veut contrôler sa glycémie. L’idéal : prendre l’habitude de cuisiner en utilisant des farines complètes et des alternatives aux sucres raffinés dotées d’un index glycémique bas.");
        insertAstuce(database, "\uD83D\uDC69\u200D\uD83C\uDF3E", "Faire le plein de fibres",
                "Excellentes contre le cholestérol, elles diminueraient aussi les risques de cancer du colon : les fibres solubles sont particulièrement utiles aux diabétiques et à tous ceux qui cherchent à contrôler leur glycémie car elles ralentissent le processus de digestion, et permettent aux sucres de se diffuser progressivement dans le sang. En consommer régulièrement permet d’éviter les pics de la glycémie. On trouve ce type de fibres en quantité intéressante dans les lentilles, les haricots secs, l’avoine, l’orge ou encore les graines de psyllium.");
        insertAstuce(database, "\uD83C\uDFCB️\u200D♀", "Avoir une activité physique régulière",
                "Chez les diabétiques, le glucose contenu dans les aliments stagne dans le sang au lieu d’intégrer les cellules pour y être transformé. Faire de l’exercice engendre une dépense énergétique supérieure, ce qui contribue à faire baisser la glycémie. L’idéal : s’astreindre à une activité physique plutôt intense au moins 30 minutes, 3 fois par semaine minimum. Marche rapide, jogging, natation… à vous de choisir le sport qui vous conviendra le mieux.");
        insertAstuce(database, "\uD83D\uDCAA", "S’activer !",
                "Pour vous motiver à marcher davantage, vous pouvez utiliser un podomètre. L’Organisation mondiale de la santé recommande d’effectuer au minimum 10 000 pas par jour. C’est beaucoup ! On peut donc commencer par se fixer des objectifs réalistes pour augmenter progressivement la moyenne de pas effectués quotidiennement.");
        insertAstuce(database, "\uD83D\uDC40", "Tenter l’autosurveillance pour mieux agir sur la maladie",
                "L’autosurveillance ne doit pas devenir une obsession ou une source d’anxiété. Elle doit être vue comme un outil qui permet de prendre conscience de la réalité du diabète (dont on ne sent pas les symptômes), et des effets d’un écart alimentaire ou des efforts physiques qui, même modérés, sont très payants. Une à 4 mesures de glycémie par semaine sont suffisantes. Vous pouvez consigner les valeurs dans un carnet pour pouvoir mieux discuter avec votre médecin des éventuels ajustements à effectuer.");
        insertAstuce(database, "\uD83D\uDC4F", " Bien s'entourer et rester positif",
                "Le diabète n’est pas une maladie bénigne : il n’y a pas de traitement qui permette de le guérir et les complications peuvent être graves.\n" +
                        "\n" +
                        "Pour garder le moral et jouer un rôle proactif dans le traitement de votre maladie, il est impératif d’aller chercher tout le soutien dont vous aurez besoin. N'hésitez pas à en parler à votre famille ou à vos amis, mais également à des personnes qui sont elles aussi touchées par le diabète de type 2.");

        final SharedPreferences taches_expPref = context.getSharedPreferences("tachesExp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = taches_expPref.edit();
        editor.putBoolean("hyperglycemie", true);
        editor.putBoolean("hypoglycemie", true);
        editor.putBoolean("glycemie_apres_repas", true);
        editor.commit();
    }


    public void insertAstuce(SQLiteDatabase database, String icon, String title, String desc) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("icon", icon);
        contentValues.put("title", title);
        contentValues.put("description", desc);
        database.insert("conseils", null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public ArrayList<Tache> getAllTachesPermanents() {
        ArrayList<Tache> taches = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT * FROM taches WHERE permanent==1", null)) {
                cursor.moveToFirst();

                do {
                    Tache tache = new Tache();
                    tache.id = cursor.getInt(0);
                    tache.title = cursor.getString(1);

                    tache.short_description = cursor.getString(2);
                    tache.description = cursor.getString(3);
                    tache.time = cursor.getString(4);
                    tache.permanent = 1;
                    taches.add(tache);
                } while (cursor.moveToNext());

            }
        }

        return taches;
    }

    public ArrayList<Tache> getTachesPerForNotifcation() {
        ArrayList<Tache> taches = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT *\n" +
                            "FROM taches T\n" +
                            "WHERE permanent==1 " +
                            "and (id not in (" +
                            "SELECT id_tache\n" +
                            "FROM taches_logs TL\n" +
                            "WHERE DATE(TL.time)==DATE('now')) );"
                    , null)) {
                final boolean b = cursor.moveToFirst();
                if (b)
                    do {
                        String tm = cursor.getString(4);
                        boolean add = false;
                        if (tm.isEmpty()) {
                            add = true;
                        } else {
                            float f_tm = Float.parseFloat(tm);
                            if (1 <= f_tm) {
                                try (Cursor rawQuery = readableDatabase.rawQuery("SELECT id_tache " +
                                        "FROM taches_logs " +
                                        "WHERE id_tache==" + cursor.getInt(0) + " and DATE(time)<DATE('now','-" + f_tm + " day');", null)) {
                                    if (rawQuery.getCount() > 0)
                                        add = true;
                                    else {
                                        try (Cursor rawQuery2 = readableDatabase.rawQuery("SELECT id_tache " +
                                                "FROM taches_logs " +
                                                "WHERE id_tache==" + cursor.getInt(0), null)) {
                                            if (rawQuery2.getCount() == 0)
                                                add = true;
                                        }
                                    }
                                }
                            } /*else {
                                float diff = (int) (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) - f_tm * 24 * 60);
                                if (diff > 0)
                                    add = true;
                            }*/

                        }
                        if (add) {
                            Tache tache = new Tache();
                            tache.id = cursor.getInt(0);
                            tache.title = cursor.getString(1);
                            tache.short_description = cursor.getString(2);
                            tache.time = tm;
                            tache.permanent = 1;
                            taches.add(tache);
                        }
                    } while (cursor.moveToNext());

            }
        }

        return taches;
    }

    public ArrayList<Tache> getEnCoursTaches() {
        ArrayList<Tache> taches = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT *\n" +
                    "FROM taches T\n" +
                    "WHERE (id not in (" +
                    "SELECT id_tache\n" +
                    "FROM taches_logs TL\n" +
                    "WHERE DATE(TL.time)==DATE('now')) )" +
                    "and " +
                    "(permanent==1 or (permanent==0 and DATE(T.time)==DATE('now') and (T.time)<=CURRENT_TIMESTAMP));", null)) {
                final boolean b = cursor.moveToFirst();
                if (b)
                    do {
                        String tm = cursor.getString(4);
                        int perman = cursor.getInt(5);

                        boolean add = false;
                        if (tm.isEmpty() || perman == 0) {
                            add = true;
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            float f_tm = Float.parseFloat(tm);
                            if (1 > f_tm) {
                                float diff = (int) (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) - f_tm * 24 * 60);
                                if (diff > 0)
                                    add = true;
                            } else {
                                try (Cursor rawQuery = readableDatabase.rawQuery("SELECT id_tache " +
                                        "FROM taches_logs " +
                                        "WHERE id_tache==" + cursor.getInt(0) + " and DATE(time)<DATE('now','-" + f_tm + " day');", null)) {

                                    if (rawQuery.getCount() > 0)
                                        add = true;
                                    else {
                                        try (Cursor rawQuery2 = readableDatabase.rawQuery("SELECT id_tache " +
                                                "FROM taches_logs " +
                                                "WHERE id_tache==" + cursor.getInt(0), null)) {
                                            if (rawQuery2.getCount() == 0)
                                                add = true;
                                        }

                                    }

                                }
                            }

                        }
                        if (add) {
                            Tache tache = new Tache();
                            tache.id = cursor.getInt(0);
                            tache.title = cursor.getString(1);
                            tache.short_description = cursor.getString(2);
                            tache.description = cursor.getString(3);
                            tache.time = tm;
                            tache.permanent = perman;
                            taches.add(tache);
                        }
                    } while (cursor.moveToNext());

            }
        }

        return taches;
    }

    public ArrayList<Tache> getFinishedTaches() {
        ArrayList<Tache> taches = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT *\n" +
                    "FROM taches\n" +
                    "WHERE id in(SELECT id_tache \n" +
                    "\t\t\t\tFROM taches_logs TL \n" +
                    "\t\t\t\tWHERE DATE(TL.time)==DATE('now'));", null)) {
                final boolean b = cursor.moveToFirst();
                if (b)
                    do {
                        Tache tache = new Tache();
                        tache.id = cursor.getInt(0);
                        tache.title = cursor.getString(1);

                        tache.short_description = cursor.getString(2);
                        tache.description = cursor.getString(3);
                        tache.time = cursor.getString(4);
                        tache.isFinished = true;
                        tache.permanent = cursor.getInt(5);
                        taches.add(tache);
                    } while (cursor.moveToNext());

            }
        }

        return taches;
    }

    public ArrayList<TacheStat> getAllStatTaches(int lastNday) {
        ArrayList<TacheStat> tachesStat = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT id," +
                            "title," +
                            "       COUNT(*) * 100/" + lastNday +
                            " FROM   taches t" +
                            "       JOIN taches_logs tl" +
                            "         ON id = id_tache " +
                            "WHERE  t.TIME < 1" +
                            "       AND permanent == 1" +
                            "       AND DATE(tl.TIME) BETWEEN DATE('now', '-" +
                            lastNday +
                            " day') AND DATE('now') " +
                            "GROUP  BY id " +
                            "UNION " +
                            "SELECT id," +
                            "title," +
                            "       COUNT(*) * 100 * t.TIME/" + lastNday +
                            " FROM   taches t" +
                            "       JOIN taches_logs tl" +
                            "         ON id = id_tache " +
                            "WHERE  t.TIME >= 1 and t.TIME<=" + lastNday +
                            "       AND permanent == 1" +
                            "       AND DATE(tl.TIME) BETWEEN DATE('now', '-" +
                            lastNday +
                            " day') AND DATE('now') " +
                            "GROUP  BY id; "
                    , null)) {

                boolean b = cursor.moveToFirst();

                if (b)
                    do {
                        TacheStat tache = new TacheStat(cursor.getInt(0),
                                cursor.getString(1), cursor.getInt(2));
                        tachesStat.add(tache);
                    } while (cursor.moveToNext());

            }
        }

        return tachesStat;
    }

    public float avg_nb_mesures_day(int lastNday) {
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT Count(*) " +
                            "        FROM   mesures" +
                            "        WHERE  Date(time) BETWEEN Date('now', '-" +
                            lastNday +
                            " day') AND Date('now');",
                    null)) {
                cursor.moveToFirst();
                return cursor.getFloat(0) / lastNday;
            }
        }
    }

    public float avg_nb_hyper_day(int lastNday) {
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT Count(*)" +
                            "        FROM   mesures" +
                            "        WHERE  Date(time) BETWEEN Date('now', '-" +
                            lastNday +
                            " day') AND Date('now') " +
                            "AND glycemie >= 250" +
                            "       ; ",
                    null)) {
                cursor.moveToFirst();
                return cursor.getFloat(0) / lastNday;
            }
        }
    }

    public float avg_nb_hypo_day(int lastNday) {
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT Count(*) AS d" +
                            "        FROM   mesures" +
                            "        WHERE  Date(time) BETWEEN Date('now', '-" +
                            lastNday +
                            " day') AND Date('now') " +
                            "AND glycemie <= 70" +
                            " ; ",
                    null)) {
                cursor.moveToFirst();
                return cursor.getFloat(0) / lastNday;
            }
        }
    }

    public float avg_nb_repas_day(int lastNday) {
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT Count(*) " +
                            "        FROM   repas" +
                            "        WHERE  Date(time) BETWEEN Date('now', '-" +
                            lastNday +
                            " day') AND Date('now');",
                    null)) {
                cursor.moveToFirst();
                return cursor.getFloat(0) / lastNday;
            }
        }
    }

    public float avg_nb_qsucre_repas_day(int lastNday) {
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT sum(quantity)/Count(*) " +
                            "        FROM   repas" +
                            "        WHERE  Date(time) BETWEEN Date('now', '-" +
                            lastNday +
                            " day') AND Date('now');",
                    null)) {
                cursor.moveToFirst();
                return cursor.getFloat(0);
            }
        }
    }


    public void changeTacheStatus(Tache tache, boolean b) {
        try (SQLiteDatabase writeableDatabase = getWritableDatabase()) {
            if (b)
                writeableDatabase.execSQL("INSERT INTO taches_logs (id_tache) VALUES (" + tache.id + ");");
            else
                writeableDatabase.execSQL("DELETE FROM taches_logs WHERE id_tache=" + tache.id + ";");
            tache.isFinished = !tache.isFinished;
        }

    }

    public void insertTacheToDb(Tache tache) {
        try (SQLiteDatabase writeableDatabase = getWritableDatabase()) {
            writeableDatabase.execSQL(tache.toSQLInsert());
        }
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT max(id) FROM taches;", null)) {
                cursor.moveToFirst();
                tache.id = cursor.getInt(0);
            }
        }
    }

    public void deleteTache(Tache tache) {
        try (SQLiteDatabase writeableDatabase = getWritableDatabase()) {
            writeableDatabase.execSQL("DELETE FROM taches WHERE id=" + tache.id + ";");
            writeableDatabase.execSQL("DELETE FROM taches_logs WHERE id_tache=" + tache.id + ";");
        }

    }

    public void updateTache(Tache tache) {
        try (SQLiteDatabase writeableDatabase = getWritableDatabase()) {
            writeableDatabase.execSQL("UPDATE taches SET title=?,short_description=?,description=?,Time=?,Permanent=? WHERE id=?;"
                    , new Object[]{tache.title, tache.short_description, tache.description, tache.time, tache.permanent, tache.id});
        }

    }


    public ArrayList<Repas> getAllRepas() {
        ArrayList<Repas> repas = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT * FROM repas", null)) {
                final boolean b = cursor.moveToFirst();
                if (b)
                    do {
                        Repas rep = new Repas();
                        rep.id = cursor.getInt(0);
                        rep.name = cursor.getString(1);
                        rep.quantity = cursor.getInt(2);
                        rep.time = cursor.getString(3);
                        repas.add(rep);
                    } while (cursor.moveToNext());

            }
        }

        return repas;
    }

    public ArrayList<Repas> getRepasToday() {
        ArrayList<Repas> repas = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT * FROM repas where DATE(time)==DATE('now')", null)) {
                final boolean b = cursor.moveToFirst();
                if (b)
                    do {
                        Repas rep = new Repas();
                        rep.id = cursor.getInt(0);
                        rep.name = cursor.getString(1);
                        rep.quantity = cursor.getInt(2);
                        rep.time = cursor.getString(3);
                        repas.add(rep);
                    } while (cursor.moveToNext());

            }
        }

        return repas;
    }

    public void insertRepasToDb(Repas repas) {
        try (SQLiteDatabase writeableDatabase = getWritableDatabase()) {
            writeableDatabase.execSQL(repas.toSQLInsert());
        }
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT max(id),time FROM repas;", null)) {
                cursor.moveToFirst();
                repas.id = cursor.getInt(0);
                repas.time = cursor.getString(1);
            }
        }
    }


    public ArrayList<Mesure> getMesuresToday() {
        ArrayList<Mesure> mesures = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT * FROM mesures where DATE(time)==DATE('now') order by time", null)) {
                final boolean b = cursor.moveToFirst();
                if (b)
                    do {
                        Mesure mesure = new Mesure();
                        mesure.id = cursor.getInt(0);
                        mesure.glycemie = cursor.getInt(1);
                        mesure.time = cursor.getString(2);
                        mesures.add(mesure);
                    } while (cursor.moveToNext());

            }
        }

        return mesures;
    }

    public ArrayList<Mesure> getAllMesures() {
        ArrayList<Mesure> mesures = new ArrayList<>();
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT * FROM mesures order by time", null)) {
                final boolean b = cursor.moveToFirst();
                if (b)
                    do {
                        Mesure mesure = new Mesure();
                        mesure.id = cursor.getInt(0);
                        mesure.glycemie = cursor.getInt(1);
                        mesure.time = cursor.getString(2);
                        mesures.add(mesure);
                    } while (cursor.moveToNext());

            }
        }

        return mesures;
    }

    public void insertMesureToDb(Mesure mesure) {
        try (SQLiteDatabase writeableDatabase = getWritableDatabase()) {
            writeableDatabase.execSQL(mesure.toSQLInsert());
        }
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT max(id),time FROM mesures;", null)) {
                cursor.moveToFirst();
                mesure.id = cursor.getInt(0);
                mesure.time = cursor.getString(1);
            }
        }
    }


    public Conseil getConseilRan() {
        try (SQLiteDatabase readableDatabase = getReadableDatabase()) {
            try (Cursor cursor = readableDatabase.rawQuery("SELECT * FROM Conseils ORDER BY RANDOM() LIMIT 1", null)) {
                cursor.moveToFirst();
                return new Conseil(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
        }
    }


}
