package com.example.elhamadamo.socketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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


public class SocketClient extends Activity {
    private Button bt;
    private Button bt2;
    private Button bt3;
    private TextView tv;
    private Socket socket;
    private String serverIpAddress = "10.0.2.2";
    //private String serverIpAddress = "192.168.0.5";

    EditText et;
    String str = "";
    TextView tv2;
    BufferedReader br;
    PrintWriter out;

    //Thread port
    private int path_threadPort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);

        Bundle b = getIntent().getExtras();
        path_threadPort = b.getInt("port");

        bt = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        et = (EditText) findViewById(R.id.editText);
        new MessageHandler().execute();

        bt2.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                System.exit(0);
            }
        });

        bt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {

                    str = et.getText().toString();
                    out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(str);
                    Log.d("Client", "Client sent message");
                } catch (UnknownHostException e) {
                    tv.setText("UnknownHostException");
                    e.printStackTrace();
                } catch (IOException e) {
                    tv.setText("IOException");
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    tv.setText("NullPointerException");
                    e.printStackTrace();
                } catch(Exception e){
                    tv.setText("FATAL ERROR");
                    e.printStackTrace();
                }
            }
        });

        bt3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocketClient.this, HomeActivity.class);
                try{
                    socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }

                startActivity(intent);
                finish();
            }
        });
    }

    class MessageHandler extends AsyncTask<Void,Void,Void>{
        String result = "";
        public Void doInBackground(Void... params){
            try{
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                socket = new Socket(serverAddr, path_threadPort);
                while(true){
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    result = br.readLine();
                    onPostExecute(result);
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
                    tv2.setText(st);
                }
            });

        }

    }

}