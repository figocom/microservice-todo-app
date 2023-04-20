package com.example.todoappmicroserviceuserapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthUser extends Auditable {
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "Username is mandatory")
    @Column(nullable = false, unique = true,updatable = false)
    private String username;
    @JsonIgnore
    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated( EnumType.STRING)
    @Builder.Default
    private Status status= Status.ACTIVE;
    public enum Status {
        ACTIVE, BLOCKED
    }
}
