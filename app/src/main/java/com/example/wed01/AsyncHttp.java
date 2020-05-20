package com.example.wed01;

import android.content.ContentValues;
import android.os.AsyncTask;

public class AsyncHttp extends AsyncTask<Void, Void, String> {
    public AsyncHttp() { super(); }

    public AsyncHttp(String _url, ContentValues _contentValues, String _method) {
        this.url += _url;
        this.contentValues = _contentValues;
        this.method = _method;
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
        result = webConnection.request(url, contentValues, method);

        return result;
    }

    private String url = "http://e0015981.ngrok.io/v2/";
    private ContentValues contentValues;
    private String method;
}
