package com.example.lab6_20206466;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

public class ResumenFragment extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;
    private FirebaseFirestore firestore;
    private String userId;

    public ResumenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return view;
        }

        firestore = FirebaseFirestore.getInstance();
        userId = user.getUid();

        obtenerDatosYGraficar();

        return view;
    }

    private void obtenerDatosYGraficar() {
        CollectionReference linea1Ref = firestore.collection("usuarios").document(userId).collection("movimientos_linea1");
        CollectionReference limaPassRef = firestore.collection("usuarios").document(userId).collection("movimientos_lima_pass");

        Task<List<Linea1>> linea1Task = linea1Ref.get().continueWith(task -> {
            List<Linea1> lista = new ArrayList<>();
            for (QueryDocumentSnapshot doc : task.getResult()) {
                lista.add(doc.toObject(Linea1.class));
            }
            return lista;
        });

        Task<List<LimaPass>> limaPassTask = limaPassRef.get().continueWith(task -> {
            List<LimaPass> lista = new ArrayList<>();
            for (QueryDocumentSnapshot doc : task.getResult()) {
                lista.add(doc.toObject(LimaPass.class));
            }
            return lista;
        });

        Tasks.whenAllSuccess(linea1Task, limaPassTask).addOnSuccessListener(results -> {
            List<Linea1> linea1 = (List<Linea1>) results.get(0);
            List<LimaPass> limaPass = (List<LimaPass>) results.get(1);

            graficarBarrasPorMes(linea1, limaPass);
            graficarTortaPorUso(linea1.size(), limaPass.size());
        });
    }
    private void graficarBarrasPorMes(List<Linea1> linea1, List<LimaPass> limaPass) {
        Map<String, Integer> linea1PorMes = new TreeMap<>();
        Map<String, Integer> limaPassPorMes = new TreeMap<>();

        DateFormat formatoMes = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Agrupar viajes de Línea 1 por mes
        for (Linea1 l : linea1) {
            try {
                String mes = formatoMes.format(formatoFecha.parse(l.getFecha()));
                linea1PorMes.put(mes, linea1PorMes.getOrDefault(mes, 0) + 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Agrupar viajes de Lima Pass por mes
        for (LimaPass l : limaPass) {
            try {
                String mes = formatoMes.format(formatoFecha.parse(l.getFecha()));
                limaPassPorMes.put(mes, limaPassPorMes.getOrDefault(mes, 0) + 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Unir los meses únicos de ambas fuentes
        Set<String> mesesUnificados = new TreeSet<>();
        mesesUnificados.addAll(linea1PorMes.keySet());
        mesesUnificados.addAll(limaPassPorMes.keySet());

        List<BarEntry> linea1Entries = new ArrayList<>();
        List<BarEntry> limaPassEntries = new ArrayList<>();
        List<String> mesesLabels = new ArrayList<>();

        // Parámetros de espacio para agrupación
        float groupSpace = 0.4f;
        float barSpace = 0.05f;
        float barWidth = 0.25f;

        float x = 0f;
        for (String mes : mesesUnificados) {
            mesesLabels.add(mes);
            linea1Entries.add(new BarEntry(x, linea1PorMes.getOrDefault(mes, 0)));
            limaPassEntries.add(new BarEntry(x, limaPassPorMes.getOrDefault(mes, 0)));
            x += 1f;
        }

        // Crear conjuntos de barras
        BarDataSet set1 = new BarDataSet(linea1Entries, "Línea 1");
        set1.setColor(Color.parseColor("#1976D2"));
        BarDataSet set2 = new BarDataSet(limaPassEntries, "Lima Pass");
        set2.setColor(Color.parseColor("#F57C00")); // Naranja

        // Configurar datos de gráfico
        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth);

        // Configurar eje X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(mesesLabels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(mesesUnificados.size()); // ← importante para mostrar todas las barras

        // Configurar gráfico
        barChart.setData(data);
        barChart.groupBars(0f, groupSpace, barSpace);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setWordWrapEnabled(true);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.invalidate();
    }


    private void graficarTortaPorUso(int totalLinea1, int totalLimaPass) {
        List<PieEntry> entries = new ArrayList<>();
        if (totalLinea1 > 0) entries.add(new PieEntry(totalLinea1, "Línea 1"));
        if (totalLimaPass > 0) entries.add(new PieEntry(totalLimaPass, "Lima Pass"));

        PieDataSet dataSet = new PieDataSet(entries, "Uso de Tarjetas");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(14f);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.invalidate();
    }


}
