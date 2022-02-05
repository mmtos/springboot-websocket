const _uri = "ws://localhost:8080/ws/stomp/echo";
const webstomp = window.Stomp;
const websocket = {
    _socket: null,
    stomp: null,
    connect : function (){
        const ws = new WebSocket(_uri);
        const onMessage = (msg) => {
            console.log("메시지 받았습니다.",msg)
        }
        const onOpen = () => {
            console.log("ws 통신 시작")
        }
        const closeSocket =  this.close.bind(this);
        const onClose = function(){
            //서버가 종료된 경우 처리.
            closeSocket();
            console.log("ws 통신 종료")
        }
        this._socket = ws;

        ws.onmessage = onMessage;
        ws.onopen = onOpen;
        ws.onclose = onClose;
        const stomp =  webstomp.over(ws);
        stomp.connect({}, function(frame) {
            console.log("STOMP Connection")
        });
        this.stomp = stomp;
    },
    close : function (){
        if(this._socket) {
            this._socket.close();
            this._socket = null;
        }
    }
}
export default websocket;
