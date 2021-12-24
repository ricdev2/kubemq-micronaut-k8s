package com.ric.dev2.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name = "event_msg")
public class RegistrationMessageEntity implements Serializable {

    @Id
    private String id;

    @Column(name = "body")
    private String body;

    @Column(name = "update_day")
    private Timestamp createDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreateDay() {
        return createDay;
    }

    public void setCreateDay(Timestamp createDay) {
        this.createDay = createDay;
    }
}
