package com.andrzej.RESTfullPokemonAPI.websocket;

import com.andrzej.RESTfullPokemonAPI.jwt.JwtAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Map;

@Component
public class MyHandshakeInterceptor implements HandshakeInterceptor {
    private final SecretKey secretKey;

    @Autowired
    public MyHandshakeInterceptor(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler handler,
                                   Map<String, Object> attributes) throws Exception {
        Map<String, String[]> parameterMap = ((ServletServerHttpRequest) request).getServletRequest().getParameterMap();
        if (parameterMap.containsKey("token")) {
            String tokenFromParam = Arrays.toString(parameterMap.get("token"));
            if (tokenFromParam.length() <= 6) {
                throw new IllegalStateException(String.format("Token %s cannot be trusted", tokenFromParam));
            }
            String token = tokenFromParam.substring(8);
            JwtAuthenticationUtils.setAuthenticationBasedOnToken(token, secretKey);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler handler,
                               Exception e) {

    }
}
