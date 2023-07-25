package vn.iodev.iosecurity.scheduler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
// import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.iosecurity.model.MailQueue;
import vn.iodev.iosecurity.model.QueueStatus;
import vn.iodev.iosecurity.repository.MailQueueRepository;
import vn.iodev.iosecurity.service.EmailService;

@Component
@Slf4j
public class MailTaskScheduler {
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private CronTrigger cronTrigger;

    // @Autowired
    // private PeriodicTrigger periodicTrigger;

    @Autowired
    EmailService emailService;

    @Autowired
    MailQueueRepository mailQueueRepository;
    
    @PostConstruct
    public void scheduleRunnableWithCronTrigger() {
        // taskScheduler.schedule(new RunnableTask("Current Date"), new Date());
        // taskScheduler.scheduleWithFixedDelay(new RunnableTask("Fixed 1 second Delay"), 1000);
        // taskScheduler.scheduleWithFixedDelay(new RunnableTask("Current Date Fixed 1 second Delay"), new Date(), 1000);
        // taskScheduler.scheduleAtFixedRate(new RunnableTask("Fixed Rate of 2 seconds"), new Date(), 2000);
        // taskScheduler.scheduleAtFixedRate(new RunnableTask("Fixed Rate of 2 seconds"), 2000);
        taskScheduler.schedule(new RunnableTask("cronjob"), cronTrigger);
        // taskScheduler.schedule(new RunnableTask("Periodic Trigger"), periodicTrigger);
    }

    class RunnableTask implements Runnable {

        private String message;

        public RunnableTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            log.info("Scanning mail queue " + message + " on thread " + Thread.currentThread().getName());
            List<Integer> statuses = new ArrayList<>();
            statuses.add(QueueStatus.WAITED);
            statuses.add(QueueStatus.SEND_FAILED);
            Pageable pageable = PageRequest.of(0, 10);

            List<MailQueue> lstQueues = mailQueueRepository.findByStatusInOrderByRetry(statuses, pageable);
            for (MailQueue mailQueue : lstQueues) {
                try {
                    mailQueue.setStatus(QueueStatus.SENDING);
                    mailQueueRepository.save(mailQueue);
                    emailService.sendHtmlMailQueue(mailQueue);
                    mailQueue.setStatus(QueueStatus.SEND_SUCCESS);
                    mailQueueRepository.save(mailQueue);
                }
                catch (Exception e) {
                    log.debug("IN MAIL SCHEDULER", e);
                    mailQueue.setStatus(QueueStatus.SEND_FAILED);
                    mailQueue.setRetry(mailQueue.getRetry() + 1);
                    mailQueueRepository.save(mailQueue);
                }
            }
        }
    }
}
