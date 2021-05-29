import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import ProductInHistory from "./ProductInHistory";
import Product from "../../GuestComponents/Stores/Product";

const apiHttp = createApiClientHttp();

function PurchaseHistory(props) {
  const [showHistory, setShowHistory] = useState(false);
  const history = props.currHistory;
  const products = history.products;

  function submitLoadHistory() {
    setShowHistory(true);
    props.onRefresh();
  }

  function sumbitHideHistory() {
    setShowHistory(false);
    props.onRefresh();
  }

  return (
    <section className="section-plans js--section-plans" id="purch history">
      {history ? (
        <div>
          <div className="row">
            <h3>
              {history.date} --- Total Cost: {history.finalPrice}
            </h3>
            <h3>
              {" "}
              <strong>
                {" "}
                {products.length > 0
                  ? "Purchased from " + products[0].storeName
                  : ""}
              </strong>
            </h3>
          </div>
          <button
            className="buttonus"
            value="load our stores..."
            onClick={showHistory ? sumbitHideHistory : submitLoadHistory}
          >
            {showHistory ? "Hide" : `Show`}
          </button>

          <div className="row">
            {showHistory
              ? products.map((currProduct, index) => (
                  <div className="col span-1-of-4">
                    <li key={index} className="curr product">
                      <ProductInHistory
                        refresh={props.refresh}
                        onRefresh={props.onRefresh}
                        connID={props.connID}
                        currProduct={currProduct}
                      ></ProductInHistory>
                    </li>
                  </div>
                ))
              : ""}
          </div>
        </div>
      ) : (
        "something wrong... - history is empty"
      )}
    </section>
  );
}

export default PurchaseHistory;
