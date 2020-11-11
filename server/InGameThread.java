package dev.game.socket.server;

import dev.game.socket.game_data.Crewmate;
import dev.game.socket.game_data.Room;
import dev.game.socket.util.JsonParser;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("unchecked")
public class InGameThread implements Runnable{

    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;

    public InGameThread(Socket clientSocket) throws IOException {
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

                JSONObject requestJson = JsonParser.createJson(recvData); // 클라이언트에게 받은 데이터를 JSON 으로 파싱
                if (requestJson != null) {
                    if(requestJson.get("Header").equals("LOGOUT")){ // 로그아웃 할 때
                        LoginThread  loginThread = new LoginThread(clientSocket);
                        new Thread(loginThread).start();
                        System.out.println("[LOGOUT]");
                        isThread=false;
                    }
                    else if(requestJson.get("Header").equals("ENTER")){
                        Room room = Room.getRoom();
                        JSONObject roomInfoJson = new JSONObject();
                        roomInfoJson.put("code", room.getRoomCode());
                        JSONObject crewmatesJson = new JSONObject();

                        room.getCrewmates().add(new Crewmate());
                        for (Crewmate crewmate : room.getCrewmates()){ // 기존 방에 있던 크루원들 초기 정보
                            crewmatesJson.put(room.getCrewmates().indexOf(crewmate), crewmate.getInitCrewmateJson());
                        }
                        roomInfoJson.put("crewmates", crewmatesJson);
                        out.println(roomInfoJson.toJSONString());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if(clientSocket != null)
                        clientSocket.close();
                    in.close();
                    out.close();
                } catch (IOException ioE) {
                    ioE.printStackTrace();
                }
                isThread = false;
            }
        }
    }
}
