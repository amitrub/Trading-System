import React, { useState } from "react";
import createApiClient from "../../ApiClient";
import "./MainPageDesign/style.css";
import "./MainPageDesign/grid.css";
import Product from "./Product";
import Store from "./Store";

const api = createApiClient();

function Stores(props) {
  async function submitLoadStores() {
    console.log("getStores");
    await api.getAllStores(props.clientConnection, props.connID);
  }

  return (
    <section className="section-form" id="stores">
      <div className="row">
        <h2>Stores</h2>
      </div>
      <button
        className="buttonus"
        value="load our stores..."
        onClick={submitLoadStores}
      >
        Show Stores
      </button>

      {props.stores.map((currStore) => (
        <li key={currStore.id} className="curr store">
          <Store
            currStore={currStore}
            clientConnection={props.clientConnection}
            connID={props.connID}
            products={props.products.filter(
              (prod) => prod.storeID === currStore.id
            )}
          ></Store>
        </li>
      ))}
    </section>
  );
}

export default Stores;
