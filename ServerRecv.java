package dev.game.socket;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerRecv implements Runnable{
    Socket clientSocket;
    BufferedReader in;

    public ServerRecv(Socket clientSocket) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        boolean isThread = true;
        while(isThread) {
            try {
                String recvData = in.readLine();
                if (recvData.equals("/quit")) {
                    isThread = false;
                }
                else
                    System.out.println("Client : " + recvData);
            } catch (Exception e) {
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
    }
}
