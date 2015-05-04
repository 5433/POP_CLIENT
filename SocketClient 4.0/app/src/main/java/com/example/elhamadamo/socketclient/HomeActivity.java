package com.example.elhamadamo.socketclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class HomeActivity extends ActionBarActivity {

    private Socket socket;
    private String serverIpAddress = "10.0.2.2";
    //private String serverIpAddress = "192.168.0.5";
    private static final int ssPort = 7777;
    public int threadPort;
    String ans = "";
    InetAddress serverAddr;

    private Button bt1;
    private Button bt2;
    private Button bt5;

    //TextView tx3;
    TextView tv;

    EditText et;

    BufferedReader br;
    PrintWriter out;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bt1 = (Button) findViewById(R.id.button4);
        bt2 = (Button) findViewById(R.id.button3);
        bt5 = (Button) findViewById(R.id.button5);

        //tx3 = (TextView) findViewById(R.id.textView23);
        //tv = (TextView) findViewById(R.id.textView2);

        et = (EditText) findViewById(R.id.editText);
        new MessageHandler().execute();


        bt1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    tv = (TextView) findViewById(R.id.textView2);
                    str = et.getText().toString();
                    out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(str);
                    Log.d("Client", "Client sent message");
                    Log.d("Server", "received: " + threadPort);
                } catch (UnknownHostException e) {
                    tv.setText("Error1");
                    e.printStackTrace();
                } catch (IOException e) {
                    tv.setText("Error2");
                    e.printStackTrace();
                } catch (Exception e) {
                    tv.setText("Error3");
                    e.printStackTrace();
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                System.exit(0);
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(threadPort > 7777){
                    Intent intent = new Intent(HomeActivity.this, SocketClient.class);
                    Bundle b = new Bundle();
                    b.putInt("port", threadPort); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    try{
                        socket.close();
                    }catch(IOException e){

                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    class MessageHandler extends AsyncTask<Void,Void,Void> {
        String result = "";
        public Void doInBackground(Void... param){
            try{
                serverAddr = InetAddress.getByName(serverIpAddress);
                socket = new Socket(serverAddr,ssPort);
                while(true){
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    try{
                        String tempo = br.readLine();
                        String[] b = tempo.split("\t");

                        int it = Integer.parseInt(b[0]);
                        if(it > 7777){
                            threadPort = it;
                        }

                    }catch(Exception e){
                        //do nothing
                    }finally{
                            result = br.readLine();
                            onPostExecute(result);

                    }

                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        public void onPostExecute(String f){
            final String st = f;

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try{
                        tv = (TextView) findViewById(R.id.textView23);
                        tv.setText(threadPort + "");

                        String[] a = st.split("\t");
                        if(a.length > 1){
                            int i = 0;
                            ans = "";
                            while(i < a.length){

                                ans += a[i] + "\n";
                                i++;
                            }
                        }
                        tv = (TextView) findViewById(R.id.textView2);
                        tv.setText(ans);


                    }catch(Exception e){

                    }

                }
            });

        }

    }
}
