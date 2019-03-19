package com.aeropress.projekt.patryk.barcodescanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import com.google.zxing.oned.*;

public class DBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "BarcodeDB";
    public static int DATABASE_VERSION = 1;

    public static final String PRODUCT_TABLE_NAME = "Product";
    public static final String PRODUCT_COLUMN_ID= "id";
    public static final String PRODUCT_COLUMN_BARCODE = "barcode";
    public static final String PRODUCT_COLUMN_NAME = "name";
    public static final String PRODUCT_COLUMN_CAL = "cal";
    public static final String PRODUCT_COLUMN_PROTEIN = "protein";
    public static final String PRODUCT_COLUMN_CARBONS = "carbons";
    public static final String PRODUCT_COLUMN_FAT = "fat";
    public static final String PRODUCT_COLUMN_ORIGIN = "origin";

    public static final String SQLITE_CREATE_PRODUCT_TABLE =" CREATE TABLE "+PRODUCT_TABLE_NAME+"("
            +PRODUCT_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +PRODUCT_COLUMN_BARCODE+"TEXT NOT NULL UNIQUE,"
            +PRODUCT_COLUMN_NAME+"TEXT NOT NULL,"
            +PRODUCT_COLUMN_CAL+"INTEGER NOT NULL,"
            +PRODUCT_COLUMN_PROTEIN+"INTEGER NOT NULL,"
            +PRODUCT_COLUMN_CARBONS+"INTEGER NOT NULL,"
            +PRODUCT_COLUMN_FAT+"INTEGER NOT NULL,"
            +PRODUCT_COLUMN_ORIGIN+"TEXT NOT NULL"
            +");";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLITE_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS  "+PRODUCT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public long insertNewProduct(Product product)
    {
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_COLUMN_BARCODE,product.barcode);
        cv.put(PRODUCT_COLUMN_NAME,product.name);
        cv.put(PRODUCT_COLUMN_CAL,product.cal);
        cv.put(PRODUCT_COLUMN_CARBONS,product.carbon);
        cv.put(PRODUCT_COLUMN_FAT,product.fat);
        cv.put(PRODUCT_COLUMN_PROTEIN,product.protein);
        cv.put(PRODUCT_COLUMN_ORIGIN,product.origin);
        return getWritableDatabase().insert(PRODUCT_TABLE_NAME,null,cv);
    }
    public Product getProductFromDB(String barcode)
    {
        Cursor cur=getWritableDatabase().rawQuery("SELECT * FROM "+PRODUCT_TABLE_NAME+" WHERE "+PRODUCT_COLUMN_BARCODE+" = "+barcode ,null);
        if(cur.getCount()==0)
            return null;
        cur.moveToFirst();
        return new Product(cur.getString(1),cur.getString(2),cur.getFloat(3),cur.getFloat(5),cur.getFloat(6),cur.getFloat(4),cur.getString(7));
    }
    public boolean updateProductValues(Product product)
    {
        if(getProductFromDB(product.barcode)==null)
        {
            return false;
        }
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_COLUMN_NAME,product.name);
        cv.put(PRODUCT_COLUMN_CAL,product.cal);
        cv.put(PRODUCT_COLUMN_CARBONS,product.carbon);
        cv.put(PRODUCT_COLUMN_FAT,product.fat);
        cv.put(PRODUCT_COLUMN_PROTEIN,product.protein);
        getWritableDatabase().update(PRODUCT_TABLE_NAME,cv,PRODUCT_COLUMN_BARCODE+"='?'",new String[]{product.barcode});
        return true;
    }

}
