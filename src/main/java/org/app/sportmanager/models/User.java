package org.app.sportmanager.models;

public class User {

    private Long id;
    private String nickname;
    private String password;

    public User(Long id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }
}
