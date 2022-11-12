/*package com.jjara.cloud.gateway.security.filters.keycloak;

import org.apache.commons.codec.binary.Base64;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyCloackBasicAuthFilter extends GenericFilterBean {

    @Autowired private KeycloakSpringBootProperties props;

    private final static String BASIC =  "Basic ";
    private final static String BEARER = "Bearer ";
    private final static String SECRET = "secret";
    private final static String SEPARATOR = ":";
    private final static int USER_IDX = 0;
    private final static int PASSWORD_IDX = 1;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String authorization = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authorization)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (!StringUtils.isEmpty(authorization) && authorization.contains(BASIC)) {
            String authorizationData = new String(new Base64().decode(authorization.replace(BASIC, "").getBytes()));
            String user = authorizationData.split(SEPARATOR)[USER_IDX];
            String password = authorizationData.split(SEPARATOR)[PASSWORD_IDX];
            Keycloak keycloak = getKeyCloak(user, password);
            HttpRequestWrapper wrapper = new HttpRequestWrapper(httpRequest);
            String token = keycloak.tokenManager().getAccessTokenString();

            wrapper.addHeader(HttpHeaders.AUTHORIZATION, BEARER + token);
            servletRequest = wrapper;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Keycloak getKeyCloak(String user, String password) {
        return KeycloakBuilder.builder()
            .serverUrl(props.getAuthServerUrl())
            .realm(props.getRealm())
            .grantType(OAuth2Constants.PASSWORD)
            .clientId(props.getResource())
            .clientSecret((String) props.getCredentials().get(SECRET))
            .username(user)
            .password(password)
            .build();
    }

    public class HttpRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String> headers = new HashMap<>();

        public HttpRequestWrapper(HttpServletRequest httpRequest) {
            super(httpRequest);
        }

        @Override
        public String getHeader(String name) {
            String value = getRequest().getParameter(name);
            if (value == null) {
                value = headers.get(name);
            }
            if (value != null) {
                return value;
            }
            return super.getHeader(name);
        }

        public void addHeader(String header, String value) {
            headers.put(header, value);
        }
    }
}
*/