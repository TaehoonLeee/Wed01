package com.example.wed01;

import android.content.ContentValues;
import android.os.AsyncTask;

public class AsyncHttp extends AsyncTask<Void, Void, String> {
    public AsyncHttp() { super(); }

    public AsyncHttp(String _url, ContentValues _contentValues) {
        this.url += _url;
        this.contentValues = _contentValues;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;
        WebConnection webConnection = new WebConnection();
        result = webConnection.request(url, contentValues);

        return result;
    }

    private String url = "https://wendsteam06.herokuapp.com/";
    private ContentValues contentValues;
}
