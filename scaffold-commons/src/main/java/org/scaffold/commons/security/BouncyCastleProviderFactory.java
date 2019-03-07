package org.scaffold.commons.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BouncyCastleProviderFactory {

    private static BouncyCastleProvider bcp = new BouncyCastleProvider();

    public static BouncyCastleProvider getInstance() {
        return bcp;
    }

}
