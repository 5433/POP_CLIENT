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

/**
 * @author Anmar Hindi
 */

public class HomeActivity extends ActionBarActivity {

    private Button bt1;
    private Button bt2;
    private Button bt5;
    private Socket socket;
    private String serverIpAddress = "10.0.2.2";
    private static final int ssPort = 7777;
    private TextView tv;
    public int threadPort;
    String ans = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv = (TextView) findViewById(R.id.textView);
        bt1 = (Button) findViewById(R.id.button4);
        bt2 = (Button) findViewById(R.id.button3);
        bt5 = (Button) findViewById(R.id.button5);
        new MessageHandler().execute();
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                    tv.setText("UNKNOWN HOST");
                    e.printStackTrace();
                } catch (IOException e) {
                    tv.setText("NO CONNECTION");
                    e.printStackTrace();
                } catch (Exception e) {
                    tv.setText("FATAL ERROR");
                    e.printStackTrace();
                }
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SocketClient.class);
                Bundle b = new Bundle();
                b.putInt("port", threadPort); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });
    }

    class MessageHandler extends AsyncTask<Void, Void, Void> {
        String result = "";

        public Void doInBackground(Void... param) {

            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                socket = new Socket(serverAddr, ssPort);
                while (true) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                    try {
                        String tempo = br.readLine();
                        String[] b = tempo.split("\t");
                        int it = Integer.parseInt(b[0]);
                        if (it > 7777) {
                            threadPort = it;
                        }

                    } catch (Exception e) {
                        //do nothing
                    } finally {
                        result = br.readLine();
                        onPostExecute(result);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //onPreExecute();
            //notify();
            return null;
        }

        public void onPreExecute() {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(100);
                    } catch (Exception e) {

                    }
                }
            };
            thread.start();

        }

        public void onPostExecute(String f) {
            final String st = f;

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        TextView tx3 = (TextView) findViewById(R.id.textView23);
                        tx3.setText(threadPort + "");

                        TextView tv = (TextView) findViewById(R.id.textView2);
                        String[] a = st.split("\t");
                        if (a.length > 1) {
                            int i = 0;
                            ans = "";
                            while (i < a.length) {

                                ans += a[i] + "\n";
                                i++;
                            }
                        }
                        tv.setText(ans);


                    } catch (Exception e) {

                    }
                }
            });

        }

    }
}
