import React, { useState } from "react";
import createApiClient from "../../ApiClient";
import "./MainPageDesign/style.css";
import "./MainPageDesign/grid.css";
import Product from "./Product";

const api = createApiClient();

function Store(props) {
  async function submitLoadProducts() {
    console.log("submit Load Products");
    await api.getAllProductsOfStore(
      props.clientConnection,
      props.connID,
      props.currStore.id
    );
  }

  const store = props.currStore;

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
        onClick={submitLoadProducts}
      >
        Show me products
      </button>

      <div className="row">
        {props.products.map((currProduct) => (
          <div className="col span-1-of-4">
            <li key={currProduct.productID} className="curr product">
              <Product
                currProduct={currProduct}
                clientConnection={props.clientConnection}
                connID={props.connID}
              ></Product>
            </li>
          </div>
        ))}
      </div>
    </section>
  );
}

export default Store;
