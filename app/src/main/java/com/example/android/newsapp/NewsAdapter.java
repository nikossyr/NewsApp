package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.android.newsapp.QueryUtils.LOG_TAG;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param news    A List of News objects to display in a list
     */
    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }
        // Get the {@link News} object located at this position in the list
        News currentNewsArticle = getItem(position);

        // Find the TextView in the news_list_item.xml layout with the ID version_name
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_text_view);
        sectionTextView.setText(currentNewsArticle.getSection());

        // Find the TextView in the news_list_item.xml layout with the title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentNewsArticle.getTitle());

        if (currentNewsArticle.getAuthor() != null) {
            // Find the TextView in the news_list_item.xml layout with the author
            TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
            authorTextView.setText(currentNewsArticle.getAuthor());
        }

        String inputDateTime = currentNewsArticle.getDate();
        if (inputDateTime != null) {
            // Find the TextView in the news_list_item.xml layout with the date
            TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date = format.parse(inputDateTime);
                String formattedDate = formatDate(date);
                dateTextView.setText(formattedDate);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Date error: " + e);
            }
        }
        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        return dateFormat.format(dateObject);
    }
}
