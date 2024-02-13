package tse.api.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static tse.api.demo.utils.constants.ExCon.PWD;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    public User() {}

    public User(String username) {
        this.username = username;
        this.password = PWD;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}