package com.example.demo.infra.repository.notification.jpa;

import com.example.demo.infra.repository.notification.jpa.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

}






