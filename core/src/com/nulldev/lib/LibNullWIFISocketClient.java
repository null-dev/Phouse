package com.nulldev.lib;

import com.badlogic.gdx.Gdx;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by nulldev on 24/01/15.
 *
 * Based On: https://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
 */

//Library to connect to the server via socket. (WIFI)
public class LibNullWIFISocketClient {

    private static String serverIpAddress = "";
    public static int serverPort = 0;

    public static Queue<String> commandQueue = new LinkedList<String>();
    //public static List<String> commandQueue = new ArrayList<String>();

    public static boolean connected = false;

    static Thread clientThread = new Thread(new ClientThread());

    static PrintWriter out;
    static Socket socket;

    public static void connect(String ip) {
        if (!connected) {
            serverIpAddress =ip;
            if (!serverIpAddress.equals("")) {
                clientThread.start();
            }
        }
    }

    public static void stop() {
        connected = false;
    }

    public static void sendCommand(String command) { commandQueue.add(command); }

    public static class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Gdx.app.log("Client", "Connecting...");
                socket = new Socket(serverAddr, serverPort);
                connected = true;

                if(connected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true);
                    while (connected) {
                        try {
                            //Loop command queue and remove commands executed
                            if(!commandQueue.isEmpty())
                                out.println(commandQueue.poll());
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
