package com.example.fidosimpledemo.rpserver.app;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!local & !test")
@Component
public class NonLocalHostNameSplitter implements NameSplitter {
    @Override
    public String split(String hostName) {
        // do something
        return hostName;
    }
}
