package net.year4000.mobile.android.news;

import lombok.Getter;

@Getter
public class NewsBlogItem {
    private String title;
    private String date;
    private String content;

    public NewsBlogItem(String title, String date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }
}
