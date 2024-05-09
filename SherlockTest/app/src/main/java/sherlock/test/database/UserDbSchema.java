package sherlock.test.database;

public class UserDbSchema {
    public static final class UserTable {
        public static final String NAME = "users";
        public static final class Columns {
            public static final String ID = "id";
            public static final String FULL_NAME = "name";
            public static final String EMAIL = "email";
            public static final String PHONE_NUM = "phone_num";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
        }
    }
}
