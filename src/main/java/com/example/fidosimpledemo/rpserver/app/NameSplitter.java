package com.example.fidosimpledemo.rpserver.app;

import org.springframework.stereotype.Component;


public interface NameSplitter {
    String split(String hostName);
}
