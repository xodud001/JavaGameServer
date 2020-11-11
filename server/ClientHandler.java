package dev.game.socket.server;

import java.net.Socket;

public class ClientHandler {
    Socket clientSocket;

    LoginThread loginThread;
    ServerSend serverSend;

    public void streamSetting() {
        try{
            clientSocket.getInetAddress();
            loginThread = new LoginThread(clientSocket);
            new Thread(loginThread).start();
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
