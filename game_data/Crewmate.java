package dev.game.socket.game_data;

import org.json.simple.JSONObject;

public class Crewmate {
    String owner; // 캐릭터 주인 아이디

    int x; // 캐릭터 x 좌표
    int y; // 캐릭터 y 좌표

    String name; // 캐릭터 이름
    String color; // 캐릭터 컬러
    String state; //바라 보고 있는 방향 UP, DOWN, LEFT, RIGHT

    int maxHP; // 최대 체력
    int HP; // 현재 체력

    boolean isStop; // 상태 정보
    float stateTimer; // 상태 시간

    public Crewmate(String owner){
        this.owner = owner;
        this.x = 150;
        this.y = 150;
        this.name = "성경이";
        this.color = "Red";
        this.state="DOWN";
        this.maxHP = 10;
        this.HP = 10;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getInitCrewmateJson(){ // 처음 유저가 입장 할 때는 전부 받아야 하니까 전부 출력
        JSONObject result = new JSONObject();

        result.put("owner", owner); //
        result.put("x", x);
        result.put("y", y);
        result.put("name", name); //
        result.put("color", color); //
        result.put("state", state);
        result.put("maxHP", maxHP);
        result.put("HP", HP);
        result.put("isStop", isStop);
        result.put("stateTimer", stateTimer);
        return result;
    }
    @SuppressWarnings("unchecked")
    public JSONObject getUpdateCrewmateJson(){ // 지속적으로 업데이트 해야할 정보
        JSONObject result = new JSONObject();

        result.put("x", x);
        result.put("y", y);
        result.put("state", state);
        result.put("maxHP", maxHP);
        result.put("HP", HP);
        result.put("isStop", isStop);
        result.put("stateTimer", stateTimer);
        return result;
    }

    public void update(JSONObject requestJson) {
        double temp = Double.parseDouble(requestJson.get("x").toString());
        this.x = (int)temp;
        temp = Double.parseDouble(requestJson.get("y").toString());
        this.y = (int)temp;
        temp = Double.parseDouble(requestJson.get("maxHP").toString());
        this.maxHP = (int)temp;
        temp = Double.parseDouble(requestJson.get("HP").toString());
        this.HP = (int)temp;

        this.isStop = requestJson.get("isStop").toString().equals("true");
        temp = Double.parseDouble(requestJson.get("stateTimer").toString());
        this.stateTimer = (float)temp;

        this.name = requestJson.get("name").toString();
        this.color = requestJson.get("color").toString();
        this.state = requestJson.get("state").toString();
    }
}
