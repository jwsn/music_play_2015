package linhai.example.com.databaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicDatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_COLLECT = "create table collect ("
    + "id integer primary key autoincrement, "
    + "name text, "
    + "pos integer, "
    + "path text)";

    public static final String CREATE_HISTORY = "create table history ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "pos integer, "
            + "path text)";

    private Context mContext;

    public MusicDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_COLLECT);
        db.execSQL(CREATE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
