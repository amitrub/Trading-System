import React, { Component } from "react";
import createApiClientHttp from "../../ApiClientHttp";
import "../../Design/grid.css";
import "../../Design/style.css";
import Store from "./StoreOwner";

const api = createApiClientHttp();

class StoresOwner extends Component {
  constructor(props) {
    super(props);

    this.state = {
      connID: "",
      userID: -1,
      founderStores: [],
      ownerStores: [],
      managerStores: [],
    };
  }

  componentWillReceiveProps(newProps) {
    console.log("8888");
    const { connID, userID } = newProps;
    this.setState({
      connID: connID,
      userID: userID,
    });
  }

  click1 = () => {
    console.log("click");
  };

  componentDidMount() {
    const { connID, userID } = this.props;
    api.ShowAllFoundedStores(connID, userID).then((res) => {
      console.log(res);
      if (res.returnObject.founderStores) {
        this.setState({
          founderStores: res.returnObject.founderStores,
          connID: connID,
          userID: userID,
        });
      }
    });
  }
  render() {
    const {
      connID,
      userID,
      founderStores,
      ownerStores,
      managerStores,
    } = this.state;
    return (
      <section className="section-form" id="stores2">
        <div className="row">
          <h2>Stores</h2>
        </div>
        <div className="row"></div>
        <button
          className="buttonus"
          value="load our stores..."
          onClick={this.click1}
        >
          Show Stores
        </button>
        {founderStores.map((store) => (
          <li key={store.id} className="curr store">
            <Store connID={connID} userID={userID} store={store}></Store>
            <h3>{userID}</h3>
          </li>
        ))}
      </section>
    );
  }
}

export default StoresOwner;
