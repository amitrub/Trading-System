import React from "react";
import createApiClient from "../../ApiClient";
import "../../Design/grid.css";
import "../../Design/style.css";
import Store from "./Store";

const api = createApiClient();

function StoresOwner(props) {
  async function submitLoadFoundedStores() {
    console.log("getStores");
    await api.getAllFoundedStores(
      props.clientConnection,
      props.connID,
      props.userID
    );
  }
  async function submitLoadOwnedStores() {
    console.log("getStores");
    await api.getAllOwnedStores(
      props.clientConnection,
      props.connID,
      props.userID
    );
  }
  async function submitLoadManagedStores() {
    console.log("getStores");
    await api.getAllManagedStores(
      props.clientConnection,
      props.connID,
      props.userID
    );
  }

  return (
    <section className="section-form" id="stores">
      <div className="row">
        <h2>Stores</h2>
      </div>
      <button
        className="buttonus"
        value="load our stores..."
        onClick={submitLoadFoundedStores}
      >
        Show Stores
      </button>

      {props.founderStores.map((currStore) => (
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

export default StoresOwner;
