import websocket from "/websocket.js";
export function start(){
    websocket.connect();
}
export function close(){
    websocket.close();
}
