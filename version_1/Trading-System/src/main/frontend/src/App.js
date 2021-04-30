import React, { useState, useEffect } from "react";
import "./App.css";
import TestComponent from "./Components/TestComponent/TestComponent";
import Register from "./Components/Register/Register";
import User from "./Components/User/User";
import { Client } from "@stomp/stompjs";

const SOCKET_URL = "ws://localhost:8080/ws-message";

function App() {
  const [currUserConnID, setCurrUserConnID] = useState("");
  // const [error, setErrorState] = useState('');
  const [message, setMessage] = useState("You server message here.");
  const [response, setResponse] = useState({
    isErr: false,
    message: "init",
    returnObject: {},
  });
  const [clientConnection, setClientConnection] = useState([]);

  let onMessageReceived = (msg) => {
    setMessage(msg.message);
  };

  function submitRegisterHandler(regData) {
    console.log(regData);
  }

  function updateConnIDHandler(connID) {
    setCurrUserConnID(connID);
  }

  useEffect(() => {
    let currentComponent = this;

    let onConnected = () => {
      console.log("Connected!!");
      client.subscribe("/topic/message", function (msg) {
        if (msg.body) {
          var jsonBody = JSON.parse(msg.body);
          if (jsonBody.message) {
            setMessage(jsonBody.message);
          }
          if (!jsonBody.isErr) {
            console.log("dd");
            setResponse(jsonBody);
          }
        }
      });
    };

    let onDisconnected = () => {
      console.log("Disconnected!!");
    };

    const client = new Client({
      brokerURL: SOCKET_URL,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: onConnected,
      onDisconnect: onDisconnected,
    });

    client.activate();
    setClientConnection(client);
  }, []);

  return (
    <div className="App">
      <h1>~ Trading System ~</h1>
      {/* <TestComponent/> */}
      {/* <User onUpdateConnID = {updateConnIDHandler}/> */}
      <Register
        onSubmitRegister={submitRegisterHandler}
        connID={currUserConnID}
        clientConnection={clientConnection}
        response={response}
      />
      {/* <div>{client}</div> */}
      <div>{message}</div>
      <div>{response.message}</div>
    </div>
  );
}

export default App;
