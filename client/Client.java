package dev.game.socket.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    BufferedReader in;
    PrintWriter out;

    String serverIPv4 = "127.0.0.1";
    int serverPort = 10002;

    String header = "\"login\"";
    String id = "\"xodud\"";
    String pw = "\"xodud\"";

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        connect();
        streamSetting();
        dataSend();
        dataRecv();
    }    

    public void connect() {
        try{
            clientSocket=new Socket(serverIPv4, serverPort);
            System.out.println("[Client] Connected to server");
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public void streamSetting() {
        try{
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }


    public void dataRecv() {
        new Thread(new Runnable() {
            boolean isThread = true;
            @Override
            public void run() {
                while(isThread)
                    try {
                        String recvData=in.readLine();
                        if(recvData.equals("/quit"))
                            isThread=false;
                        else
                            System.out.println("Server : " + recvData);
                    }
                    catch (Exception e) {
                        System.out.println(e.toString());
                        try {
                            if(clientSocket != null)
                                clientSocket.close();
                            in.close();
                        } catch (IOException ioE) {
                            ioE.printStackTrace();
                        }
                        isThread = false;
                    }
            }
        }).start();
    }
    public void dataSend() {
        new Thread(new Runnable() {
            Scanner input=new Scanner(System.in);
            boolean isThread=true;
            @Override
            public void run() {
                while(isThread)
                    try {
                        String sendData=input.nextLine();
                        if(sendData.equals("/quit"))
                            isThread=false;
                        else {
                            String login = String.format("{\"Header\":%s,\"id\":%s,\"pw\":%s}", header, id, pw);
                            System.out.println(login);
                            out.println(login);
                        }
                    }
                    catch (Exception e) {
                        System.out.println(e.toString());
                        try {
                            if(clientSocket != null)
                                clientSocket.close();
                            out.close();
                        } catch (IOException ioE) {
                            ioE.printStackTrace();
                        }
                        isThread = false;
                    }
            }
        }).start();
    }
}


