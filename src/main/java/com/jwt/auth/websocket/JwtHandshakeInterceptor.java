package com.jwt.auth.websocket;

import com.jwt.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    JwtService jwtService;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //List<String> token =request.getHeaders().get("token");

        // Extract path variable
        String tokenFromPath = extractTokenFromPath(request.getURI());
        System.out.println(tokenFromPath);
        boolean checkValid = false;
        assert tokenFromPath != null;
        if (!tokenFromPath.isEmpty()){
            //System.out.println(token.get(0));
            String username = jwtService.extractUsername(tokenFromPath);
            System.out.println(username);
            checkValid = jwtService.isWSValidToken(tokenFromPath, username);
            System.out.println(checkValid);
        }
        return checkValid;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
    private String extractTokenFromPath(URI uri) {
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                if (idx > 0 && "token".equals(pair.substring(0, idx))) {
                    return URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }
}
