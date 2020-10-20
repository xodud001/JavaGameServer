package dev.game.socket.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerSend implements Runnable{

    Socket clientSocket;
    PrintWriter out;

    public ServerSend(Socket clientSocket) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        Scanner input=new Scanner(System.in);
        boolean isThread=true;

        while(isThread) {
            try {
                String sendData = input.nextLine();
                if (sendData.equals("/quit")) {
                    isThread = false;
                }
                else
                    out.println(sendData);
            } catch (Exception e) {
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
    }
}
