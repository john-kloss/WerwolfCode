package sopro.werwolf.Database;

/**
 * Created by Gina on 12.06.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabasePlayer {

    public SQLiteDatabase sqlBase;
    public Database dbHelper;
    public String[] allCollumns = {"id", "name", "role", "team", "lover", "image", "alive"};

    public DatabasePlayer(Context context){
        dbHelper = new Database(context);
    }

    public void open() throws SQLException {
        sqlBase = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public DatabaseEntry createEnrty(int id, String name, String role, boolean team, String lover, String image, boolean alive){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("role", role);
        values.put("team", team);
        values.put("lover", lover);
        values.put("image", image);
        values.put("alive", alive);


        long insertId = sqlBase.insert("playerlog", null, values);

        Cursor cursor = sqlBase.query("playerlog", allCollumns, "id = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        return cursorToEntry(cursor);
    }

    protected List<DatabaseEntry> getAllEntries(){
        List<DatabaseEntry> EntriesList = new ArrayList<DatabaseEntry>();
        EntriesList = new ArrayList<DatabaseEntry>();

        Cursor cursor = sqlBase.query("playerlog", allCollumns, null, null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0){
            return EntriesList;
        }

        while(cursor.isAfterLast() == false){
            DatabaseEntry entry = cursorToEntry(cursor);
            EntriesList.add(entry);
            cursor.moveToNext();
        }

        cursor.close();

        return EntriesList;
    }

    private DatabaseEntry cursorToEntry(Cursor cursor){
        DatabaseEntry entry = new DatabaseEntry();
        entry.setId(cursor.getInt(0));
        entry.setName(cursor.getString(1));
        entry.setRole(cursor.getString(2));
        entry.setTeam(cursor.getInt(3)>0);
        entry.setLover(cursor.getString(4));
        entry.setImage(cursor.getString(5));
        entry.setAlive(cursor.getInt(6)>0);

        return entry;
    }
}
