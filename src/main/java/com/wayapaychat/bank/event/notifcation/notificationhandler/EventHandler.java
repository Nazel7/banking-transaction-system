package com.wayapaychat.bank.event.notifcation.notificationhandler;

import com.wayapaychat.bank.entity.DataBody;
import com.wayapaychat.bank.repository.DataBodyRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventHandler {

   private final DataBodyRepo message;

    @Async
    @EventListener
    public void publishMessage(DataBodyEvent dataBodyEvent){

        final DataBody dataBody= dataBodyEvent.getDataBody();
        DataBody savedDataBody= message.save(dataBody);

        log.info("::: Message sent with payload: [{}] :::", savedDataBody);

    }
}
