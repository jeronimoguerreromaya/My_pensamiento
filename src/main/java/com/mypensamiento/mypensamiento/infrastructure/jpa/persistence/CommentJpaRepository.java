package com.mypensamiento.mypensamiento.infrastructure.jpa.persistence;

import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
}