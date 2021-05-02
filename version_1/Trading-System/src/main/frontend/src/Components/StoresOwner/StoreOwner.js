import React, { Component, Fragment } from "react";
import createApiClientHttp from "../../ApiClientHttp";
import "../../Design/grid.css";
import "../../Design/style.css";
import ProductOwner from "./ProductOwner";

const api = createApiClientHttp();
class StoreOwner extends Component {
  constructor(props) {
    super(props);

    this.state = {
      connID: "",
      userID: "",
      storeID: -1,
      storeName: "",
      storeRate: 0,
      products: [],
      showProducts: false,
    };
  }

  componentDidMount() {
    const { connID, userID, store } = this.props;

    api.ShowStoreProducts(store.id).then((res) => {
      if (res.returnObject.products) {
        this.setState({
          connID: connID,
          userID: userID,
          storeID: store.id,
          storeName: store.name,
          storeRate: store.storeRate,
          products: res.returnObject.products,
        });
      }
    });
  }

  showProductsHandler = () => {
    this.setState((prevState) => ({
      showProducts: !prevState.showProducts,
    }));
  };

  render() {
    const {
      connID,
      userID,
      storeID,
      storeName,
      storeRate,
      products,
      showProducts,
    } = this.state;
    return (
      <section className="section-plans js--section-plans" id="store">
        <div className="row">
          <h2>
            <strong>{storeName}</strong> rate:{storeRate}
          </h2>
        </div>
        <button
          className="buttonus"
          value="load our stores..."
          onClick={this.showProductsHandler}
        >
          Show me products
        </button>

        <div className="row">
          {!showProducts ? (
            <Fragment />
          ) : (
            products.map((product) => (
              <div className="col span-1-of-4">
                <li key={product.productID} className="curr product">
                  <ProductOwner
                    connID={connID}
                    userID={userID}
                    storeID={storeID}
                    storeName={storeName}
                    storeRate={storeRate}
                    product={product}
                  ></ProductOwner>
                </li>
              </div>
            ))
          )}
        </div>
      </section>
    );
  }
}

export default StoreOwner;
