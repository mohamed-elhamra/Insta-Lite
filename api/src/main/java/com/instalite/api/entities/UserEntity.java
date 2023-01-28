package com.instalite.api.entities;

import com.instalite.api.commons.utils.ERole;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String publicId;

    @Column(length = 20, nullable = false)
    private String firstName;

    @Column(length = 20, nullable = false)
    private String lastName;

    @Column(length = 20, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole role;


}
