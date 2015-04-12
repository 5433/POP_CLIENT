package com.example.elhamadamo.socketclient;

import android.app.Activity;
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
    private TextView tv;
    private Socket socket;
    private String serverIpAddress = "10.0.2.2";
    // AND THAT'S MY DEV'T MACHINE WHERE PACKETS TO
    // PORT 5000 GET REDIRECTED TO THE SERVER EMULATOR'S
    // PORT 6000
    private static final int REDIRECTED_SERVERPORT = 8889;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);
        bt = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);
        tv = (TextView) findViewById(R.id.textView);
        new MessageHandler().execute();
        try {
            //InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
            //socket = new Socket(serverAddr, REDIRECTED_SERVERPORT);




        } catch (Exception e1) {
            e1.printStackTrace();
        }// catch (IOException e1) {
         //   e1.printStackTrace();
        //}

        bt2.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                System.exit(0);
            }
        });

        bt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    EditText et = (EditText) findViewById(R.id.editText);
                    String str = et.getText().toString();
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(str);
                    Log.d("Client", "Client sent message");

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
    }

    class MessageHandler extends AsyncTask<Void,Void,Void>{
        String result = "";
        public Void doInBackground(Void... params){
            try{
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                socket = new Socket(serverAddr, REDIRECTED_SERVERPORT);
                while(true){
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                    TextView tv = (TextView) findViewById(R.id.textView2);
                    tv.setText(st);

                }
            });

        }

    }

}