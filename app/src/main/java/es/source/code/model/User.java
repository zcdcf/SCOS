package es.source.code.model;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String password;
    private boolean oldUser;

    public User(String userName, String password, boolean isOldUser) {
        this.userName = userName;
        this.password = password;
        oldUser = isOldUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOldUser() {
        return oldUser;
    }

    public void setOldUser(boolean flag) {
        oldUser = flag;
    }
}
