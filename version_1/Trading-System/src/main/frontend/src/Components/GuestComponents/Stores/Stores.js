import React, { useEffect, useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import Search from "../Search/Search";
import Store from "./Store";

const apiHttp = createApiClientHttp();

function Stores(props) {
  const [search, setSearch] = useState(false);
  const [stores, setStores] = useState(true);
  const [allStores, setAllStores] = useState([]);

  async function fetchStores() {
    const storesResponse = await apiHttp.ShowAllStores();
    // console.log(storesResponse);

    if (storesResponse.isErr) {
      console.log(storesResponse.message);
    } else {
      setAllStores(storesResponse.returnObject.stores);
    }
  }

  useEffect(() => {
    fetchStores();
    // console.log("useEffectStores");
  }, [props.refresh]);

  function submitShowStores() {
    // console.log("get Stores");
    setStores(true);
    props.onRefresh();
  }

  function submitCloseStores() {
    // console.log("close Stores");
    setStores(false);
  }

  function submitOpenSearchStores() {
    // console.log("submit Open Search Stores");
    setSearch(true);
  }

  function submitCloseSearchStores() {
    // console.log("submit Close Search Stores");
    setSearch(false);
  }

  return (
    <section className="section-plans js--section-plans" id="stores">
      <div className="row">
        <h2>Stores</h2>
      </div>
      <div className="row">
        <div className="col span-1-of-2">
          <button
            className="buttonus"
            value="load our stores..."
            onClick={stores ? submitCloseStores : submitShowStores}
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
          refresh={props.refresh}
          onRefresh={props.onRefresh}
          connID={props.connID}
        />
      ) : (
        ""
      )}
      <section className="section-form"></section>
      <section className="section-form">
        {stores
          ? allStores.map((currStore, index) => (
              <li key={index} className="curr store">
                <Store
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  currStore={currStore}
                />
              </li>
            ))
          : ""}
      </section>
    </section>
  );
}

export default Stores;
