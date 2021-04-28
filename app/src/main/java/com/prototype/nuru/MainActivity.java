package com.prototype.nuru;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.prototype.nuru.adapters.ReportListAdapter;
import com.prototype.nuru.models.Report;
import com.prototype.nuru.sql.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final AppCompatActivity activity = MainActivity.this;

    private DatabaseHelper databaseHelper;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        //        set adapter to listview
        final ArrayList<Report> reports = databaseHelper.getAllReports();
        ReportListAdapter adapter = new ReportListAdapter(this, reports);
        ListView listView = (ListView) findViewById(R.id.report_list);
        listView.setAdapter(adapter);

        //        set click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Report report = reports.get(position);
                Intent intent = new Intent(MainActivity.this, ReportListAdapter.class);
                intent.putExtra("report", report.getId());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        databaseHelper = new DatabaseHelper(activity);
        user_id = getIntent().getIntExtra("user", 0);

        if (user_id == 0) {
            Intent login = new Intent(activity, LoginActivity.class);
            startActivity(login);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
