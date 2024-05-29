package sherlock.insecure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sherlock.insecure.database.UserDbSchema.UserTable;

public class UserBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "userBase.db";

    public UserBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                UserTable.Columns.ID + ", " +
                UserTable.Columns.FULL_NAME + ", " +
                UserTable.Columns.EMAIL + ", " +
                UserTable.Columns.PHONE_NUM + ", " +
                UserTable.Columns.USERNAME + ", " +
                UserTable.Columns.PASSWORD +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
