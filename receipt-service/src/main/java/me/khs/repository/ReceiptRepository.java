package me.khs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.khs.entity.ReceiptEntity;

public interface ReceiptRepository extends JpaRepository <ReceiptEntity, Long>{

}
