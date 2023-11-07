package com.example.pricetracker.api.headers;

import com.example.pricetracker.dto.response.AuthTokenResponse;

public class AuthorizationUtils {

    private static AuthTokenResponse authorization = null;

    public static String getAuthTokenFormatted() throws AuthTokenException {
        if(authorization == null || authorization.getAccessToken() == null || authorization.getAccessToken().isEmpty()) {
            throw new AuthTokenException("Authorization token is not set!");
        }
        return String.format("Bearer %s", authorization.getAccessToken());
    }

    public static void setAuthorizationData(AuthTokenResponse authTokenResponse) throws AuthTokenException {
        if(authTokenResponse.getAccessToken() == null || authTokenResponse.getAccessToken().isEmpty()) {
            throw new AuthTokenException("Authorization response does not contain proper token!");
        }
        authorization = authTokenResponse;
    }
}
