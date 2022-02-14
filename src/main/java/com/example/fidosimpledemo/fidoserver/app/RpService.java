package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.fidoserver.domain.RpRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RpService {

    private final RpRepository rpRepository;

    public RpService(RpRepository rpRepository) {
        this.rpRepository = rpRepository;
    }

    public boolean containsRp(String rpName) {
        return rpRepository.existsRpById(rpName);
    }

    public void findByName(String rpName) {
        Optional<RpEntity> optionalRp = rpRepository.findById(rpName);
    }
}
