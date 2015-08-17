package br.com.romgino.som.modelo;

import java.util.Date;

/**
 * Created by rom-pc on 30/06/2015.
 */
public class Registro  {

    private int id;
    private Date data;
    private float decibel;

    public Registro(int id, Date data, float decibel) {
        this.id = id;
        this.data = data;
        this.decibel = decibel;
    }

    public Registro() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Date getData() {
        return data;
    }

    

    public void setData(Date data) {
        this.data = data;
    }

    public float getDecibel() {
        return decibel;
    }

    public void setDecibel(float decibel) {
        this.decibel = decibel;
    }

}
