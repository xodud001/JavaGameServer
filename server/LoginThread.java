package dev.game.socket.server;

import com.sun.net.httpserver.Authenticator;
import dev.game.socket.database.DatabaseConnection;
import dev.game.socket.database.table.User;
import dev.game.socket.util.JsonParser;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class LoginThread implements Runnable{
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    DatabaseConnection db = DatabaseConnection.getConnector();

    public LoginThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        boolean isThread = true;
        while(isThread) {
            try {
                String recvData = in.readLine();

                System.out.println("Client: " + recvData);

                JSONObject json = JsonParser.createJson(recvData); // 클라이언트에게 받은 데이터를 JSON 으로 파싱
                if(json != null) {
                    // 로그인
                    if(json.get("Header").equals("LOGIN")) { // 파싱 데이터의 "Header"가 "login"일 떄
                        User user = new User(); // 유저 객체 생성
                        user.setId((String) json.get("id"));
                        user.setPw((String) json.get("pw"));

                        if (db.userLogin(user)) {
                            out.println("SUCCESS");
                            System.out.println("[SUCCESS] 로그인");

                            InGameThread inGameThread = new InGameThread(clientSocket);
                            new Thread(inGameThread).start();
                            isThread=false;
                        } else {
                            out.println("FAIL");
                            System.err.println("[FAIL] 로그인");
                        }
                    }
                    // 회원가입
                    else if(json.get("Header").equals("CREATE")) {
                        User user = new User();
                        user.setName((String)json.get("name"));
                        user.setBirth((String)json.get("birth"));
                        user.setPhone((String)json.get("phone"));
                        user.setId((String)json.get("id"));
                        user.setPw((String)json.get("pw"));

                        if (db.createUser(user)) {
                            out.println("SUCCESS");
                            System.out.println("[SUCCESS] 회원가입");
                        } else {
                            out.println("FAIL");
                            System.err.println("[FAIL] 회원가입");
                        }
                    }
                    // id 찾기
                    else if(json.get("Header").equals("ID")) {
                        User user = new User();
                        user.setName((String)json.get("name"));
                        user.setBirth((String)json.get("birth"));

                        String result = db.findID(user);
                        if ( result != null) {
                            out.println(result);
                            System.out.println("[SUCCESS] ID 찾기 : " + result);
                        } else {
                            out.println("FAIL");
                            System.err.println("[FAIL] ID 찾기");
                        }
                    }
                    // pw 찾기
                    else if(json.get("Header").equals("PW")) {
                        User user = new User();
                        user.setId((String)json.get("id"));
                        user.setName((String)json.get("name"));
                        String result = db.findPW(user);
                        if (result != null) {
                            out.println(result);
                            System.out.println("[SUCCESS] PW 찾기 : " + result);
                        } else {
                            out.println("FAIL");
                            System.err.println("[FAIL] PW 찾기");
                        }
                    }
                    else {
                        out.println("[WRONG] 잘못된 데이터");
                        System.out.println("[WRONG] 잘못된 데이터");
                    }
                }
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
