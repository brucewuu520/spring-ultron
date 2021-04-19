package org.springultron.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 信任所有 host name
 */
public class TrustAllHostNames implements HostnameVerifier {
    public static final TrustAllHostNames INSTANCE = new TrustAllHostNames();

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
