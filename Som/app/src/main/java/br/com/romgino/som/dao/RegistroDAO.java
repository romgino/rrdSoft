package br.com.romgino.som.dao;

import android.content.ContentValues;
import android.content.Context;

import br.com.romgino.som.datamodel.DataModel;
import br.com.romgino.som.datasource.DataSource;
import br.com.romgino.som.modelo.Registro;

/**
 * Created by rom-pc on 30/06/2015.
 */
public class RegistroDAO {
    DataSource ds;
    ContentValues values;


    public RegistroDAO(Context context) {
        ds = new DataSource(context);
    }

    public boolean adicionar(Registro obj){
        boolean retorno = false;

        values = new ContentValues();

        values.put(DataModel.getDATA(),String.valueOf(obj.getData()));
        values.put(DataModel.getDECIDEL(),obj.getDecibel());
        try {
            ds.persist(values,DataModel.getTabelaRegistro());
            retorno = true;
        } catch (Exception e){

        }

        return retorno;
    }

}
