package com.example.practica4_doit;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private Context context;

    public RecyclerViewAdapter(Context context, List<String> data) {
        this.context = context;
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_card_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String item = mData.get(position);
        holder.textView.setText(item);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            boolean isSelected = false;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelected = isChecked;
                String message = isSelected ? "Tarea completada" : "Tarea no completada";
                Toast.makeText(holder.itemView.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Eliminar tarea");
                builder.setMessage("??Est??s seguro que quieres eliminar esta tarea?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mData.size());
                        Toast.makeText(holder.itemView.getContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos la descripci??n del item seleccionado
                String selectedItemDescription = mData.get(position);

                // dialogo para el Edittext
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Editar item");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(selectedItemDescription);
                builder.setView(input);

                // Configurar el OnClickListener para el bot??n Guardar
                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newDescription = input.getText().toString();

                        // Actualizar la descripci??n del item seleccionado en la lista de datos del RecyclerViewAdapter
                        mData.set(position, newDescription);

                        // Notificar al RecyclerViewAdapter que se ha actualizado un item
                        notifyItemChanged(position);

                        Toast.makeText(context, "Tarea modificada", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                // boton cancelar
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;

        CardView myCard;
        ImageButton btn_eliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.tv_CardDescripcion);
            btn_eliminar= itemView.findViewById(R.id.btn_eliminar);
            myCard = itemView.findViewById(R.id.myCard);
        }
    }
}