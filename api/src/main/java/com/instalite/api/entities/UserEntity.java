package com.instalite.api.entities;

import com.instalite.api.commons.utils.enums.ERole;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


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

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<ImageEntity> image;

}
