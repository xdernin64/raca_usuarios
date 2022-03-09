package com.apposmosis.raca_usuarios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apposmosis.raca_usuarios.databinding.ActivityAsistenciasBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Asistencias extends AppCompatActivity {

    RecyclerView rcvsalidas;
    FirebaseFirestore db;
    ArrayList<Modelo> salidasArrayList;
    adaptador miadaptador;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    TextView totalsemana,txtnumerosemana,txtfechamin,txtfechamax;
    Button btnbuscarsemana;
    Button btnlogout;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //int grandTotalPrice = miadaptador.grandTotal(salidasArrayList);

        setContentView(R.layout.activity_asistencias);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Cargando salidas");
        progressDialog.show();
        mAuth=FirebaseAuth.getInstance();
        btnlogout=findViewById(R.id.btn_salir);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Asistencias.this,Login.class));
                finish();
            }
        });

        btnbuscarsemana=findViewById(R.id.btn_buscarsem);
        //txtnumerosemana=findViewById(R.id.txt_fechamax);
        txtfechamin=findViewById(R.id.txt_fechamin);
        txtfechamax=findViewById(R.id.txt_fechamax);





        rcvsalidas = findViewById(R.id.rec_salidas);
        rcvsalidas.setHasFixedSize(true);
        rcvsalidas.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        salidasArrayList= new ArrayList<Modelo>();
        miadaptador=new adaptador(Asistencias.this,salidasArrayList);
        rcvsalidas.setAdapter(miadaptador);

        //fecha actual
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        //semana actual
        /* PARA OBTENER DIA DE LA SEMANA SI FUNCIONA
        Calendar calendar = new GregorianCalendar();
        Calendar calendario = Calendar.getInstance();
        calendar.setFirstDayOfWeek( Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(2);
        calendar.setTime(hoy);
        int numberWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        */
        String fechats="01/01/2022";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechatedate1=null;
        try {
            fechatedate1 = formatter.parse(fechats);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("ddMMyyyy");
        //-----------------------------------------------------------------------------

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        Date fechatedate2=null;
        try {
            fechatedate2 = formatter2.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("ddMMyyyy");

        EventChangeListener(fechatedate1,fechatedate2);
        //convertir a formato dd/mm/yyyy

        txtfechamin.setText("01/01/2022");
        txtfechamax.setText(String.valueOf(dateString));
        btnbuscarsemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                salidasArrayList.clear();
                miadaptador.notifyDataSetChanged();
                //calculando fecha minima editext1
                String fechainit=txtfechamin.getText().toString();
                SimpleDateFormat formatterinit = new SimpleDateFormat("dd/MM/yyyy");
                Date finit=null;
                try {
                    finit = formatterinit.parse(fechainit);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                formatterinit = new SimpleDateFormat("ddMMyyyy");

                //calculando fecha maxima editext2
                String fechaend=txtfechamax.getText().toString();
                SimpleDateFormat formatterend = new SimpleDateFormat("dd/MM/yyyy");
                Date fend=null;
                try {
                    fend = formatterend.parse(fechaend);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                formatterend = new SimpleDateFormat("ddMMyyyy");
                //date to timestamp
                //Timestamp mints=new Timestamp(fechainicio);
                //Timestamp maxts=new Timestamp(fechafin);


                EventChangeListener(finit,fend);

            }
        });

    }


    public void EventChangeListener(Date diaminimo,Date diamaximo) {
        String id=mAuth.getCurrentUser().getUid();
        miadaptador.notifyDataSetChanged();
        // db.collection("salidas").whereEqualTo("uid",id).whereEqualTo("semana",semana).orderBy("fecha",Query.Direction.DESCENDING)
        db.collection("salidas").whereEqualTo("uid",id).whereGreaterThanOrEqualTo("fecha",diaminimo).whereLessThanOrEqualTo("fecha",diamaximo).orderBy("fecha", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null)
                {
                    if (progressDialog.isShowing())
                        Log.e("error firestore",error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType()==DocumentChange.Type.ADDED){
                        salidasArrayList.add(dc.getDocument().toObject(Modelo.class));
                        miadaptador.notifyDataSetChanged();

                    }

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }
                miadaptador.notifyDataSetChanged();

            }

        });
    }
}