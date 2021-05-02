import React, { Fragment } from "react";
import "./App.css";
import "./Design/grid.css";
import "./Design/style.css";
import Register from "./Components/Register/Register";
import { Client } from "@stomp/stompjs";
import MainPage from "./Components/MainPage/MainPage";
import createApiClient from "./ApiClient";
import Recommendations from "./Components/MainPage/Recommendations";
import Programers from "./Components/MainPage/Programers";
import Login from "./Components/Login/Login";
import Stores from "./Components/Stores/Stores";
import Navbar from "./Components/Navbar/Navbar";
import "./Components/Navbar/Navbar.css";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import DownPage from "./Components/MainPage/DownPage";
import StoresOwner from "./Components/StoresOwner/StoresOwner";
import { Typography } from "@material-ui/core";
import LoginHttp from "./Components/LoginHttp/LoginHttp";

const api = createApiClient();
const SOCKET_URL = "ws://localhost:8080/ws-message";

class App extends React.Component {
  constructor() {
    super();
    this.state = {
      clientConnection: null,
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
        manager: false,
        owner: false,
        founder: false,
        admin: false,
      },
      stores: [],
      founderStores: [],
      ownerStores: [],
      managerStores: [],
      products: [],
      refresh: false,
    };
  }

  refresHandler = () => {
    this.setState((prevState) => ({
      refresh: !prevState.refresh,
    }));
  };

  closeModal = () => {
    this.setState({ openModal: false });
  };

  loadStores = () => {
    const storeResponse = this.state.response.returnObject;
    this.setState({
      stores: storeResponse.stores,
    });
  };

  loadFounderStores = () => {
    const storeResponse = this.state.response.returnObject;
    this.setState({
      founderStores: storeResponse.founderStores,
    });
  };

  loadOwnerStores = () => {
    const storeResponse = this.state.response.returnObject;
    this.setState({
      ownerStores: storeResponse.ownerStores,
    });
  };

  loadManagerStores = () => {
    const storeResponse = this.state.response.returnObject;
    this.setState({
      managerStores: storeResponse.managerStores,
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
      // const oldConnID = this.state.connID;
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

  loginHandler = (name, pass, res) => {
    console.log("LoginHandler:" + name);
    console.log("LoginHandler:" + pass);
    console.log(res);
    const currentComponent = this;
    const loginResponse = res;

    if (loginResponse.isErr) {
      console.log(loginResponse.message);
    } else {
      // const oldConnID = this.state.connID;
      console.log("7777777777777");
      this.setState(
        (prevState) => ({
          username: name,
          pass: pass,
          userID: loginResponse.returnObject.userID,
          connID: loginResponse.returnObject.connID,
          whoAreUser: {
            guest: false,
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

                  //get All Stores
                  if (jsonBody.returnObject.founder) {
                    const founder = jsonBody.returnObject.founder;
                    currentComponent.setState({
                      whoAreUser: { founder: founder },
                    });
                  }
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
    const { clientConnection } = this.state;

    let onConnected = () => {
      console.log("Connected!!");
      // console.log("--- check subscribe: " + `/topic/${this.state.connID}`);
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

                //get All Stores
                if (jsonBody.returnObject.founderStores) {
                  this.loadFounderStores();
                }

                //get All Stores
                if (jsonBody.returnObject.ownerStores) {
                  this.loadOwnerStores();
                }

                //get All Stores
                if (jsonBody.returnObject.managerStores) {
                  this.loadManagerStores();
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
        <BrowserRouter>
          <div className="AppTry">
            <Navbar />
            <div>{this.renderContent()}</div>
          </div>
        </BrowserRouter>
      </div>
    );
  }

  renderContent = () => {
    const {
      clientConnection,
      response,
      username,
      pass,
      userID,
      connID,
      whoAreUser,
      stores,
      founderStores,
      products,
    } = this.state;
    return !clientConnection ? (
      <Fragment>
        <Typography>Connecting To System...</Typography>
      </Fragment>
    ) : (
      <Fragment>
        {userID != -1 ? (
          <StoresOwner connID={connID} userID={userID}></StoresOwner>
        ) : (
          this.guestContent()
        )}
        <Switch>
          <Route path="/app">
            <MainPage />
          </Route>
        </Switch>
      </Fragment>
    );
  };

  guestContent = () => {
    return (
      <Fragment>
        <MainPage username={this.state.username} />
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
              refresh={this.refreshandler}
              connID={this.state.connID}
              clientConnection={this.state.clientConnection}
              response={this.state.response}
            />
          </div>
          <div className="col span-1-of-2 box">
            <LoginHttp
              onSubmitLogin={this.loginHandler}
              connID={this.state.connID}
              clientConnection={this.state.clientConnection}
              response={this.state.response}
              refresHandler={this.refresHandler}
            />
          </div>
        </section>
        <Recommendations />
        <Programers />
        <DownPage />
      </Fragment>
    );
  };
}

export default App;
