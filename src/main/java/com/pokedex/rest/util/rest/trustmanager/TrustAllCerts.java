package com.pokedex.rest.util.rest.trustmanager;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;

public abstract class TrustAllCerts {
    private TrustAllCerts(){
        // Hide constructor so it's not called because of future inheritance
    }

    public static TrustManager[] getTrustManager() {
        return new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        // checks no certificates, accepts everything
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        // checks no server trusted, accepts everything
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
    }

}
