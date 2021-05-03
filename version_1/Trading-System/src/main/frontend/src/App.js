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
import DownPage from "./Components/MainPage/downPage";
import ShoppingCart from "./Components/ShoppingCart/ShoppingCart";
import { Typography } from "@material-ui/core";
import StoresOwner from "./Components/StoresOwner/StoresOwner";

const api = createApiClient();
const SOCKET_URL = "ws://localhost:8080/ws-message";

class App extends React.Component {
  constructor() {
    super();
    this.state = {
      refresh: true,
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
      guest: true,
      manager: false,
      owner: false,
      founder: false,
      admin: false,
      founderStoresNames: [],
      ownerStoresNames: [],
      managerStoresNames: [],
      stores: [],
      products: [],
      searchedProducts: [],
      showPopup: false,
      popupHeader: "",
      popupMassge: "",
    };
  }

  onRefresh = () => {
    this.setState((prevState) => ({
      refresh: prevState.refresh ? false : true,
    }));
  };

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

  loadSearchedProducts = () => {
    const productResponse = this.state.response.returnObject;
    console.log(productResponse);
    const products = productResponse.products;
    console.log(products);
    this.setState({
      searchedProducts: products,
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
          // userID: registerResponse.returnObject.userID,
          // connID: registerResponse.returnObject.connID,
        }),
        () => {
          //unsubscribe old id
          // this.state.clientConnection.subscribe(
          //   `/topic/${this.state.connID}`,
          //   (msg) => {
          //     if (msg.body) {
          //       var jsonBody = JSON.parse(msg.body);
          //       if (jsonBody.message) {
          //         currentComponent.setState({
          //           response: jsonBody,
          //         });
          //         console.log(jsonBody);
          //       }
          //     }
          //   }
          // );
        }
      );
    }
    console.log("End Register Handler! connID: " + this.state.connID);
  };

  loginHandler = (name, pass, res) => {
    const currentComponent = this;
    const loginResponse = res;

    if (loginResponse.isErr) {
      console.log(loginResponse.message);
    } else {
      // const oldConnID = this.state.connID;
      this.setState(
        (prevState) => ({
          username: name,
          pass: pass,
          userID: loginResponse.returnObject.userID,
          connID: loginResponse.returnObject.connID,
          founderStoresNames: loginResponse.returnObject.founderStoresNames,
          ownerStoresNames: loginResponse.returnObject.ownerStoresNames,
          managerStoresNames: loginResponse.returnObject.managerStoresNames,
        }),
        () => {
          this.subscribeToTopic(this.state.clientConnection, this.state.connID);
          this.state.founderStoresNames.forEach((storeName, index) => {
            this.subscribeToTopic(this.state.clientConnection, storeName);
          });
          this.state.ownerStoresNames.forEach((storeName, index) => {
            this.subscribeToTopic(this.state.clientConnection, storeName);
          });
          this.state.managerStoresNames.forEach((storeName, index) => {
            this.subscribeToTopic(this.state.clientConnection, storeName);
          });
        }
      );
    }
    console.log("End Login Handler! connID: " + this.state.connID);
  };

  subscribeToTopic = (clientConnection, topicName) => {
    clientConnection.subscribe(`/topic/${topicName}`, (msg) => {
      if (msg.body) {
        console.log(msg);
        var jsonBody = JSON.parse(msg.body);
        if (jsonBody.message) {
          this.setState({
            response: jsonBody,
            showPopup: true,
            popupHeader: jsonBody.header,
            popupMassge: jsonBody.message,
          });
          console.log(jsonBody);
          // TODO: pop up massege
        }
      }
    });
  };

  async componentDidMount() {
    let currentComponent = this;

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
                if (jsonBody.returnObject.tag === "ShowAllStores") {
                  this.loadStores();
                }
                //get All products in store
                if (jsonBody.returnObject.tag === "ShowStoreProducts") {
                  this.loadProducts();
                }
                //Search
                if (jsonBody.returnObject.tag === "Search") {
                  this.loadSearchedProducts();
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

  tryClick = () => {
    console.log("click");
    api.getTest(this.state.clientConnection, "");
  };

  render() {
    const {
      refresh,
      clientConnection,
      response,
      username,
      pass,
      userID,
      connID,
      guest,
      manager,
      owner,
      founder,
      admin,
      stores,
      products,
      searchedProducts,
    } = this.state;
    return !this.state.clientConnection ? (
      <Fragment>
        {/* <Typography>Connecting To System...</Typography> */}
        <h3>Connecting To System...</h3>
      </Fragment>
    ) : (
      <div className="App">
        {this.guestContent()}
        <Fragment>
          <button onClick={this.tryClick}>myButton</button>
        </Fragment>
        {/* {!this.state.guest &&  */}
        {this.subscriberContent()}
        {/* {this.state.owner ? ( */}
        {this.ownerContent(connID, userID)}
        {this.endOfPage()}
      </div>
    );
  }

  guestContent = () => {
    const {
      refresh,
      clientConnection,
      response,
      username,
      pass,
      userID,
      connID,
      guest,
      manager,
      owner,
      founder,
      admin,
      stores,
      products,
      searchedProducts,
    } = this.state;
    return (
      <Fragment>
        <MainPage username={username} />
        <section className="row">
          <div className="col span-1-of-2 box">
            <Register
              onSubmitRegister={this.registerHandler}
              connID={connID}
              clientConnection={clientConnection}
              response={response}
            />
          </div>
          <div className="col span-1-of-2 box">
            <Login
              onSubmitLogin={this.loginHandler}
              connID={connID}
              clientConnection={clientConnection}
              response={response}
              refresHandler={this.onRefresh}
            />
          </div>
        </section>
        <Stores
          refresh={refresh}
          onRefresh={this.onRefresh}
          loadSys={this.loadStores}
          connID={connID}
          clientConnection={clientConnection}
          stores={stores}
          products={products}
          searchedProducts={searchedProducts}
        />
        <ShoppingCart
          refresh={refresh}
          onRefresh={this.onRefresh}
          connID={connID}
          clientConnection={clientConnection}
          response={response}
          username={username}
        />
      </Fragment>
    );
  };

  subscriberContent = () => {
    return (
      <Fragment>
        <section className="section-plans js--section-plans" id="stores">
          <div className="row">
            <h2>
              <strong>Subscriber Services</strong>
            </h2>
          </div>
          {this.state.userID ? (
            <div>
              <p>Will be subscriber services</p>
            </div>
          ) : (
            <p>You are guest, login for subscriber permissions!</p>
          )}
        </section>
      </Fragment>
    );
  };

  ownerContent = (connID, userID) => {
    return (
      <Fragment>
        <StoresOwner connID={connID} userID={userID}></StoresOwner>
      </Fragment>
    );
  };

  endOfPage = () => {
    return (
      <Fragment>
        <Recommendations />
        <Programers />
        <DownPage />
      </Fragment>
    );
  };
  // managerContent = () => {
  //   return <Fragment></Fragment>;
  // };

  // adminContent = () => {
  //   return <Fragment></Fragment>;
  // };
}

export default App;
