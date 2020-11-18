package com.bonelf.auth.events;

import com.bonelf.auth.domain.Resource;
import com.bonelf.auth.service.impl.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BusReceiver {

    @Autowired
    private ResourceService resourceService;

    public void handleMessage(Resource resource) {
        log.info("Received Message:<{}>", resource);
        resourceService.saveResource(resource);
    }
}
