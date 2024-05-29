package sherlock.insecure.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import sherlock.insecure.database.UserDbSchema.UserTable;

import java.util.ArrayList;
import java.util.List;

public class UserLab {
    private static UserLab sUserLab;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    private UserLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new UserBaseHelper(mContext).getWritableDatabase();
    }

    public void addUser(User user) {
        ContentValues values = getContentValues(user);
        mDatabase.insert(UserTable.NAME, null, values);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        UserCursorWrapper cursor = queryUsers(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                users.add(cursor.getUser());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return users;
    }

    public String getUsersData() {
        List<User> users = getUsers();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            sb.append(users.get(i).getName()).append(";")
                    .append(users.get(i).getUsername()).append(";")
                    .append(users.get(i).getPhoneNum()).append(";")
                    .append(users.get(i).getEmail()).append(";")
                    .append(users.get(i).getPassword()).append(";");
        }
        return sb.toString();
    }

    public User getUser(String id) {
        UserCursorWrapper cursor = queryUsers(
                UserTable.Columns.ID + " = ?",
                new String[] {id}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Columns.ID, user.getId());
        values.put(UserTable.Columns.FULL_NAME, user.getName());
        values.put(UserTable.Columns.EMAIL, user.getEmail());
        values.put(UserTable.Columns.PHONE_NUM, user.getPhoneNum());
        values.put(UserTable.Columns.USERNAME, user.getUsername());
        values.put(UserTable.Columns.PASSWORD, user.getPassword());
        return values;
    }

    private UserCursorWrapper queryUsers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                UserTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new UserCursorWrapper(cursor);
    }

    public static UserLab getInstance(Context context) {
        if (sUserLab == null) {
            sUserLab = new UserLab(context);
        }
        return sUserLab;
    }
}
