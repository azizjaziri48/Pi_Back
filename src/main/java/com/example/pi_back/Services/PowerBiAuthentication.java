package com.example.pi_back.Services;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PowerBiAuthentication {
    private static final String AUTHORITY_URL = "https://login.microsoftonline.com/common/";
    private static final String CLIENT_ID = "cdb64f5d-936c-47c4-b489-839b8e9c7e07";
    private static final String CLIENT_SECRET = "jHy8Q~DZec-K43sdooWSfTQ.rSPn_v6e7LW4HbSS";
    private static final String RESOURCE = "https://analysis.windows.net/powerbi/api";

    public static String getAccessToken() throws Exception {
        AuthenticationContext context = new AuthenticationContext(AUTHORITY_URL, false, Executors.newFixedThreadPool(1));
        ClientCredential credential = new ClientCredential(CLIENT_ID, CLIENT_SECRET);
        Future<AuthenticationResult> future = context.acquireToken(RESOURCE, credential, null);
        AuthenticationResult result = future.get();
        return result.getAccessToken();
    }
}
