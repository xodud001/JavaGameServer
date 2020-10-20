package dev.game.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;
    private static ArrayList<ClientHandler> clientList = new ArrayList<>();

    public void serverSetting() {
        try{
            System.out.println("[Server] ---JAVA GAME SERVER 가동---");
            serverSocket = new ServerSocket(10002); // 생성&바인드
            System.out.println("[Server] socket create and bind");

        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public Server() {
        serverSetting();
    }

    public static void main(String[] args) {
        Socket clientSocket = null;
        Server s = new Server();
        while(true) {
            try {
                clientList.removeIf(ch -> ch.clientSocket.isClosed());
                System.out.println("[Server] Waiting for client connection...");
                System.out.println("[Server] Connected Client : " + clientList.size());
                clientSocket = s.serverSocket.accept();
                System.out.println("[Server] Connected to client!");
                ClientHandler client = new ClientHandler(clientSocket);
                Server.clientList.add(client);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}