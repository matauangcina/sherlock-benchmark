package sherlock.test.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import sherlock.test.database.UserDbSchema.UserTable;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String id = getString(getColumnIndex(UserTable.Columns.ID));
        String fullName = getString(getColumnIndex(UserTable.Columns.FULL_NAME));
        String email = getString(getColumnIndex(UserTable.Columns.EMAIL));
        String phoneNum = getString(getColumnIndex(UserTable.Columns.PHONE_NUM));
        String username = getString(getColumnIndex(UserTable.Columns.USERNAME));
        String password = getString(getColumnIndex(UserTable.Columns.PASSWORD));

        User user = new User();
        user.setId(id);
        user.setName(fullName);
        user.setEmail(email);
        user.setPhoneNum(phoneNum);
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }
}
