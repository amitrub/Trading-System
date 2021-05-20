import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import Product from "./Product";

const apiHttp = createApiClientHttp();

function Store(props) {
  const [productsOfStore, setProductsOfStore] = useState([]);
  const [showStore, setShowStore] = useState(false);
  const store = props.currStore;

  async function fetchStoreProducts() {
    const productsOfStoresResponse = await apiHttp.ShowStoreProducts(store.id);
    // console.log(productsOfStoresResponse);

    if (productsOfStoresResponse.isErr) {
      console.log(productsOfStoresResponse.message);
    } else {
      setProductsOfStore(productsOfStoresResponse.returnObject.products);
    }
  }

  useEffect(() => {
    fetchStoreProducts();
  }, [props.refresh]);

  function submitLoadProducts() {
    setShowStore(true);
    props.onRefresh();
  }

  function sumbitHideProducts() {
    setShowStore(false);
    props.onRefresh();
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
          ? productsOfStore.map((currProduct, index) => (
              <div className="col span-1-of-4">
                <li key={index} className="curr product">
                  <Product
                    refresh={props.refresh}
                    onRefresh={props.onRefresh}
                    connID={props.connID}
                    currProduct={currProduct}
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
