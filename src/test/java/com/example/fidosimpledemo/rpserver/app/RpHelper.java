package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.rpserver.domain.Rp;
import com.example.fidosimpledemo.rpserver.domain.RpRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class RpHelper {

    private final RpRepository rpRepository;

    public RpHelper(RpRepository rpRepository) {
        this.rpRepository = rpRepository;
    }

    public Rp save(Rp rp) {
        return rpRepository.save(rp);
    }

}
