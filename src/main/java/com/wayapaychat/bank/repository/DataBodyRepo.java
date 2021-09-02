package com.wayapaychat.bank.repository;

import com.wayapaychat.bank.entity.models.NotificationLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataBodyRepo extends JpaRepository<NotificationLog, Long> {
}
