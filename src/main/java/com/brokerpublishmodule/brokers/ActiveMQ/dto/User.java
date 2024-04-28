package com.brokerpublishmodule.brokers.ActiveMQ.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
@Data
@Accessors(chain = true)
public class User implements Serializable {

  private String userId;
  private String userName;

  @Override
  public String toString() {
    return "User{" +
        "userId='" + userId + '\'' +
        ", userName='" + userName + '\'' +
        '}';
  }
}