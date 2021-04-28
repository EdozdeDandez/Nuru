package com.prototype.nuru;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
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

public class CreateActivity extends AppCompatActivity {
    private final AppCompatActivity activity = CreateActivity.this;

    int report_id;
    int user_id;
    String imagePath;

    private int REQUEST_TAKE_GALLERY_VIDEO = 10;


    private DatabaseHelper databaseHelper;
    private Report report;

    private EditText add_tag;
    private EditText post;
    private LinearLayout tags;
    private LinearLayout media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initViews();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        databaseHelper = new DatabaseHelper(activity);

        report_id = getIntent().getIntExtra("report", 0);
        user_id = getIntent().getIntExtra("user", 0);


        add_tag = (EditText) findViewById(R.id.tag);
        post = (EditText) findViewById(R.id.post);
        tags = (LinearLayout) findViewById(R.id.tags);
        media = (LinearLayout) findViewById(R.id.media);

        if (report_id == 0) {
            report = new Report();
        } else {
            report = databaseHelper.getReport(report_id);

            post.setText(report.getPost());

            String allTags[] = report.getTags().split("; ");
            List<String> allSavedTags;
            allSavedTags = Arrays.asList(allTags);
            for(String s: allSavedTags){
                TextView tag = new TextView(this);
                tag.setText(add_tag.getText().toString());
                tags.addView(tag);
            }

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
    }

    public void addTag(View view) {
        TextView tag = new TextView(this);
        tag.setText(add_tag.getText().toString());
        tags.addView(tag);
        report.setTags(report.getTags() + add_tag.getText().toString() + "; ");
        add_tag.setText(null);
    }

    public void saveReport(View view) {
        report.setPost(post.getText().toString());
        if (report_id == 0) {
            databaseHelper.addReport(report);
        } else {
            databaseHelper.updateReport(report);
        }
        Intent home = new Intent(activity, MainActivity.class);
        home.putExtra("user", user_id);
        startActivity(home);
        finish();
    }

    public void chooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void selectImage(View view) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        Uri selectedImageUri = data.getData();
                        imagePath = getPathFromURI(selectedImageUri);
                        if (imagePath != null) {
                            File f = new File(imagePath);
                            selectedImageUri = Uri.fromFile(f);
                        }
                        LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(100, 100);
                        ImageView poster = new ImageView(this);
                        poster.setImageBitmap(selectedImage);
                        poster.setLayoutParams(imParams);
                        media.addView(poster);
                        report.setMedia(report.getMedia() + imagePath + "; ");
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImageUri = data.getData();
                        imagePath = getPathFromURI(selectedImageUri);
                        if (imagePath != null) {
                            File f = new File(imagePath);
                            selectedImageUri = Uri.fromFile(f);
                        }
                        LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(100, 100);
                        ImageView poster = new ImageView(this);
                        poster.setImageURI(selectedImageUri);
                        poster.setLayoutParams(imParams);
                        media.addView(poster);
                        report.setMedia(report.getMedia() + imagePath + "; ");
                    }
                    break;
                case 10:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedVideoUri = data.getData();
                        imagePath = getPathFromURI(selectedVideoUri);
                        if (imagePath != null) {
                            File f = new File(imagePath);
                            selectedVideoUri = Uri.fromFile(f);
                        }
                        LinearLayout.LayoutParams viParams = new LinearLayout.LayoutParams(100, 100);
                        VideoView video = new VideoView(this);
                        video.setVideoURI(selectedVideoUri);
                        video.setLayoutParams(viParams);
                        media.addView(video);
                        report.setMedia(report.getMedia() + imagePath + "; ");
                    }
                    break;
            }
        }
    }

    //    get image path
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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
