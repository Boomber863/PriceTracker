package com.example.pricetracker.api.headers;

import com.example.pricetracker.dto.response.AuthTokenResponse;

public class AuthorizationProvider {

    private AuthTokenResponse authorization;
    private static AuthorizationProvider instance;

    private AuthorizationProvider() {
        this.authorization = null;
    }

    public static AuthorizationProvider getInstance() {
        if(instance == null) {
            instance = new AuthorizationProvider();
        }
        return instance;
    }

    public String getAuthTokenFormatted() {
        if(authorization == null) {
            return "Bearer: ";
        }
        return String.format("Bearer %s", authorization.getAccessToken());
    }

    public void setAuthorizationData(AuthTokenResponse authTokenResponse) throws AuthTokenException {
        if (authTokenResponse.getAccessToken() == null || authTokenResponse.getAccessToken().isEmpty()) {
            throw new AuthTokenException("Authorization response does not contain proper token!");
        }
        authorization = authTokenResponse;
    }

    public void setAuthorizationData(String token) throws AuthTokenException {
        if (token == null || token.isEmpty()) {
            throw new AuthTokenException("Token is either null or empty!");
        }
        authorization = new AuthTokenResponse(token);
    }
}
