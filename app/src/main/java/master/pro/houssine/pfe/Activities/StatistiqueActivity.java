package master.pro.houssine.pfe.Activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseListAccidentParJour;
import master.pro.houssine.pfe.Response.ResponsePourcentageMort;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatistiqueActivity extends AppCompatActivity {

    private PieChart pieChart;
    static BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    static int pD, pN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);
        pieChart = findViewById(R.id.activity_main_piechart);
        barChart = findViewById(R.id.BarChart);
        setupPieChart();
        pourcentageMorts();
        pourcentageMortsParJour();
    }


    private void pourcentageMortsParJour() {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseListAccidentParJour> call = api.listAccidentStat();
        call.enqueue(new Callback<ResponseListAccidentParJour>() {

            @Override
            public void onResponse(Call<ResponseListAccidentParJour> call, Response<ResponseListAccidentParJour> response) {
                if (response.isSuccessful()) {
                    int[] nbblessures = response.body().getBlessures();
                    int[] id = response.body().getId();
                    barEntries = new ArrayList<>();
                    for (int i = 0; i < nbblessures.length; i++) {
                        barEntries.add(new BarEntry(id[i], nbblessures[i]));
                    }
                    barDataSet = new BarDataSet(barEntries, "Nombre de blessures par rapport aux accidents");
                    barData = new BarData(barDataSet);
                    barChart.setData(barData);
                    barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(18f);
                }
            }

            @Override
            public void onFailure(Call<ResponseListAccidentParJour> call, Throwable t) {

            }
        });
    }


    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Taux de Dangereuse");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }


    private void pourcentageMorts() {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponsePourcentageMort> call = api.pourcentageMorts();
        call.enqueue(new Callback<ResponsePourcentageMort>() {

            @Override
            public void onResponse(Call<ResponsePourcentageMort> call, Response<ResponsePourcentageMort> response) {
                if (response.isSuccessful()) {
                    pD = response.body().getNbre_dangereux();
                    pN = response.body().getNbre_normale();

                    ArrayList<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(pN, "Normale"));
                    entries.add(new PieEntry(pD, "Dangereux"));


                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(Color.GREEN, Color.RED);

                    PieData data = new PieData(dataSet);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter(pieChart));
                    data.setValueTextSize(12f);
                    data.setValueTextColor(Color.BLACK);

                    pieChart.setData(data);
                    pieChart.invalidate();

                    pieChart.animateY(1400, Easing.EaseInOutQuad);

                }
            }

            @Override
            public void onFailure(Call<ResponsePourcentageMort> call, Throwable t) {

            }
        });
    }

}