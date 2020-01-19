package ru.job4j.tourist.db.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.job4j.tourist.db.dbhelper.NotRequiredForDB;
import ru.job4j.tourist.db.model.ModelLocation;

public class DBHelper extends SQLiteOpenHelper {
    private final static String TAG = DBHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private final HashMap<String, String> varTypeMapping;
    private static final String APP_NAME = "App";
    private static String DATABASE_EXTENSION = ".db";

    public DBHelper(Context context, String name) {
        super(context, name.toLowerCase() + "_" + APP_NAME + DATABASE_EXTENSION, null, DATABASE_VERSION);
        varTypeMapping = new HashMap<>();
        varTypeMapping.put("Integer", "INTEGER");
        varTypeMapping.put("Long", "LONG");
        varTypeMapping.put("String", "TEXT");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_POST = getCreateTableStatement(ModelLocation.class);

        db.execSQL(CREATE_TABLE_POST);

        String CREATE_INDEX_POST = getTableIndex(ModelLocation.class, "_id");

        db.execSQL(CREATE_INDEX_POST);
    }

    private String getCreateTableStatement(Class<?> clazz) {
        String tableName = clazz.getSimpleName();
        List<Field> allFields = Arrays.asList(clazz.getDeclaredFields());
        List<TableFieldItem> fieldName = new ArrayList<>();
        String idType = "";
        for (Field field : allFields) {
            if (field.isAnnotationPresent(NotRequiredForDB.class) || Modifier.isStatic(field.getModifiers()))
                continue;
            TableFieldItem item = new TableFieldItem();
            item.fieldName = field.getName();
            String varType = varTypeMapping.get(field.getType().getSimpleName());
            item.variableType = varType != null ? varType : "TEXT";
            fieldName.add(item);
            if (field.getName().equals("_id")) {
                idType = field.getType().getSimpleName();
            }
        }
        String query = "CREATE TABLE " + tableName + " (" + BaseColumns._ID + " ";
        query = query + idType + " PRIMARY KEY ";

        if (idType.equals("Integer")) {
            query = query + "AUTOINCREMENT NOT NULL";
        }
        query = query + " , ";
        if (allFields.size() == 1) {
            query = query.replace(" ,", "");
        }
        query = addFieldsInTable(query, fieldName);
        query = query + ", UNIQUE(title) ON CONFLICT REPLACE" + " ) ;";
        Log.d(TAG, "check create table statement " + query);
        return query;
    }

    private String addFieldsInTable(String query, List<TableFieldItem> fieldsName) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fieldsName.size(); i++) {
            TableFieldItem item = fieldsName.get(i);
            if (!item.fieldName.equals("_id")) {
                String fieldName = item.fieldName;
                stringBuilder.append(" ")
                        .append(fieldName)
                        .append(" ")
                        .append(item.variableType)
                        .append(i <= (fieldsName.size() - 2) ? " , " : " ");
            }
        }
        query = query + stringBuilder.toString();
        return query;
    }

    private String getTableIndex(Class<?> clazz, String indexField) {
        String tableName = clazz.getSimpleName();
        String query = "CREATE INDEX 'tag_" + tableName + "' ON '" + tableName + "' ( '" + indexField + "' )";
        Log.d(TAG, "check create index " + query);
        return query;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.enableWriteAheadLogging();
        db.execSQL("PRAGMA synchronous=NORMAL");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDB(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDB(db);
    }

    private void resetDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ModelLocation.class.getSimpleName());
        onCreate(db);
    }

    private static class TableFieldItem {
        String fieldName;
        String variableType;
    }
}
