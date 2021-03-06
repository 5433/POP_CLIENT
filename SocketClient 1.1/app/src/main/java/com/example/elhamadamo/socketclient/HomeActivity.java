package com.example.elhamadamo.socketclient;

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

    private Button bt1;
    private Button bt2;
    private Socket socket;
    private String serverIpAddress = "10.0.2.2";
    private static final int ssPort = 7777;
    private TextView tv;
    public int threadPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv = (TextView) findViewById(R.id.textView);
        bt1 = (Button) findViewById(R.id.button4);
        bt2 = (Button) findViewById(R.id.button3);
        new MessageHandler().execute();
        bt2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                System.exit(0);
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    EditText et = (EditText) findViewById(R.id.editText);
                    String str = et.getText().toString();
                    PrintWriter out = new PrintWriter(new BufferedWriter(
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
    }

    class MessageHandler extends AsyncTask<Void,Void,Void> {
        String result = "";
        public Void doInBackground(Void... param){

            try{

                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                socket = new Socket(serverAddr,ssPort);
                while(true){
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if(!br.readLine().contains("1234567890")){
                        result = br.readLine();
                        onPostExecute(result);
                    }else{
                        threadPort = Integer.parseInt(br.readLine());
                        System.out.println(threadPort);
                    }
                    notify();
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
                        TextView tv = (TextView) findViewById(R.id.textView2);
                        String[] a = st.split("\t");
                        //tv.setText(a[0] + "\n" + a[1] + a[2] + "\n" + a[3] + a[4]);
                        tv.setText(a[0] + "\n" + a[1] + "\n" + a[2]);
                        wait(1000);
                    }catch(Exception e){

                    }
                }
            });

        }

    }
}
