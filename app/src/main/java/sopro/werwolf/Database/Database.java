package sopro.werwolf.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gina on 12.06.2016.
 */
public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "player.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PLAYERLOG = "playerlog";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_TEAM = "team";
    public static final String COLUMN_LOVER = "lover";
    public static final String COLUMN_IMAGE = COLUMN_ROLE;
    public static final String COLUMN_ALIVE = "alive";

    public static final String CREATE_TABLE =
            "create table " + TABLE_PLAYERLOG + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text not null, " +
                    COLUMN_ROLE + " text not null, " +
                    COLUMN_TEAM + " boolean, " +
                    COLUMN_LOVER + " text, " +
                    COLUMN_IMAGE + " text not null" +
                    COLUMN_ALIVE + " boolean);"
            ;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
