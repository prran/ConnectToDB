package com.example.analysetableinmysql

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ConnectToServer(netInfo:Array<out String>) : AsyncTask<String, String, String>(){

    val netInfo = netInfo
    var sqlOver : SQLOverListener? = null;
    val URL : String = "http://XX.XX.XX.XX:3000/post"

    override fun doInBackground(vararg params: String?): String {

        try {

            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

            val jsonObject : JSONObject = JSONObject()
            jsonObject.accumulate("HOST", netInfo.get(0));
            jsonObject.accumulate("DB", netInfo.get(1));
            jsonObject.accumulate("ID", netInfo.get(2));
            jsonObject.accumulate("PASSWORD", netInfo.get(3));
            jsonObject.accumulate("QUERY", params.get(0));

            var con : HttpURLConnection? = null
            var reader : BufferedReader? = null

            try{
                val url = URL(URL)


                con = url.openConnection() as HttpURLConnection;

                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "text/plain");//서버에 response 데이터를 text로 받음

                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미

                con.connect();

                //서버로 보내기위해서 스트림 만듬 //버퍼를 생성하고 넣음

                val outStream = con.getOutputStream();
                val writer = BufferedWriter(OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();//버퍼를 받아줌

                if(con.responseCode != HttpURLConnection.HTTP_OK)
                {
                    Log.e("Connection failed" , "Wrong connection : " + con.responseCode)
                    return String()
                }
                else
                {
                    Log.e("Connection Success" , "connection success")
                }


                //서버로 부터 데이터를 받음
                val stream = con.getInputStream();

                reader = BufferedReader(InputStreamReader(stream))

                val buffer = StringBuilder();
                var line: String?
                do {
                    line = reader.readLine();
                    buffer.append(line);
                } while (line != null)


                return buffer.toString();//서버로 부터 받은 값을 리턴

            } catch (e: MalformedURLException){
                e.printStackTrace();
            } catch (e: IOException) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();//버퍼를 닫아줌
                    }
                } catch (e:IOException) {
                    e.printStackTrace();
                }
            }
        } catch (e:Exception) {
            e.printStackTrace();
        }

        return String()
    }

    protected override fun onPostExecute(result:String) {
        sqlOver?.onSQLOvered()
    }

    fun setSQLOverListener(overListener : SQLOverListener)
    {
        sqlOver = overListener
    }
}

interface SQLOverListener
{
    abstract fun onSQLOvered()
}