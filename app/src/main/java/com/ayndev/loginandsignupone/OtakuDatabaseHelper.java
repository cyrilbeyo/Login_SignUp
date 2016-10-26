package com.ayndev.loginandsignupone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class OtakuDatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase myDatabase;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Otaku.db";
    private static final String TABLE_NAME = "Member";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String TYPE_INTEGER = "INTEGER";
    private static final String TYPE_VARCHAR30 = "VARCHAR(30)";
    private static final String PRIMARY_KEY = "Primary Key";
    private static final String NOT_NULL = "not null";
    private static final String AUTO_INCREMENT = "AutoIncrement";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " " + TYPE_INTEGER + " " + PRIMARY_KEY + " " + AUTO_INCREMENT + ", " +
            COLUMN_NAME + " " + TYPE_VARCHAR30 + " " + NOT_NULL + ", " +
            COLUMN_EMAIL + " " + TYPE_VARCHAR30 + " " + NOT_NULL + ", " +
            COLUMN_PASSWORD + " " + TYPE_VARCHAR30 + " " + NOT_NULL + ");";

    private static final String SQL_SELECT_EMAIL_PASSWORD = "SELECT " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + " From " + TABLE_NAME + ";";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    OtakuDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    void insertOtakuMember(Otakunz otakunz) {
        myDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, otakunz.getNameUser());
        values.put(COLUMN_EMAIL, otakunz.getEmailUser());
        values.put(COLUMN_PASSWORD, otakunz.getPassUser());

        myDatabase.insert(TABLE_NAME, null, values);
    }

    String searchPasswordFromEmail(String emailUser) {
        String passwordUser = "";

        myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery(SQL_SELECT_EMAIL_PASSWORD, null);

        if (cursor.moveToFirst()) {
            String tempEmail, tempPassword;
            do {
                tempEmail = cursor.getString(0);
                tempPassword = cursor.getString(1);

                if (emailUser.equals(tempEmail)) {
                    passwordUser = tempPassword;
                    break;
                }

            } while (cursor.moveToNext());
        }

        return passwordUser;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        myDatabase = db;
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        this.onCreate(db);
    }
}
