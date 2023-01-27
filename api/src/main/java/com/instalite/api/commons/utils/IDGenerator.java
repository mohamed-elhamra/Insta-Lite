package com.instalite.api.commons.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class IDGenerator {

    private static final Random random = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateStringId(){
        int length = Constants.PUBLIC_ID_LENGTH;
        StringBuilder id = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            id.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new String(id);
    }

}
