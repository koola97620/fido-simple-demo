package com.example.fidosimpledemo.fidoserver.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

public class CertificateUtil {
    public static List<Certificate> getCertificates(List<byte[]> certificateBytesList) throws CertificateException {
        List<Certificate> certificates = new ArrayList<>();
        for (byte[] certificateBytes : certificateBytesList) {
            certificates.add(getCertificate(certificateBytes));
        }
        return certificates;
    }

    private static Certificate getCertificate(byte[] certificateBytes) throws CertificateException {
        return getCertificate(new ByteArrayInputStream(certificateBytes));
    }

    private static Certificate getCertificate(InputStream inputStream) throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return certificateFactory.generateCertificate(inputStream);
    }

}
