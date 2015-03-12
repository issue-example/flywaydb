package sandbox.android;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.android.ContextHolder;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    public static final String DB_NAME = "sandbox.android.db";

    @InjectView(R.id.tables_tv)
    TextView tablesTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, 0, null);
        ContextHolder.setContext(this);
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:sqlite:" + db.getPath(), "", "");
        flyway.setLocations("classpath:db/migration");
        flyway.migrate();


        String tables = listAllTablesInDb(db);
        tablesTv.setText(tables);
    }

    private String listAllTablesInDb(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        String tables = "";

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                tables = tables +"\n- "  + c.getString(0);
                c.moveToNext();
            }
        }
        return tables;
    }

}
