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
      showStores: false,
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

  showStoresClick = () => {
    this.setState((prevState) => ({
      showStores: !prevState.showStores,
    }));
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
      showStores,
    } = this.state;
    return (
      <section className="section-form" id="owners">
        <div className="row">
          <h2>
            <strong>My Stores</strong>
          </h2>
        </div>
        <div className="row">
          <h2>
            <strong>{userID}</strong>
          </h2>
        </div>
        {userID !== -1 ? (
          <div>
            <div className="row"></div>
            <button
              className="buttonus"
              value="load our stores..."
              onClick={this.showStoresClick}
            >
              Show Stores
            </button>
            {showStores ? (
              founderStores.map((store) => (
                <li key={store.id} className="curr store">
                  <Store connID={connID} userID={userID} store={store}></Store>
                  <h3>{userID}</h3>
                </li>
              ))
            ) : (
              <h3></h3>
            )}
          </div>
        ) : (
          <div>
            <p>You are guest, login for owner permissions!</p>
          </div>
        )}
      </section>
    );
  }
}

export default StoresOwner;
