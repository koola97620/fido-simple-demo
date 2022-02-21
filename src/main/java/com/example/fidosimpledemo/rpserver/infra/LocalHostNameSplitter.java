package com.example.fidosimpledemo.rpserver.infra;

import com.example.fidosimpledemo.rpserver.domain.NameSplitter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"local","test"})
@Component
public class LocalHostNameSplitter implements NameSplitter {
    private static final int FIRST_INDEX = 0;

    @Override
    public String split(String hostName) {
        String[] split = hostName.split(":");
        return split[FIRST_INDEX];
    }
}
