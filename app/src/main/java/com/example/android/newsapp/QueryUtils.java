package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final String KEY_TITLE = "webTitle";
    private static final String SECTION_NAME = "sectionName";
    private static final String URL_NAME = "webUrl";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the USGS dataset and return a {@link List<News>} object to represent a List of news.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.e(LOG_TAG, "Link: " + url);
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link List<News>} object by parsing out information
     * about the first news story from the input newsJSON string.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject newsJSONObject = new JSONObject(newsJSON);
            JSONObject newsJSONObjectResults = newsJSONObject.getJSONObject("response");

            JSONArray newsJSONArray = newsJSONObjectResults.getJSONArray("results");

            for (int i = 0; i < newsJSONArray.length(); i++) {
                JSONObject results = newsJSONArray.getJSONObject(i);
                String section = results.getString(SECTION_NAME);
                String title = results.getString(KEY_TITLE);
                String url = results.getString(URL_NAME);

                boolean isDate = false;
                boolean isAuthor = false;
                String author = "";
                String date = "";
                // Check if there is a tag Array and "contributor" entry
                if (results.has("tags")) {
                    JSONArray tags = results.getJSONArray("tags");
                    if (!tags.isNull(0)) {
                        JSONObject authorObject = tags.getJSONObject(0);
                        if (authorObject.has("webTitle")
                                && authorObject.getString("webTitle") != null) {
                            isAuthor = true;
                            author = authorObject.getString("webTitle");
                        }
                    }
                }
                //Check if there is  a date in the article
                if (results.has("webPublicationDate") && results.getString("webPublicationDate") != null) {
                    isDate = true;
                    date = results.getString("webPublicationDate");
                } else {
                    isDate = false;
                    //news.add(new News(title, section, isDate, author, url));
                }
                if (isAuthor && isDate) {
                    news.add(new News(title, section, date, author, url));
                } else if (isAuthor) {
                    news.add(new News(title, section, isDate, author, url));
                } else {
                    news.add(new News(title, section, isDate, date, url));
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        // Return the list of news articles
        return news;
    }
}
