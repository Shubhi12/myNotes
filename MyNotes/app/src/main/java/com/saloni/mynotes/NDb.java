package com.saloni.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SALONI on 04-07-2017.
 */

public class NDb extends SQLiteOpenHelper {

    public static final String dbname = "MyNotes.db";
    public static final String _id = "_id";
    public static final String name = "name";
    public static final String remark = "remark";
    public static final String dates = "dates";
    public static final String mynotes = "mynotes";
    private HashMap hp;
    SQLiteDatabase db;

    public NDb(Context context) {
        super(context, dbname, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mynotes"
                + "(_id integer primary key, name text,remark text,dates text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + mynotes);
        onCreate(db);
    }
    public Cursor fetchAll() {
        db = this.getReadableDatabase();
        Cursor mCursor = db.query(mynotes, new String[] { "_id", "name",
                "dates", "remark" }, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public boolean insertNotes(String title, String date, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", title);
        contentValues.put("dates", date);
        contentValues.put("remark", content);
        db.insert(mynotes, null, contentValues);
        return true;
    }
    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, mynotes);
        return numRows;
    }
    public boolean updateNotes(Integer id, String name, String dates,
                               String remark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("dates", dates);
        contentValues.put("remark", remark);
        db.update(mynotes, contentValues, "_id = ? ",
                new String[] { Integer.toString(id) });
        return true;
    }
    public Integer deleteNotes(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(mynotes, "_id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<Entity> getAll() {
        ArrayList<Entity> array_list = new ArrayList<Entity>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + mynotes, null);
        //res.moveToFirst();
        if (res != null) {
            while (res.moveToNext()) {
                int id = res.getInt(res.getColumnIndex(_id));
                String r = res.getString(res.getColumnIndex(remark));
                String d = res.getString(res.getColumnIndex(dates));
                String _n = res.getString(res.getColumnIndex(name));
                Entity e = new Entity();
                e.setID(id);
                e.setTitle(_n);
                e.setRemark(r);
                e.setDate(d);
                array_list.add(e);
            }
        }
        return array_list;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor z = db.rawQuery("select * from " + mynotes + " where _id=" + id
                + "", null);
        return z;
    }
}
