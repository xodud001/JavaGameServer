package dev.game.socket.server;

import java.net.Socket;

public class ClientHandler {
    Socket clientSocket;

    ServerRecv serverRecv;
    ServerSend serverSend;

    public void streamSetting() {
        try{
            clientSocket.getInetAddress();
            serverRecv = new ServerRecv(clientSocket);
            serverSend = new ServerSend(clientSocket);
            new Thread(serverRecv).start();
            new Thread(serverSend).start();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public ClientHandler(Socket clientSocket){
            this.clientSocket = clientSocket;
            streamSetting();
    }
}
