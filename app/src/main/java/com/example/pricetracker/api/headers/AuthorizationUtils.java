package com.example.pricetracker.api.headers;

import com.example.pricetracker.dto.response.AuthTokenResponse;

public class AuthorizationUtils {

    private static AuthTokenResponse authentication = null;

    public static String getAuthTokenFormatted() throws AuthTokenException {
        if(authentication == null || authentication.getAccessToken() == null || authentication.getAccessToken().isEmpty()) {
            throw new AuthTokenException("Authorization token is not set!");
        }
        return String.format("Bearer %s", authentication.getAccessToken());
    }

    public static void setAuthorizationData(AuthTokenResponse authTokenResponse) throws AuthTokenException {
        if(authTokenResponse.getAccessToken() == null || authTokenResponse.getAccessToken().isEmpty()) {
            throw new AuthTokenException("Authorization response does not contain proper token!");
        }
        authentication = authTokenResponse;
    }
}
