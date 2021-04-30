import React, { setState } from "react";
import "./App.css";
import TestComponent from "./Components/TestComponent/TestComponent";
import Register from "./Components/Register/Register";
import User from "./Components/User/User";
import { Client } from "@stomp/stompjs";
import createApiClient from "./ApiClient";

const api = createApiClient();
const SOCKET_URL = "ws://localhost:8080/ws-message";

class App extends React.Component {
  constructor() {
    super();
    this.state = {
      connID: "connID",
      response: {
        isErr: false,
        message: "init",
        returnObject: {},
      },
      clientConnection: "",
    };
  }

  // const [connID, setConnID] = useState("");
  // const [response, setResponse] = useState({
  //   isErr: false,
  //   message: "init",
  //   returnObject: {},
  // });
  // const [clientConnection, setClientConnection] = useState([]);

  submitRegisterHandler = (regData) => {
    console.log(regData);
  };

  async componentDidMount() {
    let currentComponent = this;

    let onConnected = () => {
      console.log("Connected!!");
      console.log("--- check subscribe: " + `/topic/${this.state.connID}`);
      client.subscribe(`/topic/${this.state.connID}`, function (msg) {
        if (msg.body) {
          var jsonBody = JSON.parse(msg.body);
          if (jsonBody.message) {
            // setResponse(jsonBody);
            currentComponent.setState({
              response: jsonBody,
            });
            console.log(jsonBody);
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

    const connectionRespone = await api.connectSystem();
    console.log(connectionRespone);
    if (!connectionRespone) console.log("Error response is null!!!");
    // setResponse(connectionRespone);

    this.setState(
      (prevState) => ({
        response: connectionRespone,
        connID: connectionRespone.returnObject.connID,
      }),
      () => {
        console.log(connectionRespone.returnObject.connID);
        console.log(this.state.connID);
        client.activate();
        this.setState({
          clientConnection: client,
        });
      }
    );

    // setConnID((prevState) => {
    //   connID: connectionRespone.returnObject.connID;
    // });
    // console.log(connectionRespone.returnObject.connID);
    // console.log(connID);

    // setClientConnection(client);
  }

  render() {
    return (
      <div className="App">
        <h1>~ Trading System ~</h1>
        <Register
          onSubmitRegister={this.submitRegisterHandler}
          connID={this.state.connID}
          clientConnection={this.state.clientConnection}
          response={this.state.response}
        />
        <div>
          <p>
            response: (isErr={this.state.response.isErr ? "true" : "false"},
            msg:
            {this.state.response.message}){" "}
          </p>
          <p>connID: {this.state.connID}</p>
        </div>
      </div>
    );
  }
}

export default App;
