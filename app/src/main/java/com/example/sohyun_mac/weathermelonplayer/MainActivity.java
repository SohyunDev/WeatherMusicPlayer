package com.example.sohyun_mac.weathermelonplayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    TextView weatherView;
    TextView musicStatus;
    Button startMusic;
    Document doc = null;
    MediaPlayer mediaPlayer = null;
    String weather="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherView = (TextView) findViewById(R.id.weatherView);
        musicStatus = (TextView) findViewById(R.id.musicStatus);
        startMusic = (Button)findViewById(R.id.startMusic);
        startMusic.setText("플레이");
        startMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer== null){
                    playing();
                }
                else{
                    stop();
                }
            }
        });




        GetXMLTask task = new GetXMLTask();
        task.execute("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159068000");


    }

    private void playing(){
        String musicStatusText="";
        long time = System.currentTimeMillis();
        int random = (int)time%2;
        if(weather.equals("")){
            musicStatusText+="날씨 정보를 불러오지 못햇쩌용";
            return;
        }
        else if(weather.equals("맑음")) {
            if(random ==0) {
                musicStatusText += "The.madPix.project - Moments\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sunny0);
            }
            else{
                musicStatusText+="Kellee Maize - Crown\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sunny1);
            }
        }
        else if(weather.equals("구름조금")){
            if(random ==0) {
                musicStatusText += "QueenzTheLyricist - Hip-Hop Ft.Thelonious\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.littlecloud0);
            }
            else{
                musicStatusText+="Robin Grey - From The Ground Up\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.littlecloud1);
            }

        }
        else if(weather.equals("구름많음")) {
            if(random ==0) {
                musicStatusText += "Alchemy - We Were Cheerleaders\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lotcloud0);
            }
            else{
                musicStatusText+="Wordsmith - The Statement\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lotcloud1);
            }
        }
        else if(weather.equals("흐림")){
            if(random ==0) {
                musicStatusText += "Ralph Castelli - good morning\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.cloudy0);
            }
            else{
                musicStatusText+="THE DLX - Two Kids\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.cloudy1);
            }
        }
        else if(weather.equals("비")){
            if(random ==0) {
                musicStatusText += "Duo Teslar - Universal Funk\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy0);
            }
            else{
                musicStatusText+="Ivan Tregub - Thunder God\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy1);
            }
        }
        else if(weather.equals("눈/비")){
            if(random ==0) {
                musicStatusText += "Craze 24 - Rolling Original Clean\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sandr);
            }
            else{
                musicStatusText+="Ivan Tregub - Thunder God\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy1);
            }
        }
        else if(weather.equals("눈")){
            if(random ==0) {
                musicStatusText += "Melanie Ungar - Crazy Glue\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.snow0);
            }
            else{
                musicStatusText+="Robin Grey - Hymn For Her\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.snow1);
            }
        }
        else{
            if(random == 0) {
                musicStatusText += "상어가족 - 아기상어뚜루룯루두루~\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.shark1);
            }
            else{
                musicStatusText+="상어가족 빠른버전 - 아기상어뚜루둘두루루~\n";
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.shark2);
            }
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        musicStatusText+="재생중~";
        musicStatus.setText(musicStatusText);
        startMusic.setText("정지");
    }

    private void stop(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        musicStatus.setText("");
        startMusic.setText("플레이");
    }


    private class GetXMLTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc){

            NodeList nodeList = doc.getElementsByTagName("data");
            String s = "";
            s+= "현재 동작구 흑석동의 날씨\n";
            Node node = nodeList.item(0);
            Element fstElmnt = (Element) node;
            NodeList nameList = fstElmnt.getElementsByTagName("temp");
            Element nameElement = (Element)nameList.item(0);
            nameList = nameElement.getChildNodes();
            s+="온도 = "+((Node)nameList.item(0)).getNodeValue()+"°C, ";

            NodeList websiteList = fstElmnt.getElementsByTagName("wfKor");
            s+="날씨 = "+websiteList.item(0).getChildNodes().item(0).getNodeValue()+"\n";
            weather = websiteList.item(0).getChildNodes().item(0).getNodeValue().replace(" ","");
            weatherView.setText(s);
            super.onPostExecute(doc);
        }

    }





}
