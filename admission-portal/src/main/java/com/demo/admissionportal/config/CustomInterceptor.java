package com.demo.admissionportal.config;


import jakarta.annotation.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CustomInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, @Nullable Exception e) {
        if(serverHttpRequest.getHeaders() == null )
            return ;
        if(serverHttpRequest.getHeaders().get("Sec-WebSocket-Protocol") == null)
            return ;
        String protocol = (String) serverHttpRequest.getHeaders().get("Sec-WebSocket-Protocol").get(0);
        if(protocol == null)
            return ;

        serverHttpResponse.getHeaders().add("Sec-WebSocket-Protocol", protocol);

    }

}