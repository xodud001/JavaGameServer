package dev.game.socket.database;

import dev.game.socket.database.table.User;

import java.sql.*;

public class DatabaseConnection {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; // 드라이버
    private final String DB_URL = "jdbc:mysql://localhost:3306/user?&serverTimezone=UTC&useSSL=false";  // 접속할 DB 서버
    private final static String USER_NAME = "root"; // DB에 접속할 사용자 이름
    private final static String PASSWORD = "root"; // 사용자 비밀번호
    private static DatabaseConnection connector;
    private static Connection conn;

    public DatabaseConnection(){
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getConnector() {
        if(connector == null)
            connector = new DatabaseConnection();
        return connector;
    }

    public boolean createUser(User user) {
        String sql = "insert into user values(?, ?, ?, ?, ?);";
        PreparedStatement pstate = null;
        boolean result = false;
        try {
            pstate = conn.prepareStatement(sql);
            pstate.setString(1, user.getId());
            pstate.setString(2, user.getPw());
            pstate.setString(3, user.getName());
            pstate.setString(4, user.getBirth().toString());
            pstate.setString(5, user.getPhone());

            if(pstate.executeUpdate() == 1) {
                System.out.println("[회원가입 성공]");
                result = true;
            } else {
                System.out.println("[회원가입 실패]");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstate != null && !pstate.isClosed())
                    pstate.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean userLogin(User user) {
        String sql = "SELECT pw FROM user WHERE id = ?;";
        PreparedStatement pstate = null;
        boolean result = false;

        try {
            pstate = conn.prepareStatement(sql);
            pstate.setString(1, user.getId());
            ResultSet rs = pstate.executeQuery();
            if(rs.next()) {
                if(rs.getString(1).equals(user.getPw())) {
                    System.out.println("[로그인 성공]");
                    result = true;
                }else

                    System.out.println("[로그인 실패] ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstate != null && !pstate.isClosed())
                    pstate.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
