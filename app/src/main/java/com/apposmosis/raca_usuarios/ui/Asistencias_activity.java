package com.apposmosis.raca_usuarios.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apposmosis.raca_usuarios.Clases_adicionales.InputFilterMinMax;
import com.apposmosis.raca_usuarios.Login;
import com.apposmosis.raca_usuarios.Modelo_salidas;
import com.apposmosis.raca_usuarios.R;
import com.apposmosis.raca_usuarios.adaptador_salida;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Asistencias_activity extends AppCompatActivity {

    RecyclerView rcvsalidas;
    FirebaseFirestore db;
    ArrayList<Modelo_salidas> salidasArrayList;
    adaptador_salida miadaptador;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    TextView totalsemana,txtnumerosemana,txtfechamin,txtfechamax;
    Button btnbuscarsemana;
    Button btnlogout;
    private Switch noche;
    private TextView tvfecha,tvhorasalida,tvminutosalida;
    private ImageButton btnguardarhora;
    TextView tvdatos,tvcodigo;
    EditText txtobservacion;

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
        //guardando salida id
        btnguardarhora=findViewById(R.id.btn_agregarHora);
        tvfecha=findViewById(R.id.txt_fecha);
        tvhorasalida=findViewById(R.id.txt_horasalida);
        tvminutosalida=findViewById(R.id.txt_minutosalida);
        tvdatos=findViewById(R.id.tv_datos);
        tvcodigo=findViewById(R.id.tv_codigo);
        noche=findViewById(R.id.switch_noche);
        btnlogout=findViewById(R.id.btn_salir);
        txtobservacion=findViewById(R.id.txt_nota);
        //estableciendo los editext de hora minuto
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(2);
        tvhorasalida.setFilters(filterArray);
        tvminutosalida.setFilters(filterArray);
        tvhorasalida.setFilters(new InputFilter[]{ new InputFilterMinMax("1","24") });
        tvminutosalida.setFilters(new InputFilter[]{ new InputFilterMinMax("0","59") });
        //estableciendo vecha en edittext dd/mm/yyyy
        long datehoy = System.currentTimeMillis();
        SimpleDateFormat sdft = new SimpleDateFormat("ddMMyyyy");
        String dateStringhoy = sdft.format(datehoy);

        //obeteniendo numero de la semanna
        Calendar calendar = new GregorianCalendar();


        //estableciendo el spinner noche
        noche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noche.isChecked()){
                    tvhorasalida.setText("1");
                    tvminutosalida.setText("00");
                    tvhorasalida.setVisibility(view.INVISIBLE);
                    tvminutosalida.setVisibility(view.INVISIBLE);
                }
                else{
                    tvhorasalida.setText("");
                    tvminutosalida.setText("");
                    tvhorasalida.setVisibility(view.VISIBLE);
                    tvminutosalida.setVisibility(view.VISIBLE);
                }
            }
        });

        btnguardarhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(Asistencias_activity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Guardando salida");
                progressDialog.show();


                String strhsalida=tvhorasalida.getText().toString();
                String strmsalida=tvminutosalida.getText().toString();
                String fechats=tvfecha.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date fechatedate=null;
                try {
                    fechatedate = formatter.parse(fechats);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                formatter = new SimpleDateFormat("ddMMyyyy");


                if (strhsalida.isEmpty() || strmsalida.isEmpty()){
                    Toast.makeText(Asistencias_activity.this, "Ingrese datos en horas y minutos", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    if (fechats.length()==10){
                        Double horas=Double.parseDouble(strhsalida);
                        Double minutos=Double.parseDouble(strmsalida);
                        String codigotxt=tvcodigo.getText().toString();
                        String datostxt=tvdatos.getText().toString();
                        String observacion=txtobservacion.getText().toString();
                        String horasalida= tvhorasalida.getText().toString()+":"+tvminutosalida.getText().toString();
                        DecimalFormat df = new DecimalFormat("###.##");
                        Double input=((horas+(minutos/60))-14.75);
                        //Date fechatedate= new Date(fechats,"dd/mm/yy");
                        BigDecimal bd = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
                        double tiempoextra = bd.doubleValue();

                        if (tiempoextra>=1){
                            tiempoextra=tiempoextra+0;
                        }
                        else {
                            tiempoextra=0.0;
                        }

                        if (noche.isChecked()){
                            horasalida="Turno de noche";
                            tiempoextra=3.25;


                        }
                        else{
                            horasalida=horasalida;
                            tiempoextra=tiempoextra;

                        }

                        Calendar calendario = Calendar.getInstance();
                        calendar.setFirstDayOfWeek( Calendar.MONDAY);
                        calendar.setMinimalDaysInFirstWeek(2);
                        calendar.setTime(fechatedate);
                        int numberWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

                        Map<String, Object> map= new HashMap<>();

                        String id=mAuth.getCurrentUser().getUid();

                        String docid= dateStringhoy+codigotxt;


                        map.put("codigo",codigotxt);
                        map.put("apellidosynombres",datostxt);
                        map.put("fecha",fechatedate);
                        map.put("horadesalida",horasalida);
                        map.put("horasextra",tiempoextra);
                        map.put("uid",id);
                        map.put("docid",docid);
                        map.put("observacion",observacion);




                        db.collection("salidas").document(docid).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if (task2.isSuccessful()){

                                    Toast.makeText(Asistencias_activity.this, "Se guardo la hora de salida", Toast.LENGTH_SHORT).show();
                                    btnbuscarsemana.callOnClick();
                                    miadaptador.notifyDataSetChanged();
                                }
                                else
                                {

                                    Toast.makeText(Asistencias_activity.this,"No se pudo registrar la hora de salida", Toast.LENGTH_SHORT).show();

                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                    else {
                        Toast.makeText(Asistencias_activity.this, "Ingrese formato de fecha dd/mm/aaaa", Toast.LENGTH_SHORT).show();
                        tvfecha.setError("Ingrese fecha válida");
                        progressDialog.dismiss();


                    }


                }

            }
        });



        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Asistencias_activity.this, Login.class));
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
        salidasArrayList= new ArrayList<Modelo_salidas>();
        miadaptador=new adaptador_salida(Asistencias_activity.this,salidasArrayList);
        rcvsalidas.setAdapter(miadaptador);

        //fecha actual
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        String fechats="01/01/2022";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechatedate1=null;
        tvfecha.setText(dateString);

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
        informaciondeusuario();


    }
    private void informaciondeusuario() {
        String id=mAuth.getCurrentUser().getUid();
        db.collection("usuarios").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    String codigo=documentSnapshot.getString("codigo");
                    String nombrecompleto=documentSnapshot.getString("apellidosynombres");
                    String uid=documentSnapshot.getString("codigo");
                    tvcodigo.setText(codigo);
                    tvdatos.setText(nombrecompleto);

                }
                else{
                    Toast.makeText(Asistencias_activity.this, "El documento no existe", Toast.LENGTH_SHORT).show();

                }

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
                        salidasArrayList.add(dc.getDocument().toObject(Modelo_salidas.class));
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