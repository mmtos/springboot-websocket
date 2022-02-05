# 20220204 Websocket

## EchoApp 설계
- Topic은 사계절로 한다. (winter, spring, summer, autumn)
- Client는 하나 이상의 Topic을 Subscribe한다.
- Client에서 destination을 통해 특정 Topic으로 메시지를 보내면 해당 Topic을 구독하고 있는 모든 Client에게 메시지를 전달한다.
- 참고 URL 
  - https://velog.io/@cksal5911/WebSoket-stompJSReact-%EC%B1%84%ED%8C%85-1
  - https://dev-gorany.tistory.com/235

## 배경지식
- 웹소켓 Connection을 맺기 전에 Handshake Request를 먼저 수행한다.
  - handshake request의 주요 Headers
    - Upgrade: websocket
    - Connection: Upgrade

- SockJS(Websocket emulation)
  - SockJS는 어플리케이션이 WebSocket API를 사용하도록 허용하지만 브라우저에서 WebSocket을 지원하지 않는 경우에 대안으로 어플리케이션의 코드를 변경할 필요 없이 런타임에 필요할 때 대체하는 것
  - streaming, polling

- STOMP (Simple Text Oriented Messaging Protocol) 
  - 웹소켓 기반 프로토콜로, 일반 텍스트 메시지에 형식을 부여하고 pub/sub 구조로 동작이 가능하게 하는 역할. 
  - @EnableWebSocketMessageBroker를 통해 설정 가능
  - Frame이 통신의 기본 단위임

- 참고 URL 
  - https://dev-gorany.tistory.com/212

## EnableWebSocket
### 주요 Bean 및 role
1. WebSocketHandler
- WebSocketSession, WebSocketMessage 를 전달받아 websocket요청을 처리하는 역할
- Helper Class (org.springframework.web.socket.handler)
  - AbstractWebSocketHandler
  - TextWebSocketHandler
- WebSocketHandlerDecorator : 추가 기능을 위한 Decorator
  - ExceptionWebSocketHandlerDecorator
  - LoggingWebSocketHandlerDecorator
- 주요 메서드
  - afterConnectionEstablished
  - handleMessage
  - handleTransportError
  - afterConnectionClosed
  - supportsPartialMessages : partial Message를 처리할때 사용.. 기본값 false

2. WebSocketSession
   - 역할
     - session 관련 정보 취득(id, uri, handshakeheaders, some attribute, local address, remote address(client), sub-protocol, websocket extension ... etc ) 
     - 메시지 길이 설정
     - 메시지 전송
     - 해당 세션 종료 
   - 주요 메서드
     - getAttributes()
       - On the server side : the map populated initially through a "HandshakeInterceptor"
       - On the client side the map populated via "WebSocketClient" handshake methods.
     - sendMessage()
     - close()
   - WebSocketSessionDecorator
     - 세션 기능 추가를 위한 데코레이터
     - ConcurrentWebSocketSessionDecorator : guarantee only one thread can send messages at a time.

3. CloseStatus (웹소켓 종료상태코드) - 추후 조사
4. WebSocketMessage
   - Helper 클래스 
     - AbstractWebSocketMessage 
   - 주요 subClass
     - TextMessage
     - BinaryMessage
5. HandshakeInterceptor
   - 웹소켓 연결전 Handshake 과정에서 사용할 hook 
   - 주요 메서드 
     - beforeHandshake
     - afterHandshake
   - 주요 Sub Class (org.springframework.web.socket.server.support)
     - HttpSessionHandshakeInterceptor : HttpSession의 정보들을 WebSocketSession의 Attribute로 추가
     - OriginHandshakeInterceptor : SOP를 검사하기 위한 Interceptor
6. WebSocketClient
   - 클라이언트
7. WebSocketHandlerRegistration
  - WebSocketHandler, HandshakeHandler, HandshakeInterceptor을 등록하는 역할

## EnableWebSocketMessageBroker
### 주요 Bean 및 role
1. WebSocketMessageBrokerConfigurer 
  - registerStompEndpoints : STOMP 통신용 WebSocket endpoint를 설정한다. 
    - StompEndpointRegistry : 설정 클래스 
      - addEndPoint()  
      - setErrorHandler()
  - configureMessageBroker : 메시지 브로커를 설정한다. 
    - MessageBrokerRegistry : 설정 클래스
        - setPreservePublishOrder : 메시지의 발행 순서를 지켜서 클라이언트에 전달한다, 성능상 문제가 발생할 수 있다.  
        - setCacheLimit : destination 별 session 정보 cache size를 결정한다.
        - enableSimpleBroker : in memory message broker 사용
        - enableStompBrokerRelay : 서드파티 message broker 사용
  - configureWebSocketTransport : 메시지 전송관련 설정
  - configureClientInboundChannel : client -> application 채널을 설정한다. 실제 운영환경에 맞게 최적화 해주는게 좋다.
  - configureClientOutboundChannel : application -> client 채널을 설정한다. 실제 운영환경에 맞게 최적화 해주는게 좋다.
   
2. SimpleBrokerMessageHandler
   - enableSimpleBroker 적용시 사용되는 메시지 핸들러
   - 역할 1 : keeps track of subscriptions (SubscriptionRegistry)
     - DefaultSubscriptionRegistry
       - 하나의 session에서 여러개의 Topic을 subscribe 가능
       - Subscription, SessionInfo, SessionRegistry 내부클래스를 사용해서 subscriptions 정보를 관리
       - DestinationCache로 Map<String, LinkedMultiValueMap<String, String>> : destination -> [sessionId -> subscriptionId's] 를 캐시 
       - registerSubscription : sessionId, subscriptionId, destination, message
       - unregisterSubscription
   - 역할 2 : sends messages to subscribers.
     - Message Type : org.springframework.messaging.simp.SimpMessageType
     - handleMessageInternal(message)
3. 