package com.tinuproject.tinu.domain.chat.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class ChatConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(stompEndpointRegistry: StompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/ws-stomp")
            .withSockJS();
    }

    override fun configureMessageBroker(messageBrokerRegistry: MessageBrokerRegistry) {
        messageBrokerRegistry.setApplicationDestinationPrefixes("/send"); // 도착 경로에 대한 prefix, /send/topic, /send/room 와 같이 된다.
        messageBrokerRegistry.enableSimpleBroker("/topic", "/room"); // 메시지 브로커. topic은 해당 토픽을 구독하는 모두에게, room은 해당 룸을 구독한 사람 1명에게 보내는 경우 사용 예정.
    }
}