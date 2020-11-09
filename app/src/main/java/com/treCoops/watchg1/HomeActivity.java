package com.treCoops.watchg1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.highsoft.highcharts.Common.HIChartsClasses.HIChart;
import com.highsoft.highcharts.Common.HIChartsClasses.HIColumn;
import com.highsoft.highcharts.Common.HIChartsClasses.HIOptions;
import com.highsoft.highcharts.Common.HIChartsClasses.HIPlotOptions;
import com.highsoft.highcharts.Common.HIChartsClasses.HISeries;
import com.highsoft.highcharts.Common.HIChartsClasses.HISubtitle;
import com.highsoft.highcharts.Common.HIChartsClasses.HITitle;
import com.highsoft.highcharts.Common.HIChartsClasses.HITooltip;
import com.highsoft.highcharts.Common.HIChartsClasses.HIXAxis;
import com.highsoft.highcharts.Common.HIChartsClasses.HIYAxis;
import com.highsoft.highcharts.Common.HIColor;
import com.highsoft.highcharts.Core.HIChartView;
import com.treCoops.watchg1.Util.AlertBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    HIChartView hiChartView;
    ArrayList<Double> ColumnData = new ArrayList<>();
    ArrayList<HISeries> series = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ImageView imgStatus = findViewById(R.id.imgStatus);
        final TextView txtStatus = findViewById(R.id.txtStatus);
        final ImageView imgSetting = findViewById(R.id.imgSetting);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("mobile").child("sitted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(Long.class) == 0){
                    imgStatus.setImageResource(R.drawable.stand);
                    txtStatus.setText(R.string.stainding);
                }else{
                    txtStatus.setText(R.string.sitting);
                    imgStatus.setImageResource(R.drawable.waiting);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase error", "loadPost:onCancelled", databaseError.toException());
                AlertBar.notifyDanger(HomeActivity.this, "Data Fetching Failed!");
            }
        });

        hiChartView = findViewById(R.id.chart);
        final HIOptions option = new HIOptions();
        hiChartView.setWillNotDraw(true);

        final HITitle title_MSV = new HITitle();
        title_MSV.setText("Sitting Summery");

        final HIChart chart = new HIChart();
        chart.setType("column");
        option.setChart(chart);

        final HISubtitle subtitle_MSV = new HISubtitle();
        subtitle_MSV.setText("Day wise");

        final HITooltip tooltip = new HITooltip();
        tooltip.setHeaderFormat("<span style=\"font-size:15px\">{point.key}</span><table>");
        tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>" + "<td style=\"padding:0\"><b>{point.y}</b></td></tr>");
        tooltip.setFooterFormat("</table>");
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        
        myRef.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Double> ColumnData = new ArrayList<>();
                ArrayList<String> series1 = new ArrayList<>();
                series1.clear();
                Double val = 0.0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.e("Time", convertSeconds(snapshot.getValue(Integer.class)));
//                    val = (snapshot.getValue(Double.class).doubleValue()/3600);
//                    val = convertSeconds(snapshot.getValue(Double.class));
//                    ColumnData.add((double) Math.round(val*10)/10.0);
                    ColumnData.add(Double.parseDouble(convertSeconds(snapshot.getValue(Integer.class))));
                    series1.add(snapshot.getKey());

                }

                final HIPlotOptions plotOptions = new HIPlotOptions();
                plotOptions.setColumn(new HIColumn());
                plotOptions.getColumn().setPointPadding(0.2);
                plotOptions.getColumn().setBorderWidth(0);

                final HIXAxis hixAxis = new HIXAxis();
                hixAxis.setCategories(new ArrayList<>(series1));
                HITitle xTitle = new HITitle();
                xTitle.setText("Days");
                hixAxis.setTitle(xTitle);

                final HIYAxis hiyAxis = new HIYAxis();
                HITitle yTitle = new HITitle();
                yTitle.setText("Sitting Hours (hr)");
                hiyAxis.setTitle(yTitle);

                final HIColumn MSDataColumn = new HIColumn();
                MSDataColumn.setName("Sitting Hours (hr)");
                MSDataColumn.setBorderRadius(4);
                MSDataColumn.setColor(HIColor.initWithHexValue("03BF6F"));
                MSDataColumn.setBorderColor(HIColor.initWithHexValue("019355"));
                MSDataColumn.setBorderWidth(3);
                MSDataColumn.setShowInLegend(false);

                MSDataColumn.setData(ColumnData);

                series.add(MSDataColumn);

                option.setTitle(title_MSV);
                option.setSubtitle(subtitle_MSV);
                option.setXAxis(new ArrayList(){{add(hixAxis);}});
                option.setYAxis(new ArrayList(){{add(hiyAxis);}});
                option.setTooltip(tooltip);
                option.setPlotOptions(plotOptions);
                option.setSeries(series);
                option.setChart(chart);
                hiChartView.setWillNotDraw(false);
                hiChartView.setOptions(option);
                hiChartView.reload();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase error", "loadPost:onCancelled", databaseError.toException());
                AlertBar.notifyDanger(HomeActivity.this, "Data Fetching Failed!");
            }
        });



        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                Animatoo.animateSlideLeft(HomeActivity.this);
                finish();
            }
        });
    }

    public static String convertSeconds(int seconds) {
        int h = seconds/ 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        String sh = (h > 0 ? String.valueOf(h) + "." + "" : "0");
        String sm = (m < 10 && m > 0 && h > 0 ? "0" : "") + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : String.valueOf(m) + "" + "") : "00");
//        String ss = (s == 0 && (h > 0 || m > 0) ? "" : (s < 10 && (h > 0 || m > 0) ? "0" : "") + String.valueOf(s) + " " + "sec");
//        return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
        return sh + (h > 0 ? "" : ".") + sm + (m > 0 ? "" : "");
    }
}