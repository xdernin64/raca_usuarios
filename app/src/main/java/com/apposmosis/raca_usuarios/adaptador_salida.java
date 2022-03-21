package com.apposmosis.raca_usuarios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apposmosis.raca_usuarios.ui.Asistencias_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class adaptador_salida extends RecyclerView.Adapter<adaptador_salida.MyViewHolder> {
    Context context;
    ArrayList<Modelo_salidas> salidasArrayList;
    FirebaseFirestore firestore;
    Asistencias_activity actsalidas;
    Double totalhorasextras;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public adaptador_salida(Context context, ArrayList<Modelo_salidas> salidasArrayList){
        this.context=context;
        this.salidasArrayList=salidasArrayList;
        firestore=FirebaseFirestore.getInstance();
        this.actsalidas=actsalidas;


    }

    public void deleteData(int position){
        Modelo_salidas item = salidasArrayList.get(position);
        db.collection("salidas").document(item.getDocid()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            salidasArrayList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Salida eliminada" , Toast.LENGTH_SHORT).show();


                        }else{
                            Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @NonNull
    @Override
    public adaptador_salida.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.salida , parent , false);
        return new MyViewHolder(v);
    }

    private void notifyRemoved(int position){

        Intent intebtsalidas=new Intent(context, Asistencias_activity.class);

        context.startActivity(intebtsalidas);
        actsalidas.finish();
    }





    @Override
    public void onBindViewHolder(@NonNull adaptador_salida.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Modelo_salidas modeloSalidas = salidasArrayList.get(position);
        Integer segundos= Integer.parseInt(String.valueOf(modeloSalidas.fecha.getSeconds()));
        Date date = new Date(segundos*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        holder.tvfecha.setText(String.valueOf(formattedDate));
        holder.tvcodigo.setText(modeloSalidas.codigo);
        holder.tvhsalida.setText(modeloSalidas.horadesalida);
        holder.tvhextras.setText(String.valueOf(modeloSalidas.horasextra));
        holder.tvobservacion.setText(modeloSalidas.observacion);

        Double price = Double.parseDouble(String.valueOf(salidasArrayList.get(position).getHorasextra()));
        int count = getItemCount();


        for (int i = 0; i < count; i++){
            Double tsum=0.00;
            tsum = tsum + price;
            Log.d("total pay : ", String.valueOf(tsum));
        }
        holder.btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("salidas").document(modeloSalidas.docid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Salida eliminada", Toast.LENGTH_SHORT).show();
                        salidasArrayList.remove(position);
                        notifyItemRemoved(position);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return salidasArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvfecha,tvcodigo,tvhsalida,tvhextras,txthorasemanales,tvobservacion,tvdatos;
        Button btneliminar;
        String uid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvfecha=itemView.findViewById(R.id.tvshow_fecha);
            tvcodigo=itemView.findViewById(R.id.tvshow_codigo);
            tvhsalida=itemView.findViewById(R.id.tvshow_salida);
            tvhextras=itemView.findViewById(R.id.tvshow_horasextras);
            txthorasemanales=itemView.findViewById(R.id.txt_fechamax);
            btneliminar=itemView.findViewById(R.id.btn_eliminar);
            tvobservacion=itemView.findViewById(R.id.tv_observaciones);
            tvdatos=itemView.findViewById(R.id.tv_datos);

        }
    }
}
