package org.app.sportmanager.repositories;

import org.app.sportmanager.HashUtil;
import org.app.sportmanager.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public Long addUser(String username, String password){
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        long generated_id = (long) -1;
        String hashPassword = HashUtil.hashPassword(password);

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){

            statement.setString(1,username);
            statement.setString(2,hashPassword);
            int affectedRows = statement.executeUpdate();

            if(affectedRows > 0){
                try(ResultSet generstedKeys = statement.getGeneratedKeys()){
                    if(generstedKeys.next()){
                        generated_id = generstedKeys.getLong(1);
                    }
                }
            }
            else{
                throw new SQLException("No rows affected");
            }

            System.out.println("User(" + username + ") added");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generated_id;
    }

    public boolean signIn(String username, String password){
        String query = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1,username);

            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    String hashPassword = resultSet.getString("password");
                    return HashUtil.checkPassword(password, hashPassword);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean checkUserExist(String username){
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1,username);

            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
