package com.anasdarai.assistant_diabtique.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.anasdarai.assistant_diabtique.R;
import com.anasdarai.assistant_diabtique.objs.Mesure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewCourbe extends View {

    final Paint wallpaint = new Paint();
    final Path wallpath = new Path();

    public ViewCourbe(Context context) {
        super(context);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        nor_min_glucose= Integer.parseInt(sharedPreferences.getString("taux_glycemie_inf","75"));
        nor_max_glucose= Integer.parseInt(sharedPreferences.getString("taux_glycemie_sup","180"));

    }
    public ViewCourbe(Context context, AttributeSet attrs){
        super(context, attrs);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        nor_min_glucose= Integer.parseInt(sharedPreferences.getString("taux_glycemie_inf","75"));
        nor_max_glucose= Integer.parseInt(sharedPreferences.getString("taux_glycemie_sup","180"));

    }


    final int min_glucose=50;
    final int max_glucose=250;


    int nor_min_glucose=80,nor_max_glucose=180;

    private final ArrayList<point_glucose> Points=new ArrayList<>();



    public void setPoints(ArrayList<Mesure> mesures){
        Points.clear();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH);
        for (Mesure mesure:mesures
             ) {
            try {
                cal.setTime(sdf.parse(mesure.time));// all done
            } catch (ParseException e) {
                e.printStackTrace();
            }
            float tm=cal.get(Calendar.HOUR_OF_DAY)+(float)cal.get(Calendar.MINUTE)/60;
            Points.add(new point_glucose(tm, mesure.glycemie));
        }
//Collections.sort(Points, (t1, t2) -> Float.compare(t1.time, t2.time));
        requestLayout();
    }
    public void addPoint(Mesure mesure){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH);
        try {
            cal.setTime(sdf.parse(mesure.time));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }


        float tm=cal.get(Calendar.HOUR_OF_DAY)+(float)cal.get(Calendar.MINUTE)/60;


        final point_glucose pointGlucose = new point_glucose(tm, mesure.glycemie);


         int pos = Collections.binarySearch(Points, pointGlucose, (Comparator<Object>) (o1, o2) -> {
             point_glucose p1 = (point_glucose) o1;
             point_glucose p2 = (point_glucose) o2;
             return Float.compare(p1.time, p2.time);
         });

        if (pos<0)
            pos=-pos-1;
        Points.add(pos,pointGlucose);

       requestLayout();



    }
    @Override
    public void onDraw(Canvas canvas) {
/*setBackgroundColor(Color.parseColor("#ecf0f1"));
        Points.add(new point_glucose(4,120));
        Points.add(new point_glucose(8,90));
        Points.add(new point_glucose(10,190));
        Points.add(new point_glucose(15,100));*/

        wallpaint.setColor(Color.parseColor("#80ECF0F1"));
        wallpaint.setStyle(Paint.Style.FILL);

            float h_max=getHeight()*(1-(float)(nor_max_glucose-min_glucose)/(max_glucose-min_glucose));

            float h_min=getHeight()*(1-(float)(nor_min_glucose-min_glucose)/(max_glucose-min_glucose));

            wallpath.reset();
            wallpath.moveTo(0, h_max);
            wallpath.lineTo(getWidth(), h_max);
        wallpath.lineTo(getWidth(), h_min);
        wallpath.lineTo(0, h_min);
        wallpath.lineTo(0, h_max);

            canvas.drawPath(wallpath, wallpaint);

        wallpaint.setTextSize(30);

        wallpaint.setColor(Color.parseColor("#75C0392B"));
        canvas.drawText(nor_max_glucose+"",0,h_max-10,wallpaint);


        Rect bounds = new Rect();
        wallpaint.getTextBounds(nor_min_glucose+"",0,(nor_min_glucose+"").length(),bounds);
        canvas.drawText(nor_min_glucose+"",0,h_min+bounds.bottom + bounds.height()+10,wallpaint);




        wallpaint.setColor(getResources().getColor(R.color.line_out_safe));
        wallpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        wallpaint.setStrokeWidth(4);
        canvas.drawLine(0, h_max,getWidth(), h_max,wallpaint);
        canvas.drawLine(0, h_min,getWidth(), h_min,wallpaint);


        wallpath.reset();
        wallpaint.setColor(getResources().getColor(R.color.line_bitween_point));
        wallpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        wallpaint.setStrokeWidth(6);
        for (int i = 0; i < Points.size()-1; i++) {

            float w1=(Points.get(i).time/4)*getWidth()/6-(float)getWidth()/12;
            float h1=getHeight()*(1-(float)(Points.get(i).glucose_val-min_glucose)/(max_glucose-min_glucose));


            float w2=(Points.get(i+1).time/4)*getWidth()/6-(float)getWidth()/12;
            float h2=getHeight()*(1-(float)(Points.get(i+1).glucose_val-min_glucose)/(max_glucose-min_glucose));



            wallpath.reset();
            wallpath.moveTo(w1, h1);
            wallpath.lineTo(w2, h2);
            canvas.drawPath(wallpath, wallpaint);
        }


        wallpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (point_glucose time:Points
        ) {
            float w=(time.time/4)*getWidth()/6-(float)getWidth()/12;
            float h=getHeight()*(1-(float)(time.glucose_val-min_glucose)/(max_glucose-min_glucose));


            wallpath.reset();
            if (h_min>=h&&h>=h_max){
                wallpaint.setColor(getResources().getColor(R.color.point_in));
            }else
                wallpaint.setColor(getResources().getColor(R.color.point_out));
            wallpath.addCircle(w,h,25, Path.Direction.CW);
            canvas.drawPath(wallpath, wallpaint);
        }



    }

    private static class point_glucose{
        final float time;
        final int glucose_val;


        point_glucose(float time, int glucose_val) {
            this.time = time;
            this.glucose_val = glucose_val;
        }
    }

}
