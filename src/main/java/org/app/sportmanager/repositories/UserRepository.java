package org.app.sportmanager.repositories;

import org.app.sportmanager.HashUtil;
import org.app.sportmanager.MySQLConnection;
import org.app.sportmanager.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

    public Integer signIn(String username, String password){
        String query = "SELECT id, password FROM users WHERE username = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1,username);

            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    String hashPassword = resultSet.getString("password");
                    if (HashUtil.checkPassword(password, hashPassword)){
                        return Integer.parseInt(resultSet.getString("id"));
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
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

    public boolean deleteUser(User user){
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, user.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User with ID " + user.getId() + " deleted");
                return true;
            } else {
                System.out.println("User with ID " + user.getId() + " not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean updateUser(User user){
        String query = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        String hashPassword = HashUtil.hashPassword(user.getPassword());

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, user.getNickname());
            statement.setString(2, hashPassword);
            statement.setLong(3, user.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User with ID " + user.getId() + " updated");
                return true;
            } else {
                System.out.println("User with ID " + user.getId() + " not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



}
