package com.nulldev.lib;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

/**
 * Created by nulldev on 24/01/15.
 *
 * Based On: https://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
 */
public class LibNullWIFISocketServer {
    // DEFAULT IP
    public static String serverIP = "0.0.0.0";

    // DESIGNATE A PORT
    public static final int SERVERPORT = 29992;

    private static ServerSocket serverSocket;

    public static Thread serverThread = null;

    private static boolean running = false;
    
    public static void startServer() {
    	running = true;
    	serverThread = new Thread(new ServerThread());
    	if(serverIP == "0.0.0.0") {
    		serverIP = getLocalIpAddress();
    	}
        serverThread.start();
        System.out.println("Server started on WIFI on " + serverIP + ":" + SERVERPORT);
    }

    public static void stopServer() {
    	System.out.println("Stopping server...");
    	running = false;
    	//Wait for thread to die if it's looping
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
    	//Thread is still not dead, kill the thread
    	serverThread.stop();
    	try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	System.out.println("Server stopped!");
    }

    public static class ServerThread implements Runnable {

        public void run() {
            try {
                if (serverIP != null) {
                    serverSocket = new ServerSocket(SERVERPORT);
                    while (running) {
                        // LISTEN FOR INCOMING CLIENTS
                        Socket client = serverSocket.accept();
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null && running) {
                                //TODO COMMAND RECEIVED
                            	System.out.println("COMMAND RECEIVED!" + line);
                            }
                        } catch (Exception e) {
                            //TODO CONNECTION LOST
                            e.printStackTrace();
                        }
                    }
                    serverSocket.close();
                    System.out.println("All clients disconnected!");
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
    private static String getLocalIpAddress() {
        try {
            return getLocalHostLANAddress().getHostAddress().toString();
        } catch (Exception ex) {
            //TODO UNABLE TO GET IP ADDRESS???
        	System.out.println("Unable to get IP address!");
        	ex.printStackTrace();
        }
        return null;
    }
    
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        }
                        else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        }
        catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
}
