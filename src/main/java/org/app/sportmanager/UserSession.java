package org.app.sportmanager;

import org.app.sportmanager.models.User;

public class UserSession {
    private static UserSession instance;

    private User currentUser;

    private UserSession(){}

    public static UserSession getInstance(){
        if (instance == null){
            instance = new UserSession();
        }
        return instance;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }
}
