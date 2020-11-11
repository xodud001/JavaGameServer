package dev.game.socket.util;

import dev.game.socket.database.DatabaseConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Connection;

public class JsonParser {
    private static JSONParser jParser = new JSONParser();

    public static JSONObject createJson(String data) throws ParseException {
        return (JSONObject)jParser.parse(data);
    }
}
