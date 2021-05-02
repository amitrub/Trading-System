import React, { useState } from "react";
import createApiClient from "../../ApiClient";
import "../../Design/grid.css";
import "../../Design/style.css";
import Search from "../Search/Search";
import Store from "./Store";

const api = createApiClient();

function Stores(props) {
  const [search, setSearch] = useState(false);
  const [stores, setStores] = useState(false);

  async function submitLoadStores() {
    console.log("get Stores");
    setStores(true);
    await api.getAllStores(props.clientConnection, props.connID);
  }

  async function submitCloseStores() {
    console.log("close Stores");
    setStores(false);
  }

  function submitOpenSearchStores() {
    console.log("submit Open Search Stores");
    setSearch(true);
  }

  function submitCloseSearchStores() {
    console.log("submit Close Search Stores");
    setSearch(false);
  }

  return (
    <section className="section-form" id="stores">
      <div className="row">
        <h2>Stores</h2>
      </div>
      <div className="row">
        <div className="col span-1-of-2">
          <button
            className="buttonus"
            value="load our stores..."
            onClick={stores ? submitCloseStores : submitLoadStores}
          >
            {stores ? "Hide Stores" : "Show All Stores"}
          </button>
        </div>
        <div className="col span-1-of-2">
          <button
            className="buttonus"
            value="load our stores..."
            onClick={search ? submitCloseSearchStores : submitOpenSearchStores}
          >
            {search ? "Hide Search" : "Search"}
          </button>
        </div>
      </div>

      {search ? (
        <Search
          clientConnection={props.clientConnection}
          connID={props.connID}
          searchedProducts={props.searchedProducts}
        ></Search>
      ) : (
        ""
      )}

      {stores
        ? props.stores.map((currStore) => (
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
          ))
        : ""}
    </section>
  );
}

export default Stores;
