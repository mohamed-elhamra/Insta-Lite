package com.instalite.api.entities;

import com.instalite.api.commons.utils.enums.EVisibility;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "videos")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String publicId;

    @Column(length = 60, nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EVisibility visibility;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
