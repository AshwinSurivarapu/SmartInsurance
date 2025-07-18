package com.smartinsure.config.jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretGenerator {

        public static void main(String[] args) {
            int keyLength = 64; // Generates 512-bit key (HS512)
            byte[] secret = new byte[keyLength];
            new SecureRandom().nextBytes(secret);

            String base64Secret = Base64.getEncoder().encodeToString(secret);
            System.out.println("Generated JWT Secret:\n");
            System.out.println(base64Secret);
        }
    }

