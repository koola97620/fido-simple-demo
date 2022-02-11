package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.rpserver.domain.Rp;
import com.example.fidosimpledemo.rpserver.domain.RpRepository;
import com.example.fidosimpledemo.rpserver.exception.NotFoundRpHostNameException;
import org.springframework.stereotype.Service;

@Service
public class RpService {

    private final NameSplitter nameSplitter;
    private final RpRepository rpRepository;

    public RpService(NameSplitter nameSplitter, RpRepository rpRepository) {
        this.nameSplitter = nameSplitter;
        this.rpRepository = rpRepository;
    }

    public Rp getRpEntity(String host) {
        String hostName = nameSplitter.split(host);
        return rpRepository.findRpEntityByName(hostName)
                .orElseThrow(NotFoundRpHostNameException::new);
    }

}
