package egingenierie.radioespace.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHelper {

    private static final String TAG = "AnyDBAdapter";
    private DatabaseHelperInternal mDbHelper;
    private static SQLiteDatabase mDb;
    private static String DB_PATH = Environment.getDataDirectory() + "/data/" + "egingenierie.radioespace" + "/databases/";
    private static final String DATABASE_NAME = "ods.db";
    private static final int DATABASE_VERSION = 1;
    private final Context adapterContext;

    public DatabaseHelper(Context context) {
        this.adapterContext = context;
    }

    public DatabaseHelper open() throws SQLException {
        mDbHelper = new DatabaseHelperInternal(adapterContext);
        try {
            mDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        try {
            mDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        return this;
    }

    public Cursor executeSelect(String query) {
        return mDb.rawQuery(query, null);
    }

    public int deleteFromTable(int id) {
        //return mDb.delete(query, null, null);

        return mDb.delete("favorite", "id" + " = ?", new String[]{"" + id});
    }

    public int deleteFromTableArticle(int id) {
        //return mDb.delete(query, null, null);

        return mDb.delete("favouritearticle", "id" + " = ?", new String[]{"" + id});
    }

    public void executeInsert(String query, String[] str) {
        mDb.execSQL(query, str);
    }

    public void close() {
        mDbHelper.close();
    }

    private static class DatabaseHelperInternal extends SQLiteOpenHelper {

        Context helperContext;

        DatabaseHelperInternal(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database!!!!!");
            onCreate(db);
        }

        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();
            if (dbExist) {
            } else {

                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public SQLiteDatabase getDatabase() {
            String myPath = DB_PATH + DATABASE_NAME;
            return SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        }

        private boolean checkDataBase() {
            File dbFile = new File(DB_PATH + DATABASE_NAME);
            return dbFile.exists();
        }

        private void copyDataBase() throws IOException {
            Date currentDate = new Date(System.currentTimeMillis());
            DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss a");
            System.out.println("start copy database at = " + df.format(currentDate));
            InputStream myInput = helperContext.getAssets().open("database/" + DATABASE_NAME);
            String outFileName = DB_PATH + DATABASE_NAME;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
            currentDate = new Date(System.currentTimeMillis());
            System.out.println("end copy database at = " + df.format(currentDate));
        }

        public void openDataBase() throws SQLException {
            String myPath = DB_PATH + DATABASE_NAME;
            mDb = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }

        @Override
        public synchronized void close() {

            if (mDb != null)
                mDb.close();

            super.close();

        }
    }


}