package com._3line.gravity.core.verification.service;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.Enumerated;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationState {

    public Set<String> usersInVerificationMode = Collections.synchronizedSet(new HashSet<String>());

    public boolean isInVerificationMode(String username) {
        return usersInVerificationMode.contains(username.toUpperCase());
    }

    public void addToMode(String username){
        usersInVerificationMode.add(username.toUpperCase());
    }

    public void  removeFromMode(String username){
        usersInVerificationMode.remove(username.toUpperCase());
    }
}
