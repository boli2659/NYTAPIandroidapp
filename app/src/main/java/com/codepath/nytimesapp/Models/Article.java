package com.codepath.nytimesapp.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable{

    String webUrl;
    String headline;

    public String getThumbnail() {
        return thumbnail;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    String thumbnail;

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject jsonPic = multimedia.getJSONObject(0);
                this.thumbnail =  "http://www.nytimes.com/" + jsonPic.getString("url");
            }
            else {
                this.thumbnail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> articles = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                articles.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }
}
