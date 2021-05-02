import React, { useState } from "react";
import createApiClient from "../../ApiClient";
import "../../Design/grid.css";
import "../../Design/style.css";
import Product from "./Product";

const api = createApiClient();

function Store(props) {
  const [showStore, setShowStore] = useState(false);
  const store = props.currStore;

  async function submitLoadProducts() {
    console.log("submit Load Products");
    setShowStore(true);
    await api.getAllProductsOfStore(
      props.clientConnection,
      props.connID,
      props.currStore.id
    );
  }

  async function sumbitHideProducts() {
    console.log("submit hide Products");
    setShowStore(false);
  }

  return (
    <section className="section-plans js--section-plans" id="store">
      <div className="row">
        <h2>
          <strong>{store.name}</strong> rate:{store.storeRate}
        </h2>
      </div>
      <button
        className="buttonus"
        value="load our stores..."
        onClick={showStore ? sumbitHideProducts : submitLoadProducts}
      >
        {showStore ? "Hide products" : "Show me products"}
      </button>

      <div className="row">
        {showStore
          ? props.products.map((currProduct, index) => (
              <div className="col span-1-of-4">
                <li key={index} className="curr product">
                  <Product
                    refresh={props.refresh}
                    onRefresh={props.onRefresh}
                    currProduct={currProduct}
                    clientConnection={props.clientConnection}
                    connID={props.connID}
                  ></Product>
                </li>
              </div>
            ))
          : ""}
      </div>
    </section>
  );
}

export default Store;
