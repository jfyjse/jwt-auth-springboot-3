package com.jwt.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;


    @Column(name = "is_logged_out")
    private boolean loggedOut;

   @Column(name = "user_id")
    private String userId;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isLoggedOut() {
        return !loggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        this.loggedOut = loggedOut;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
