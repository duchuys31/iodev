package vn.iodev.iosecurity.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iodev.iosecurity.model.MailQueue;

public interface MailQueueRepository extends JpaRepository<MailQueue, Long> {
    List<MailQueue> findByStatusInOrderByRetry(List<Integer> statuses, Pageable pageable);
}
