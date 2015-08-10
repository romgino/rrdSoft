package br.com.romgino.som.datamodel;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.romgino.som.modelo.Registro;

/**
 * Created by rom-pc on 30/06/2015.
 */

public class DataModel {
    private static final String DB_NAME = "db_registro";
    private static final String TABELA_REGISTRO =  "registro";
    private static final String ID =  "id";
    private static final String DATA =  "data";
    private static final String DECIDEL =  "decibel";
    private static final String TIPO_TEXTO =  "TEXT";
    private static final String TIPO_INTEIRO =  "INTEGER";
    private static final String TIPO_DATA =  "NUMERIC";
    private static final String TIPO_INTEIRO_PK =  "INTEGER PRIMARY KEY ";

    public static String criarTabelaRegistra(){
        String query ="CREATE TABLE " + TABELA_REGISTRO;
        query += "(";
        query += ID +" "+ TIPO_INTEIRO_PK +", ";
        query += DATA +" "+ TIPO_DATA +", ";
        query += DECIDEL +" "+ TIPO_DATA +" ";
        query += ")";

        return query;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static String getTabelaRegistro() {
        return TABELA_REGISTRO;
    }

    public static String getID() {
        return ID;
    }

    public static String getDATA() {
        return DATA;
    }

    public static String getDECIDEL() {
        return DECIDEL;
    }

}
