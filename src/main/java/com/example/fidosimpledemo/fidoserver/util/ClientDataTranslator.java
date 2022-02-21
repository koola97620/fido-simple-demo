package com.example.fidosimpledemo.fidoserver.util;

import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.fidoserver.domain.CollectedClientData;
import com.example.fidosimpledemo.fidoserver.exception.*;
import com.example.fidosimpledemo.common.model.TokenBinding;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

@Slf4j
public class ClientDataTranslator {

    public static byte[] handleClientDataJSON(String type, String savedChallenge, String base64UrlEncodedClientDataJSON, String origin, TokenBinding tokenBinding) {
        String clientDataJSON = new String(Base64.getUrlDecoder().decode(base64UrlEncodedClientDataJSON));
        log.info("clientDataJSON: {}", clientDataJSON);
        CollectedClientData collectedClientData;
        try {
            collectedClientData = getCollectedClientData(clientDataJSON);
        } catch (IOException e) {
            throw new FIDO2InvalidFormatClientException("Invalid format client exception: " + e.getMessage());
        }
        byte[] clientDataJSONBytes = clientDataJSON.getBytes();
        log.info("collectedClientData: {}", collectedClientData);
        if (ObjectUtils.isEmpty(collectedClientData.getType()) ||
                ObjectUtils.isEmpty(collectedClientData.getChallenge()) ||
                ObjectUtils.isEmpty(collectedClientData.getOrigin())) {
            throw new FIDO2InvalidFormatClientException("Required field missing");
        }

        // verify challenge (should be matched to challenge sent in create call)
        log.info("Verify challenge matched to challenge sent");
        if (!collectedClientData.getChallenge().equals(savedChallenge)) {
            throw new FIDO2ChallengeNotMatchedException("Challenge is not matched");
        }

        log.info("Check operation type in collectedClientData");
        if (!type.equals(collectedClientData.getType())) {
            throw new FIDO2InvalidTypeException("invalid operation type");
        }

        // verify origin
        log.info("Verify origin matched to origin in collectedClientData");
        URI originFromClientData;
        URI originFromRp;
        try {
            originFromClientData = new URI(collectedClientData.getOrigin());
            originFromRp = new URI(origin);
        } catch (URISyntaxException e) {
            throw new FIDO2InvalidOriginException("Invalid origin");
        }

        checkOrigin(originFromClientData, originFromRp);

        // verify token binding
        log.info("Verify token binding if supported");
        if (collectedClientData.getTokenBinding() != null) {
            if (collectedClientData.getTokenBinding().getStatus() == null) {
                throw new FIDO2TokenBindingException("TOKEN_BINDING_STATUS_MISSING");
            }
            if (tokenBinding != null) {
                if (collectedClientData.getTokenBinding().getStatus() != tokenBinding.getStatus()) {
                    throw new FIDO2TokenBindingException("TOKEN_BINDING_INFO_NOT_MATCHED");
                } else {
                    if (collectedClientData.getTokenBinding().getId() == null ||
                            tokenBinding.getId() == null) {
                        throw new FIDO2TokenBindingException("TOKEN_BINDING_INFO_NOT_MATCHED");
                    } else {
                        if (!tokenBinding.getId().equals(collectedClientData.getTokenBinding().getId())) {
                            throw new FIDO2TokenBindingException("TOKEN_BINDING_INFO_NOT_MATCHED");
                        }
                    }
                }
            }
        }

        // compute hash of clientDataJSON
        log.info("Compute hash of clientDataJSON");

        return Digests.sha256(clientDataJSONBytes);
    }

    private static CollectedClientData getCollectedClientData(String clientDataJSON) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(clientDataJSON, CollectedClientData.class);
    }

    private static void checkOrigin(URI originFromClientData, URI originFromRp) {
        if (!originFromRp.toString().equals(originFromClientData.toString())) {
            throw new FIDO2OriginNotMatchedException(
                    "From collected data: " + originFromClientData + ", From request param: " + originFromRp);
        }
    }
}
