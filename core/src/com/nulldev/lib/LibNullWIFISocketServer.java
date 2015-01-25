package com.nulldev.lib;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

/**
 * Created by hc on 24/01/15.
 *
 * Based On: https://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
 */
public class LibNullWIFISocketServer {
    // DEFAULT IP
    public static String serverIP = "0.0.0.0";

    // DESIGNATE A PORT
    public static final int SERVERPORT = 8080;

    private ServerSocket serverSocket;

    Thread serverThread = new Thread(new ServerThread());

    public void startServer() {
        serverIP = getLocalIpAddress();
        serverThread.start();
    }

    public void stopServer() {
        try {
            //Close the socket
            serverSocket.close();
        } catch (IOException e) {
            Gdx.app.error("Unable to stop server:", e.toString());
            e.printStackTrace();
        }
    }

    public class ServerThread implements Runnable {

        public void run() {
            try {
                if (serverIP != null) {
                    serverSocket = new ServerSocket(SERVERPORT);
                    while (true) {
                        // LISTEN FOR INCOMING CLIENTS
                        Socket client = serverSocket.accept();

                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null) {
                                //TODO COMMAND RECEIVED
                                Gdx.app.log("Command received:", line);
                            }
                            break;
                        } catch (Exception e) {
                            //TODO CONNECTION LOST
                            e.printStackTrace();
                        }
                    }
                } else {
                    //TODO NO INTERNET CONNECTION
                }
            } catch (Exception e) {
                //TODO UNKNOWN ERROR
                e.printStackTrace();
            }
        }
    }

    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress(); }
                }
            }
        } catch (Exception ex) {
            //TODO UNABLE TO GET IP ADDRESS???
            Gdx.app.error("Unable to get IP Address:", ex.toString());
        }
        return null;
    }
}
