package com.prototype.nuru.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prototype.nuru.R;
import com.prototype.nuru.models.Report;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportListAdapter extends ArrayAdapter<Report> {
    final static String TAG = ReportListAdapter.class.getName();
    public ReportListAdapter(Context context, ArrayList<Report> reports) {
        super(context, 0, reports);
    }
    private final String[] okFileExtensions = new String[] {
            "jpg",
            "png",
            "gif",
            "jpeg"
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Report report = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_list, parent, false);
        }
        // Lookup view for data population
        TextView date = convertView.findViewById(R.id.report_date);
        TextView post = convertView.findViewById(R.id.report_post);
        TextView tags = convertView.findViewById(R.id.report_tags);
        ImageView poster = convertView.findViewById(R.id.report_image);

        Uri posterUri;
        String media[] = report.getMedia().split("; ");
        List<String> allMedia;
        allMedia = Arrays.asList(media);
        for(String s: allMedia){
            File f = new File(s);
            if (isImage(f)) {
                posterUri = Uri.fromFile(f);
                poster.setImageURI(posterUri);
                break;
            }
        }

        date.setText(report.getDateCreated());
        post.setText(report.getPost().substring(0,50));
        tags.setText(report.getTags());
        // Return the completed view to render on screen
        return convertView;
    }

    public boolean isImage(File file) {
        for (String extension: okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
