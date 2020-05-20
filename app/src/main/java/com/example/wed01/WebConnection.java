package com.example.wed01;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebConnection {
    public String request(String _url, ContentValues _params, String _method) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuffer sbParams = new StringBuffer();

        // 1. StringBuffer에 파라미터 연결
        if (_params == null) sbParams.append("");
        else {
            boolean isAnd = false;
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                if (isAnd) sbParams.append("&");
                sbParams.append(key).append("=").append(value);

                if (!isAnd) {
                    if (_params.size() >= 2) isAnd = true;
                }
            }
        }

        // 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
        try {
            URL url = new URL(_url);//url을 가져온다.

            urlConnection = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            if(_method.equals("POST")) {
                urlConnection.setRequestMethod(_method); // URL 요청에 대한 메소드 설정 : POST.
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                urlConnection.setConnectTimeout(2 * 1000);
                urlConnection.connect();//연결 수행

                // [2-2]. parameter 전달 및 데이터 읽어오기.
                String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
                OutputStream os = urlConnection.getOutputStream();
                os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
            }
            else {
                urlConnection.setRequestMethod(_method); // URL 요청에 대한 메소드 설정 : GET.
//                urlConnection.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setConnectTimeout(2 * 1000);
                urlConnection.connect();//연결 수행
            }

            //입력 스트림 생성

            InputStream stream = urlConnection.getInputStream();

            //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.

            reader = new BufferedReader(new InputStreamReader(stream));

            //실제 데이터를 받는곳

            StringBuffer buffer = new StringBuffer();

            //line별 스트링을 받기 위한 temp 변수

            String line = "";

            //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.

            while((line = reader.readLine()) != null){

                buffer.append(line);

            }

            //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String… urls) 니까

            return buffer.toString();
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            try {

                //버퍼를 닫아준다.

                if(reader != null){

                    reader.close();

                }

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        return null;
    }
}