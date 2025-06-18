package com.example.lab6_20206466;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Linea1Adapter extends RecyclerView.Adapter<Linea1Adapter.Linea1ViewHolder> {
    private List<Linea1> lista;
    private OnMovimientoLongClickListener listener;

    public Linea1Adapter(List<Linea1> lista, OnMovimientoLongClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    //agregamos una interfaz de escucha para clicks largos

    public interface OnMovimientoLongClickListener {
        void onLongClick(Linea1 movimiento);
    }

    @NonNull
    @Override
    public Linea1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_linea1, parent, false);
        return new Linea1ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Linea1ViewHolder holder, int position) {
        Linea1 movimiento = lista.get(position);

        holder.tvTarjetaId.setText("ID Tarjeta: " + movimiento.getTarjetaId());
        holder.tvFecha.setText("Fecha: " + movimiento.getFecha());
        holder.tvEntrada.setText("Entrada: " + movimiento.getEstacionEntrada());
        holder.tvSalida.setText("Salida: " + movimiento.getEstacionSalida());
        holder.tvTiempo.setText("Tiempo: " + movimiento.getTiempoViaje());

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(movimiento);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class Linea1ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTarjetaId, tvFecha, tvEntrada, tvSalida, tvTiempo;

        public Linea1ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTarjetaId = itemView.findViewById(R.id.tvTarjetaId);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEntrada = itemView.findViewById(R.id.tvEntrada);
            tvSalida = itemView.findViewById(R.id.tvSalida);
            tvTiempo = itemView.findViewById(R.id.tvTiempo);
        }
    }
}
