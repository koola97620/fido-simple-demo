package com.example.fidosimpledemo.fidoserver.query;

import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.fidoserver.domain.RpRepository;
import com.example.fidosimpledemo.rpserver.app.NameSplitter;
import com.example.fidosimpledemo.rpserver.exception.NotFoundRpHostNameException;
import org.springframework.stereotype.Service;

@Service
public class RpQueryService {

    private final NameSplitter nameSplitter;
    private final RpRepository rpRepository;

    public RpQueryService(NameSplitter nameSplitter, RpRepository rpRepository) {
        this.nameSplitter = nameSplitter;
        this.rpRepository = rpRepository;
    }

    public RpEntity getRpEntity(String host) {
        String hostName = nameSplitter.split(host);
        return rpRepository.findRpEntityById(hostName)
                .orElseThrow(NotFoundRpHostNameException::new);
    }

}
