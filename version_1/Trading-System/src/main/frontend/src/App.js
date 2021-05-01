import React from "react";
import "./App.css";
import "./Components/MainPage/MainPageDesign/style.css";
import "./Components/MainPage/MainPageDesign/grid.css";
import Register from "./Components/MainPage/Register";
import { Client } from "@stomp/stompjs";
import MainPage from "./Components/MainPage/MainPage";
import createApiClient from "./ApiClient";
import Reccomaditions from "./Components/MainPage/Reccomaditions";
import Programers from "./Components/MainPage/Programers";
import Login from "./Components/MainPage/Login";

const api = createApiClient();
const SOCKET_URL = "ws://localhost:8080/ws-message";

class App extends React.Component {
  constructor() {
    super();
    this.state = {
      clientConnection: "",
      response: {
        isErr: false,
        message: "init",
        returnObject: {},
      },
      username: "guest",
      pass: "",
      userID: -1,
      connID: "connID",
      whoAreUser: {
        guest: true,
        subscriber: false,
        manager: false,
        owner: false,
      },
    };
  }

  registerHandler = (name, pass) => {
    console.log(name);
    const currentComponent = this;
    const registerResponse = this.state.response;

    if (registerResponse.isErr) {
      console.log(registerResponse.message);
    } else {
      const oldConnID = this.state.connID;
      this.setState(
        (prevState) => ({
          username: name,
          pass: pass,
          userID: registerResponse.returnObject.userID,
          connID: registerResponse.connID,
        }),
        () => {
          //unsubscribe old id
          this.state.clientConnection.subscribe(
            `/topic/${this.state.connID}`,
            (msg) => {
              if (msg.body) {
                var jsonBody = JSON.parse(msg.body);
                if (jsonBody.message) {
                  currentComponent.setState({
                    response: jsonBody,
                  });
                  console.log(jsonBody);
                }
              }
            }
          );
        }
      );
    }
  };

  async componentDidMount() {
    let currentComponent = this;

    let onConnected = () => {
      console.log("Connected!!");
      console.log("--- check subscribe: " + `/topic/${this.state.connID}`);
      client.subscribe(`/topic/${this.state.connID}`, (msg) => {
        if (msg.body) {
          var jsonBody = JSON.parse(msg.body);
          if (jsonBody.message) {
            currentComponent.setState({
              response: jsonBody,
            });
            console.log(jsonBody);
          }
        }
      });
      currentComponent.setState({
        clientConnection: client,
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

    this.setState(
      (prevState) => ({
        response: connectionRespone,
        connID: connectionRespone.returnObject.connID,
      }),
      () => {
        console.log(connectionRespone.returnObject.connID);
        console.log(this.state.connID);
        client.activate();
        // this.setState({
        //   clientConnection: client,
        // });
      }
    );
  }

  render() {
    return (
      <div className="App">
        <MainPage />
        <section className="row">
          <div className="col span-1-of-2 box">
            <Register
              onSubmitRegister={this.registerHandler}
              connID={this.state.connID}
              clientConnection={this.state.clientConnection}
              response={this.state.response}
            />
          </div>
          <div className="col span-1-of-2 box">
            <Login
              onSubmitRegister={this.registerHandler}
              connID={this.state.connID}
              clientConnection={this.state.clientConnection}
              response={this.state.response}
            />
          </div>
        </section>
        <Reccomaditions />
        <Programers />
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
