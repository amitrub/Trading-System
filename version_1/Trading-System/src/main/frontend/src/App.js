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
import Store from "./Components/MainPage/Store";
import Stores from "./Components/MainPage/Stores";

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
      stores: [],
      products: [],
    };
  }

  loadStores = () => {
    const storeResponse = this.state.response.returnObject;
    this.setState({
      stores: storeResponse.stores,
    });
  };

  loadProducts = () => {
    const productResponse = this.state.response.returnObject;
    console.log(productResponse);
    const products = productResponse.products;
    console.log(products);
    this.setState({
      products: products,
    });
  };

  registerHandler = (name, pass) => {
    console.log("RegisterHandler:" + name);
    const currentComponent = this;
    const registerResponse = this.state.response;

    if (registerResponse.isErr) {
      console.log(registerResponse.message);
    } else {
      const oldConnID = this.state.connID;
      this.setState(
        (prevState) => ({
          userID: registerResponse.returnObject.userID,
          connID: registerResponse.returnObject.connID,
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
    console.log("End Register Handler! connID: " + this.state.connID);
  };

  loginHandler = (name, pass) => {
    console.log("LoginHandler:" + name);
    const currentComponent = this;
    const loginResponse = this.state.response;

    if (loginResponse.isErr) {
      console.log(loginResponse.message);
    } else {
      const oldConnID = this.state.connID;
      this.setState(
        (prevState) => ({
          username: name,
          pass: pass,
          userID: loginResponse.returnObject.userID,
          connID: loginResponse.returnObject.connID,
          whoAreUser: {
            guest: false,
            subscriber: true,
            manager: false,
            owner: false,
          },
          // whoAreUser: loginResponse.returnObject.whoAreUser
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
    console.log("End Login Handler! connID: " + this.state.connID);
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
            currentComponent.setState(
              {
                response: jsonBody,
              },
              () => {
                //get All Stores
                if (jsonBody.returnObject.stores) {
                  this.loadStores();
                }

                //get All products in store
                if (jsonBody.returnObject.products) {
                  this.loadProducts();
                }
              }
            );
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
        <MainPage
          username={this.state.username}
          loadSys={this.loadStores}
          connID={this.state.connID}
          clientConnection={this.state.clientConnection}
        />
        <Stores
          loadSys={this.loadStores}
          connID={this.state.connID}
          clientConnection={this.state.clientConnection}
          stores={this.state.stores}
          products={this.state.products}
        />
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
              onSubmitLogin={this.loginHandler}
              connID={this.state.connID}
              clientConnection={this.state.clientConnection}
              response={this.state.response}
            />
          </div>
        </section>
        <Reccomaditions />
        <Programers />
        {/* <div>
          <p>
            response: (isErr={this.state.response.isErr ? "true" : "false"},
            msg:
            {this.state.response.message}){" "}
          </p>
          <p>connID: {this.state.connID}</p>
        </div> */}
      </div>
    );
  }
}

export default App;
