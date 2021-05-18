import React, { Fragment, useEffect } from "react";
import "./App.css";
import "./Design/grid.css";
import "./Design/style.css";
import Register from "./Components/GuestComponents/Register/Register";
import { Client } from "@stomp/stompjs";
import MainPage from "./Components/OtherComponents/MainPage/MainPage";
import Recommendations from "./Components/OtherComponents/MainPage/Recommendations";
import Programers from "./Components/OtherComponents/MainPage/Programers";
import Login from "./Components/GuestComponents/Login/Login";
import Stores from "./Components/GuestComponents/Stores/Stores";
import "./Components/OtherComponents/Navbar/Navbar.css";
import DownPage from "./Components/OtherComponents/MainPage/DownPage";
import ShoppingCart from "./Components/GuestComponents/ShoppingCart/ShoppingCart";
import OwnerStores from "./Components/OwnerComponents/OwnerStores/OwnerStores";
import Logout from "./Components/SubscriberComponents/Logout/Logout";
import MyPopup from "./Components/OtherComponents/MyPopup/MyPopup";
import createApiClientHttp from "./ApiClientHttp";
import SubscriberServices from "./Components/SubscriberComponents/SubscriberServices/SubscriberServices";

const apiHttp = createApiClientHttp();
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
      popupMassages: [],
    };
  }

  onRefresh = () => {
    this.setState((prevState) => ({
      refresh: prevState.refresh ? false : true,
    }));
  };

  onClosePopupApp = () => {
    this.setState(
      (prevState) => ({
        popupMassages: prevState.popupMassages.slice(1),
      }),
      () => {
        this.setState((prevState) => ({
          showPopup: prevState.popupMassages.length !== 0,
        }));
      }
    );
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

  loginHandler = (name, pass, res) => {
    const loginResponse = res;

    if (loginResponse.isErr) {
      console.log(loginResponse.message);
    } else {
      this.setState(
        (prevState) => ({
          username: name,
          pass: pass,
          userID: loginResponse.returnObject.userID,
          connID: loginResponse.returnObject.connID,
          guest: loginResponse.returnObject.guest,
          manager: loginResponse.returnObject.manager,
          owner: loginResponse.returnObject.owner,
          founder: loginResponse.returnObject.founder,
          admin: loginResponse.returnObject.admin,
          founderStoresNames: loginResponse.returnObject.founderStoresNames,
          ownerStoresNames: loginResponse.returnObject.ownerStoresNames,
          managerStoresNames: loginResponse.returnObject.managerStoresNames,
        }),
        () => {
          this.subscribeToTopic(this.state.clientConnection, this.state.connID);
          console.log(res.returnObject.messages);
          res.returnObject.messages.map((msg) => {
            this.setState((prevState) => ({
              popupMassages: [...prevState.popupMassages, msg],
            }));
          });

          this.onRefresh();
        }
      );
    }
    // console.log("End Login Handler! connID: " + this.state.connID);
  };

  logoutHandler = (logoutResponse) => {
    this.setState((prevState) => ({
      username: "guest",
      pass: "",
      userID: -1,
      connID: logoutResponse.returnObject.connID,
      guest: logoutResponse.returnObject.guest,
      manager: logoutResponse.returnObject.manager,
      owner: logoutResponse.returnObject.owner,
      founder: logoutResponse.returnObject.founder,
      admin: false,
    }));
  };

  subscribeToTopic = (clientConnection, topicName) => {
    clientConnection.subscribe(`/topic/${topicName}`, (msg) => {
      if (msg.body) {
        var jsonBody = JSON.parse(msg.body);
        console.log(jsonBody);
        if (jsonBody.message) {
          this.setState(
            (prevState) => ({
              popupMassages: [...prevState.popupMassages, jsonBody.message],
            }),
            () => {
              this.onRefresh();
            }
          );
          // console.log(jsonBody);
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
            // console.log(jsonBody);
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

    const connectionRespone = await apiHttp.ConnectSystem();
    // console.log("Connection response:");
    // console.log(connectionRespone);
    if (!connectionRespone) console.log("Error response is null!!!");

    this.setState(
      (prevState) => ({
        response: connectionRespone,
        connID: connectionRespone.returnObject.connID,
      }),
      () => {
        // console.log(connectionRespone.returnObject.connID);
        // console.log(this.state.connID);
        // here we sure that the connID updated
        client.activate();
        // this.setState({
        //   clientConnection: client,
        // });
      }
    );
  }

  render() {
    const { userID, connID, showPopup, popupMassages } = this.state;
    return !this.state.clientConnection ? (
      <Fragment>
        <h3>Connecting To System...</h3>
      </Fragment>
    ) : (
      <div className="App">
        {popupMassages.length > 0 ? (
          <MyPopup
            errMsg={popupMassages[0]}
            onClosePopup={this.onClosePopupApp}
          ></MyPopup>
        ) : (
          ""
        )}
        {this.guestContent()}
        {this.subscriberContent()}
        {this.ownerContent(connID, userID)}
        {this.endOfPage()}
      </div>
    );
  }

  guestContent = () => {
    const { refresh, username, userID, connID } = this.state;
    return (
      <Fragment>
        <MainPage username={username} />
        {username === "guest" ? (
          <section className="row" id="sign">
            <div className="col span-1-of-2 box">
              <Register connID={connID} />
            </div>
            <div className="col span-1-of-2 box">
              <Login
                connID={connID}
                onSubmitLogin={this.loginHandler}
                onRefresh={this.onRefresh}
              />
            </div>
          </section>
        ) : (
          <section className="row">
            <div className="box">
              <Logout
                userID={userID}
                connID={connID}
                onLogout={this.logoutHandler}
                onRefresh={this.onRefresh}
              />
            </div>
          </section>
        )}

        <Stores refresh={refresh} onRefresh={this.onRefresh} connID={connID} />

        <ShoppingCart
          refresh={refresh}
          onRefresh={this.onRefresh}
          connID={connID}
          username={username}
          userID={userID}
        />
      </Fragment>
    );
  };

  subscriberContent = () => {
    const { username, userID, connID, refresh } = this.state;
    return (
      <Fragment>
        <section className="section-plans js--section-plans" id="subscribers">
          <div className="row">
            <h2>
              <strong>Subscriber Services</strong>
            </h2>
          </div>
          {this.state.userID !== -1 ? (
            <SubscriberServices
              connID={connID}
              userID={userID}
              username={username}
              refresh={refresh}
              onRefresh={this.onRefresh}
            />
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
        <OwnerStores
          connID={connID}
          userID={userID}
          refresh={this.state.refresh}
          onRefresh={this.onRefresh}
        ></OwnerStores>
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
