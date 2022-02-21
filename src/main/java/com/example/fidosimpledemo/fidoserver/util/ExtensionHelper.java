package com.example.fidosimpledemo.fidoserver.util;

import com.example.fidosimpledemo.fidoserver.domain.UserKey;
import com.example.fidosimpledemo.rpserver.dto.AuthenticationExtensionsClientOutputs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtensionHelper {
    public static UserKey createUserKeyWithExtensions(UserKey.UserKeyBuilder userKeyBuilder, AuthenticationExtensionsClientOutputs clientExtensions, AuthenticatorExtension authenticatorExtensions) {
        // verify extension
        log.info("Verify extension");

        // check authenticator extensions
        log.info("Check authenticator extension");

//        if (authenticatorExtensions != null) {
//            log.debug("Extensions: {}", authenticatorExtensions);
//            if (authenticatorExtensions.getCredProtect() != null) {
//                log.info("Handle credProtect extension");
//                userKeyBuilder.credProtect(authenticatorExtensions.getCredProtect().getValue());
//            }
//        }

        // check client extensions
        log.info("Check client extension");
        if (clientExtensions != null) {
            log.info("Client extension output: {}", clientExtensions);
            if (clientExtensions.getCredProps() != null) {
                userKeyBuilder.rk(clientExtensions.getCredProps().getRk());
            }
        }

        return userKeyBuilder.build();
    }
}
