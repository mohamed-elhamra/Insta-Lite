package com.instalite.api.repositories;

import com.instalite.api.entities.ImageEntity;
import com.instalite.api.entities.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {
    Optional<VideoEntity> findByPublicId(String videoId);
    Optional<VideoEntity> findByTitle(String videoTitle);
}
