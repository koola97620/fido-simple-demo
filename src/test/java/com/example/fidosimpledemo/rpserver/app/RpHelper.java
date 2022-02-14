package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.fidoserver.domain.RpRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class RpHelper {

    private final RpRepository rpRepository;

    public RpHelper(RpRepository rpRepository) {
        this.rpRepository = rpRepository;
    }

    public RpEntity save(RpEntity rpEntity) {
        return rpRepository.save(rpEntity);
    }

}
