package com.example.socialmediarestapi.model.entity;

import com.example.socialmediarestapi.security.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * Representation of User in terms of his private details and authentication/authorization info in the database. This object should NOT be returned to a normal user by presentation layer as it contains a lot of private info. Profile object should be returned instead.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Qualifier
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;


}
