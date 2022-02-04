# 20220204 Websocket

# 설계
- 데이터 받는 EndPoint
- 받은 저장할 자료구조 
- 버스 노선별로 pub/sub 구조 추가
- 클라이언트 단에서 WebSocket API나 Sock, Stomp library를 사용하여 서버와 연결 
## websocket 배경지식
- handshake request를 먼저 수행한다.
  - Upgrade: websocket
  - Connection: Upgrade

- SockJS(Websocket emulation)
  - SockJS는 어플리케이션이 WebSocket API를 사용하도록 허용하지만 브라우저에서 WebSocket을 지원하지 않는 경우에 대안으로 어플리케이션의 코드를 변경할 필요 없이 런타임에 필요할 때 대체하는 것
  - streaming, polling
- STOMP (Simple Text Oriented Messaging Protocol) 
  - 웹소켓 기반 프로토콜로, 일반 텍스트 메시지에 형식을 부여하고 pub/sub 구조로 동작이 가능하게 하는 역할. 
  - @EnableWebSocketMessageBroker를 통해 설정 가능
  - https://dev-gorany.tistory.com/235
- 참고 URL 
  - https://dev-gorany.tistory.com/212
  - 
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
5. WebSocketClient
   - 클라이언트.
6. WebSocketHandlerRegistration
  - WebSocketHandler, HandshakeHandler, HandshakeInterceptor을 등록하는 역할
  - 
## EnableWebSocketMessageBroker
  - STOMP 적용