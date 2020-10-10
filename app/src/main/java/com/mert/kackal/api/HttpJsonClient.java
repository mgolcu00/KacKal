package com.mert.kackal.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HttpJsonClient {
    public static class GetNewsFromJsonTask extends AsyncTask<Void, Void, List<NewsModel>> {

        private Context context;
        private List<NewsModel> newsModels;
        private Fragment fragment;
        private static final String API_URL = "https://api.collectapi.com/food/calories?query=";
        private static final String API_KEY = "apikey 5YHVatlergufQ9OPhVecYy:5ztA4BplcWa0PJG5ByVlEy";
        private static final String API_TYPE = "application/json";
        private static final String API_RSS_URL = "https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fonedio.com%2Fsupport%2Frss.xml%3Fcategory%3D50187b5d295c043264000144";

        public GetNewsFromJsonTask(Context context, List<NewsModel> newsModels, Fragment fragment) {
            this.context = context;
            this.newsModels = newsModels;
            this.fragment = fragment;
        }

        public GetNewsFromJsonTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<NewsModel> doInBackground(Void... voids) {
            newsModels = new ArrayList<NewsModel>();
            try {
                URL url = new URL(API_RSS_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestProperty("authorization", API_KEY);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("content-type", "application/json");

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String json = bufferedReader.readLine();
                    JSONTokener jsonTokener = new JSONTokener(json);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    Log.i("HttpJson", String.valueOf(jsonArray));

                } else {
                    //Baglanti basarisiz oldugu kisim
                    Log.i("HttpClient : ", "Connection failed : " + httpURLConnection.getResponseCode());
                }
            } catch (IOException e) {

                //Baglantı hataso
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return newsModels;


        }

        @Override
        protected void onPostExecute(List<NewsModel> newsModels) {
            super.onPostExecute(newsModels);
        }
    }

    public static class NewsModel {
        private String title;
        private String pubDate;
        private String link;
        private String guid;
        private String author;
        private String thumbnail;
        private String description;
        private String content;
        private Object enclosure;
        private String[] categories;

        public NewsModel(String title, String pubDate, String link, String guid, String author, String thumbnail, String description, String content, Object enclosure, String[] categories) {
            this.title = title;
            this.pubDate = pubDate;
            this.link = link;
            this.guid = guid;
            this.author = author;
            this.thumbnail = thumbnail;
            this.description = description;
            this.content = content;
            this.enclosure = enclosure;
            this.categories = categories;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getEnclosure() {
            return enclosure;
        }

        public void setEnclosure(Object enclosure) {
            this.enclosure = enclosure;
        }

        public String[] getCategories() {
            return categories;
        }

        public void setCategories(String[] categories) {
            this.categories = categories;
        }
    }
}

