package com.example.fidosimpledemo.fidoserver.app;

import java.util.List;

public interface VendorSpecificMetadataService {
    List<String> getAttestationRootCertificates(AuthenticatorVendor vendor);
}
