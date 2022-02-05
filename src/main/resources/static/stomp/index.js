import websocket from "/stomp/websocket.js";

const subscribeBaseUri = "/topic/season/"
const enterUri = "/echoApp/enter"
const sendUri = "/echoApp/sendMessage"

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
    websocket.stomp.send(enterUri,{},JSON.stringify({message:"안녕하세요.",season:season}));
}
export function send(season,message){
    websocket.stomp.send(sendUri,{},JSON.stringify({message:message,season:season}));
}
