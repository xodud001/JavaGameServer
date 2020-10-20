package dev.game.socket.server;

import com.sun.net.httpserver.Authenticator;
import dev.game.socket.database.DatabaseConnection;
import dev.game.socket.database.table.User;
import dev.game.socket.util.JsonParser;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerRecv implements Runnable{
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;


    public ServerRecv(Socket clientSocket) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        boolean isThread = true;
        while(isThread) {
            try {
                String recvData = in.readLine();
                JSONObject json= JsonParser.createJson(recvData);

                if(json != null) {
                    if (json.get("Header").equals("login")) {
                        DatabaseConnection db = DatabaseConnection.getConnector();
                        User user = new User();
                        user.setId((String)json.get("id"));
                        user.setPw((String)json.get("pw"));

                        if (db.userLogin(user)) {
                            System.out.println("SUCCESS");
                            out.println("SUCCESS");
                        }else{
                            System.out.println("FAIL");
                            out.println("FAIL");
                        }
                    }
                }
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
