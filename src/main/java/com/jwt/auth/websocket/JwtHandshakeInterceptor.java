package com.jwt.auth.websocket;

import com.jwt.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    JwtService jwtService;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        List<String> token =request.getHeaders().get("token");
        boolean checkValid = false;
        assert token != null;
        if (!token.isEmpty()){
            System.out.println(token.get(0));
            String extractedToken = token.get(0);
            String username = jwtService.extractUsername(token.get(0));
            System.out.println(username);
            checkValid = jwtService.isWSValidToken(extractedToken, username);
            System.out.println(checkValid);
        }
        return checkValid;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
