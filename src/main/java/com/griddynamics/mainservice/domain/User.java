package com.griddynamics.mainservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "users")
public class User {
    @Id
    private String _id;
    private String name;
    private String phone;

    public User() {
    }

    public User(String _id, String name, String phone) {
        this._id = _id;
        this.name = name;
        this.phone = phone;
    }
}
