package com.example.lab6_20206466;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LimaPassAdapter extends RecyclerView.Adapter<LimaPassAdapter.LimaPassViewHolder> {
    private List<LimaPass> lista;
    private OnMovimientoLongClickListener listener;

    public LimaPassAdapter(List<LimaPass> lista, OnMovimientoLongClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    //agregamos una interfaz de escucha para clicks largos

    public interface OnMovimientoLongClickListener {
        void onLongClick(LimaPass movimiento);
    }

    @NonNull
    @Override
    public LimaPassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lima_pass, parent, false);
        return new LimaPassViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull LimaPassViewHolder holder, int position) {
        LimaPass movimiento = lista.get(position);

        holder.tvTarjetaId.setText("ID Tarjeta: " + movimiento.getTarjetaId());
        holder.tvFecha.setText("Fecha: " + movimiento.getFecha());
        holder.tvEntrada.setText("Entrada: " + movimiento.getParaderoEntrada());
        holder.tvSalida.setText("Salida: " + movimiento.getParaderoSalida());
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

    public static class LimaPassViewHolder extends RecyclerView.ViewHolder {
        TextView tvTarjetaId, tvFecha, tvEntrada, tvSalida, tvTiempo;

        public LimaPassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTarjetaId = itemView.findViewById(R.id.tvTarjetaId);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEntrada = itemView.findViewById(R.id.tvEntrada);
            tvSalida = itemView.findViewById(R.id.tvSalida);
            tvTiempo = itemView.findViewById(R.id.tvTiempo);
        }
    }
}
