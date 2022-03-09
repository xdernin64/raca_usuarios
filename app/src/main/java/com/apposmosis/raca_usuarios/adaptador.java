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

public class adaptador extends RecyclerView.Adapter<adaptador.MyViewHolder> {
    Context context;
    ArrayList<Modelo> salidasArrayList;
    FirebaseFirestore firestore;
    Asistencias actsalidas;
    Double totalhorasextras;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public  adaptador(Context context,ArrayList<Modelo> salidasArrayList){
        this.context=context;
        this.salidasArrayList=salidasArrayList;
        firestore=FirebaseFirestore.getInstance();
        this.actsalidas=actsalidas;


    }

    public void deleteData(int position){
        Modelo item = salidasArrayList.get(position);
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
    public adaptador.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.salida , parent , false);
        return new MyViewHolder(v);
    }

    private void notifyRemoved(int position){

        Intent intebtsalidas=new Intent(context,Asistencias.class);

        context.startActivity(intebtsalidas);
        actsalidas.finish();
    }



    @Override
    public void onBindViewHolder(@NonNull adaptador.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Modelo modelo= salidasArrayList.get(position);
        Integer segundos= Integer.parseInt(String.valueOf(modelo.fecha.getSeconds()));
        Date date = new Date(segundos*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        holder.tvfecha.setText(String.valueOf(formattedDate));
        holder.tvcodigo.setText(modelo.codigo);
        holder.tvhsalida.setText(modelo.horadesalida);
        holder.tvhextras.setText(String.valueOf(modelo.horasextra));

        Double price = Double.parseDouble(String.valueOf(salidasArrayList.get(position).getHorasextra()));
        int count = getItemCount();


        for (int i = 0; i < count; i++){
            Double tsum=0.00;
            tsum = tsum + price;
            Log.d("total pay : ", String.valueOf(tsum));
        }
    }


    @Override
    public int getItemCount() {
        return salidasArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvfecha,tvcodigo,tvhsalida,tvhextras,txthorasemanales;
        Button btneliminar;
        String uid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvfecha=itemView.findViewById(R.id.tvshow_fecha);
            tvcodigo=itemView.findViewById(R.id.tvshow_codigo);
            tvhsalida=itemView.findViewById(R.id.tvshow_salida);
            tvhextras=itemView.findViewById(R.id.tvshow_horasextras);
            txthorasemanales=itemView.findViewById(R.id.txt_fechamax);
        }
    }
}
