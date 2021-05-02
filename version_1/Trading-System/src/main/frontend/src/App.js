import React from "react";
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
import ShoppingCart from "./Components/ShoppingCart/ShoppingCart";

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
      guest: true,
      manager: false,
      owner: false,
      founder: false,
      admin: false,
      stores: [],
      products: [],
      searchedProducts: [],
      shoppingCart: [],
    };
  }

  onAddToCart = (product, quantity) => {
    console.log("App.js onAddToCart:");
    console.log(product);
    console.log(quantity);
    const productToBuy = {
      storeID: product.storeID,
      storeName: product.storeName,
      productID: product.productID,
      productName: product.productName,
      price: product.price,
      category: product.category,
      quantity: quantity,
    };
    this.setState(
      (prevState) => ({
        shoppingCart: [...prevState.shoppingCart, productToBuy],
      }),
      () => {
        console.log("ufter shopping cart update:");
        console.log(this.state.shoppingCart);
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

  loginHandler = (name, pass) => {
    console.log("LoginHandler:" + name);
    const currentComponent = this;
    const loginResponse = this.state.response;

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
          guest: loginResponse.returnObject.guest,
          manager: loginResponse.returnObject.manager,
          owner: loginResponse.returnObject.owner,
          founder: loginResponse.returnObject.founder,
          admin: loginResponse.returnObject.admin,
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

  render() {
    return (
      <div className="App">
        <BrowserRouter>
          <div className="AppTry">
            {/* <Navbar /> */}
            <Switch>
              <Route path="/">
                <MainPage username={this.state.username} />
                <Stores
                  loadSys={this.loadStores}
                  connID={this.state.connID}
                  clientConnection={this.state.clientConnection}
                  stores={this.state.stores}
                  products={this.state.products}
                  searchedProducts={this.state.searchedProducts}
                  onAddToCart={this.onAddToCart}
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
                <Recommendations />
                <Programers />
                <DownPage />
                <ShoppingCart
                  connID={this.state.connID}
                  clientConnection={this.state.clientConnection}
                  response={this.state.response}
                  username={this.state.username}
                  shoppingCart={this.state.shoppingCart}
                />
              </Route>
              <Route path="/services"></Route>
              <Route path="/admin"></Route>
              <Route path="/cart"></Route>
            </Switch>
          </div>
        </BrowserRouter>
      </div>
    );
  }
}

export default App;
