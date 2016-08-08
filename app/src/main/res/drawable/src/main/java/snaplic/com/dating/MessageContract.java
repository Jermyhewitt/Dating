package snaplic.com.dating;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Jermaine Hewitt on 1/8/2016.
 */
public class MessageContract
{
    public MessageContract()
    {}

    public static abstract class MessageEntry implements BaseColumns
    {
        public static final String TABLE_NAME="messageTable";
        public static final String COLUMN_NAME_Sender="Sender";
        public static final String COLUMN_NAME_Time="Temporal";
        public static final String COLUMN_NAME_Receiver="Receiver";
        public static final String COLUMN_NAME_MessageContent="MessageContent";
        public static final String COLUMN_NAME_NULLABLE="Sender";


    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MessageEntry.TABLE_NAME + " (" +
                    MessageEntry._ID + " INTEGER PRIMARY KEY," +
                    MessageEntry.COLUMN_NAME_MessageContent+TEXT_TYPE+COMMA_SEP+
                    MessageEntry.COLUMN_NAME_Receiver+TEXT_TYPE + COMMA_SEP +
                    MessageEntry.COLUMN_NAME_Sender+TEXT_TYPE + COMMA_SEP +
                    MessageEntry.COLUMN_NAME_Time+ TEXT_TYPE+
                    " )";


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME;
}
