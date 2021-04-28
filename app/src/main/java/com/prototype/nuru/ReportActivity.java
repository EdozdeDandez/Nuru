package com.prototype.nuru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.prototype.nuru.models.Report;
import com.prototype.nuru.sql.DatabaseHelper;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private final AppCompatActivity activity = ReportActivity.this;

    int report_id;
    int user_id;

    private DatabaseHelper databaseHelper;
    private Report report;

    private TextView date;
    private TextView tags;
    private TextView post;
    private LinearLayout media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        databaseHelper = new DatabaseHelper(activity);

        report_id = getIntent().getIntExtra("report", 0);
        user_id = getIntent().getIntExtra("user", 0);

        report = databaseHelper.getReport(report_id);

        date = (TextView) findViewById(R.id.rep_date);
        post = (TextView) findViewById(R.id.rep_post);
        tags = (TextView) findViewById(R.id.rep_tags);
        media = (LinearLayout) findViewById(R.id.rep_media);

        date.setText(report.getDateCreated());
        post.setText(report.getPost());
        tags.setText(report.getTags());

        Uri posterUri;
        String mediaFiles[] = report.getMedia().split("; ");
        List<String> allMedia;
        allMedia = Arrays.asList(mediaFiles);
        for(String s: allMedia){
            if (isImage(s)) {
                File f = new File(s);
                posterUri = Uri.fromFile(f);
                LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(100,100);
                ImageView poster = new ImageView(this);
                poster.setImageURI(posterUri);
                poster.setLayoutParams(imParams);
                media.addView(poster);
            } else if (isVideo(s)) {
                File f = new File(s);
                posterUri = Uri.fromFile(f);
                LinearLayout.LayoutParams viParams = new LinearLayout.LayoutParams(100,100);
                VideoView video = new VideoView(this);
                video.setVideoURI(posterUri);
                video.setLayoutParams(viParams);
                media.addView(video);            }
        }
    }

    public void deleteReport(View view) {
        databaseHelper.deleteReport(report);
        Intent home = new Intent(activity, MainActivity.class);
        home.putExtra("user", user_id);
//        home.putExtra("ROLE", role);
        startActivity(home);
        finish();
    }

    public void editReport(View view) {
        Intent edit = new Intent(activity, CreateActivity.class);
        edit.putExtra("report", report_id);
        edit.putExtra("user", user_id);
        startActivity(edit);
        finish();
    }

    public static boolean isImage(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideo(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
}
