import websocket from "/stomp/websocket.js";

const subscribeBaseUri = "/topic/season/"
const enterUri = (season) => `/echoApp/${season}/enter`
const sendUri = (season) => `/echoApp/${season}/sendMessage`
export function start(){
    websocket.connect();
}
export function close(){
    websocket.close();
}
export function subscribe(season){
    const subscribeUri = subscribeBaseUri + season;
    websocket.stomp.subscribe(subscribeUri, (receivedMessage) => {
        console.log(receivedMessage);
    });
    websocket.stomp.send(enterUri(season),{},JSON.stringify({message:"안녕하세요."}));
}
export function send(season,message){
    websocket.stomp.send(sendUri(season),{},JSON.stringify({message:message}));
}
