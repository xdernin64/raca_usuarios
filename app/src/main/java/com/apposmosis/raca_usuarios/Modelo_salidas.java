package com.apposmosis.raca_usuarios;

import android.widget.Button;

import com.google.firebase.Timestamp;

public class Modelo_salidas {
    Timestamp fecha;
    String codigo;
    String horadesalida;
    Double horasextra;
    String Apellidosynombres;
    String observacion;



    String docid;
    Button elimnar;

    public Modelo_salidas(){}

    public Modelo_salidas(Timestamp fecha, String codigo, String horadesalida, Double horasextra, String apellidosynombres, String observacion, String docid, Button elimnar) {
        this.fecha = fecha;
        this.codigo = codigo;
        this.horadesalida = horadesalida;
        this.horasextra = horasextra;
        Apellidosynombres = apellidosynombres;
        this.observacion = observacion;
        this.docid = docid;
        this.elimnar = elimnar;
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

    public String getApellidosynombres() {
        return Apellidosynombres;
    }

    public void setApellidosynombres(String apellidosynombres) {
        Apellidosynombres = apellidosynombres;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Button getElimnar() {
        return elimnar;
    }

    public void setElimnar(Button elimnar) {
        this.elimnar = elimnar;
    }
}
