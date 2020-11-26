package com.jjara.cloud.gateway;

import org.apache.commons.codec.binary.Base64;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationFilterCustom extends GenericFilterBean {

    private KeycloakSpringBootProperties props;

    public AuthenticationFilterCustom(KeycloakSpringBootProperties props) {
        this.props = props;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String authorization = httpRequest.getHeader("authorization");
        authorization = authorization.replace("Basic ", "");

        String decodedString = new String(new Base64().decode(authorization.getBytes()));
        String user = decodedString.split(":")[0];
        String password = decodedString.split(":")[1];

        Keycloak keycloak = KeycloakBuilder.builder() //
                .serverUrl(props.getAuthServerUrl()) //
                .realm(props.getRealm()) //
                .grantType(OAuth2Constants.PASSWORD) //
                .clientId(props.getResource()) //
                .clientSecret((String) props.getCredentials().get("secret")) //
                .username(user)
                .password(password)
                .build();

        HttpRequestWrapper wrapper = new HttpRequestWrapper(httpRequest);
        wrapper.addHeader("Authorization", "Bearer " + keycloak.tokenManager().getAccessTokenString());


        filterChain.doFilter(wrapper, servletResponse);
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