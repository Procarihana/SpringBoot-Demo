package com.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class User implements Serializable {
    BigInteger id;
    String username;
    String avatar;
    @JsonIgnore
    String encryptedPassword;
    @JsonIgnore
    Instant createdAt;
    @JsonIgnore
    Instant updatedAt;

    public User(BigInteger id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }

    public User(BigInteger id, String username, String encryptedPassword, String avatar) {
        this.id = id;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.avatar = avatar;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

    }


    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}

