package com.imooc.imooc_res02.bean;

import java.io.Serializable;

/**
 * Created by Gln on 2017/4/26.
 */
public class User  implements Serializable {
    private int id;
    private String password;
    private String username;
    private int icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
