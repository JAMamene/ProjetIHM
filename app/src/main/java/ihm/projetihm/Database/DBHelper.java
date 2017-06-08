package ihm.projetihm.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ihm.projetihm.Model.Category;
import ihm.projetihm.Model.News;
import ihm.projetihm.Model.Source;


public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "database.db";

    private static DBHelper instance;

    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private QueryBuilder qb;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.qb = new QueryBuilder();
    }

    public void openDataBase() throws SQLException, IOException {
        //Open the database
        String myPath = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database", e);
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database doesn't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor execQuery(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void execWriteQuery(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(query);
        } catch (SQLiteException e) {
            if (e instanceof SQLiteConstraintException) {
                Log.w("QUERYTWEET", "Duplicate key");
            } else {
                e.printStackTrace();
            }
        }
    }

    private List<Category> getCategories(long id) {
        Cursor c = this.getReadableDatabase().rawQuery(qb.getCategory(id), null);
        List<Category> list = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            list.add(Category.valueOf(c.getString(0)));
        }
        c.close();
        return list;
    }

    public long getAuthorId(String author) {
        ContentValues cv = new ContentValues();
        cv.put("name", author);
        long id;
        try {
            id = this.getWritableDatabase().insertOrThrow("author", null, cv);
        } catch (SQLiteException e) {
            Cursor c = execQuery(qb.getAuthor(author));
            c.moveToFirst();
            id = c.getLong(0);
            c.close();
            Log.w("QUERYAUTHOR", "Duplicate key or exception");
        }
        return id;
    }

    public News getNews(Cursor cursor) throws ParseException {
        News n = new News(cursor.getInt(0),
                cursor.getString(1),
                getCategories(cursor.getInt(0)),
                Source.valueOf(cursor.getString(11)),
                cursor.getString(13),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getLong(6),
                (!cursor.isNull(14) && cursor.getInt(14) != 0),
                cursor.getLong(7),
                cursor.getInt(8),
                (!cursor.isNull(9) && cursor.getInt(9) != 0)
        );
        Log.d("News", n.toString());
        return n;
    }
}