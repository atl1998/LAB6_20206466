package com.example.lab6_20206466;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LimaPassFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAgregar;
    private LimaPassAdapter adapter;
    private List<LimaPass> listaMovimientos = new ArrayList<>();
    private FirebaseFirestore firestore;
    private CollectionReference coleccion;

    public LimaPassFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lima_pass, container, false);

        recyclerView = view.findViewById(R.id.recyclerMovimientos);
        fabAgregar = view.findViewById(R.id.fabAgregar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LimaPassAdapter(listaMovimientos, this::mostrarDialogoOpciones);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            coleccion = firestore
                    .collection("usuarios")
                    .document(user.getUid())
                    .collection("movimientos_lima_pass");
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        fabAgregar.setOnClickListener(v -> mostrarDialogoNuevoMovimiento());

        cargarMovimientos();

        return view;
    }

    private void cargarMovimientos() {
        coleccion.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Error al leer Firestore", Toast.LENGTH_SHORT).show();
                return;
            }

            listaMovimientos.clear();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                LimaPass movimiento = doc.toObject(LimaPass.class);
                listaMovimientos.add(movimiento);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void mostrarDialogoNuevoMovimiento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nuevo Movimiento");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_lima_pass, null);
        builder.setView(dialogView);

        EditText etTarjeta = dialogView.findViewById(R.id.etTarjetaId);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        etFecha.setFocusable(false); // Para evitar que aparezca el teclado
        etFecha.setOnClickListener(v -> mostrarDatePicker(etFecha));
        EditText etEntrada = dialogView.findViewById(R.id.etParaderoEntrada);
        EditText etSalida = dialogView.findViewById(R.id.etParaderoSalida);
        EditText etTiempo = dialogView.findViewById(R.id.etTiempoViaje);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String id = UUID.randomUUID().toString();
            LimaPass movimiento = new LimaPass(
                    id,
                    etTarjeta.getText().toString(),
                    etFecha.getText().toString(),
                    etEntrada.getText().toString(),
                    etSalida.getText().toString(),
                    etTiempo.getText().toString()
            );

            coleccion.document(id).set(movimiento)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(getContext(), "Guardado en Firestore", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoOpciones(LimaPass movimiento) {
        String[] opciones = {"Editar", "Eliminar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Opciones para la tarjeta");
        builder.setItems(opciones, (dialog, which) -> {
            if (which == 0) {
                mostrarDialogoEditarMovimiento(movimiento);
            } else if (which == 1) {
                coleccion.document(movimiento.getId()).delete()
                        .addOnSuccessListener(unused ->
                                Toast.makeText(getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Error al eliminar: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
        builder.show();
    }

    private void mostrarDialogoEditarMovimiento(LimaPass movimiento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Editar Movimiento");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_lima_pass, null);
        builder.setView(dialogView);

        EditText etTarjeta = dialogView.findViewById(R.id.etTarjetaId);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        EditText etEntrada = dialogView.findViewById(R.id.etParaderoEntrada);
        EditText etSalida = dialogView.findViewById(R.id.etParaderoSalida);
        EditText etTiempo = dialogView.findViewById(R.id.etTiempoViaje);

        // Rellenar campos actuales
        etTarjeta.setText(movimiento.getTarjetaId());
        etFecha.setText(movimiento.getFecha());
        etEntrada.setText(movimiento.getParaderoEntrada());
        etSalida.setText(movimiento.getParaderoSalida());
        etTiempo.setText(movimiento.getTiempoViaje());


        // Hacer que el campo fecha muestre calendario
        etFecha.setFocusable(false);
        etFecha.setOnClickListener(v -> mostrarDatePicker(etFecha));

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            movimiento.setTarjetaId(etTarjeta.getText().toString());
            movimiento.setFecha(etFecha.getText().toString());
            movimiento.setParaderoEntrada(etEntrada.getText().toString());
            movimiento.setParaderoSalida(etSalida.getText().toString());
            movimiento.setTiempoViaje(etTiempo.getText().toString());

            coleccion.document(movimiento.getId()).set(movimiento)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(getContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Error al actualizar: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDatePicker(EditText etFecha) {
        final Calendar calendario = Calendar.getInstance();

        DatePickerDialog dialogoFecha = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    // Formato yyyy-MM-dd
                    String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    etFecha.setText(fechaSeleccionada);
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );

        dialogoFecha.show();
    }

}