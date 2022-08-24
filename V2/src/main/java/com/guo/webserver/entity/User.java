package com.guo.webserver.entity;

import java.io.Serializable;

public class User implements Serializable {
  private   String name;
   private String password;
   private String nickname;
   private int age;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User(String name, String password, String nickname, int age) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
    }
}
