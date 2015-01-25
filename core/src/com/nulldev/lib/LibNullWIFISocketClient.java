package com.nulldev.lib;

import com.badlogic.gdx.Gdx;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nulldev on 24/01/15.
 *
 * Based On: https://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
 */

//Library to connect to the server via socket. (WIFI)
public class LibNullWIFISocketClient {

    public String serverIpAddress = "";
    public int serverPort = 0;

    public List<String> commandQueue;

    public boolean connected = false;

    Thread clientThread = new Thread(new ClientThread());

    public void connect(String ip) {
        if (!connected) {
            serverIpAddress =ip;
            if (!serverIpAddress.equals("")) {
                clientThread.start();
            }
        }
    }

    public void stop() {
        clientThread.interrupt();
    }

    public void sendCommand(String command) { commandQueue.add(command); }

    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Gdx.app.log("Client", "Connecting...");
                Socket socket = new Socket(serverAddr, serverPort);
                connected = true;
                PrintWriter out;
                if(connected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true);
                    while (connected) {
                        try {
                            //Loop command queue and remove commands executed
                            for (Iterator<String> iterator = commandQueue.iterator(); iterator.hasNext(); ) {
                                out.println(iterator.next());
                                iterator.remove();
                            }
                        } catch (Exception e) {
                            Gdx.app.error("Client", "Connection Closed", e);
                            connected = false;
                        }
                    }
                    //Resource leaks suck :(
                    out.close();
                    socket.close();
                    Gdx.app.log("Client", "Connection Closed");
                }
            } catch (Exception e) {
                Gdx.app.error("Client", "Connection Closed", e);
                connected = false;
            }
        }
    }
}
