package com.apposmosis.raca_usuarios;

import android.widget.Button;

import com.google.firebase.Timestamp;

public class Modelo {
    Timestamp fecha;
    String codigo;
    String horadesalida;
    Double horasextra;



    String docid;
    Button elimnar;

    public Modelo(){}

    public Modelo(Timestamp fecha, String codigo,String horadesalida,Double horasextra){
        this.fecha=fecha;
        this.codigo=codigo;
        this.horadesalida=horadesalida;
        this.horasextra=horasextra;
    }

    public Timestamp getFecha() {

        return fecha;
    }

    public void setFecha(Timestamp fecha) {

        this.fecha = fecha;
    }

    public String getCodigo() {

        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getHoradesalida() {
        return horadesalida;
    }

    public void setHoradesalida(String horadesalida) {
        this.horadesalida = horadesalida;
    }

    public Double getHorasextra() {
        return horasextra;
    }

    public void setHorasextra(Double horasextra) {
        this.horasextra = horasextra;

    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }



}
