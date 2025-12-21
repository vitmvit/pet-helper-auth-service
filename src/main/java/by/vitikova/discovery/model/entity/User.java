package by.vitikova.discovery.model.entity;

import by.vitikova.discovery.constant.RoleName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String id;
    private String login;
    private String password;
    private RoleName role;
    private LocalDateTime createDate;
    private LocalDateTime lastVisit;
}