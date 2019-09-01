package com.example.android.newsapp;

public class News {
    /**
     * Title of the article
     */
    private String mTitle;

    /**
     * Section the article falls into
     */
    private String mSection;

    /**
     * Date of this article(if provided)
     */
    private String mDate ;

    /**
     * Author of this article(if provided)
     */
    private String mAuthor;

    /**
     * URL of this article
     */
    private String mWebsiteUrl;

    /**
     * Create a new News object
     *
     * @param title is the title of the article
     * @param section  is the section of the article
     * @param websiteUrl  is the Web URL of the article
     */
    public News(String title, String section, String websiteUrl) {
        mTitle = title;
        mSection = section;
        mWebsiteUrl = websiteUrl;
    }

    /**
     * Create a new News object
     *
     * @param title is the title of the article
     * @param section  is the section of the article
     * @param authorOrDate is the date or the author of the article
     * @param websiteUrl  is the Web URL of the article
     */
    public News(String title, String section, boolean isDate, String authorOrDate, String websiteUrl) {
        mTitle = title;
        mSection = section;
        if (isDate) {
            mDate = authorOrDate;
        }else {
            mAuthor = authorOrDate;
        }
        mWebsiteUrl = websiteUrl;
    }

    /**
     * Create a new News object
     *
     * @param title is the title of the article
     * @param section  is the section of the article
     * @param date is the date the article was published
     * @param author is the authorof the article
     * @param websiteUrl  is the Web URL of the article
     */
    public News(String title, String section, String date, String author, String websiteUrl) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mAuthor = author;
        mWebsiteUrl = websiteUrl;
    }

    /**
     * Get the Title of the article.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Get the Author of the article.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Get the Section of the article.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Get the Date of the article.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Get the URL of the article.
     */
    public String getWebsiteUrl() {
        return mWebsiteUrl;
    }
}
