package br.com.romgino.som.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.romgino.som.datamodel.DataModel;
import br.com.romgino.som.modelo.Registro;

/**
 * Created by rom-pc on 01/07/2015.
 */
public class DataSource extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public DataSource(Context context) {
        super(context, DataModel.getDbName(), null, 1);
        db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataModel.criarTabelaRegistra());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        onCreate(db);
    }
    public void persist(ContentValues values, String tabela){
        if (values.containsKey("id") && values.getAsInteger("id") != null
                && values.getAsInteger("id") != 0){
            Integer id = values.getAsInteger("id");
            db.update(tabela,values,"id = " + id, null);
        } else {
            db.insert(tabela,null,values);
        }
    }
    public List<Registro> listatudo(){
        List<Registro> registros = new ArrayList<Registro>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataModel.getDbName(), null);
        DateFormat dataf;
       /* if (cursor.moveToFirst()){
            do {
                registros.add(new Registro(
                                Integer.parseInt(cursor.getString(0)),
                                SimpleDateFormat("dd-mm-yyy").format(cursor.getString(1)),
                                cursor.getString(2)
                    )
                );
            }while (cursor.moveToNext());
        }
*/
        return registros;

    }
}
/*
* context - Usado para abrir ou criar o banco de dados
*    name - É o nome do banco de dados no sistema de arquivos,
*           caso seja null será criado um banco de dados apenas na memória.
* factory - Usado para criar um cursor de objetos.
*           Se este valor for null será criado um objeto default.
* version - Este é o númedo da versão do banco de dados, iniciado por 1.
*           Caso este número seja alterado para 2,
*           significa que a estrutura do banco de dados foi alterada e
*           o método onUpgrade() será executado.
*/