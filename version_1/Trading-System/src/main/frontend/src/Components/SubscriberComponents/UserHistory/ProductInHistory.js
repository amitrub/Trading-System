import React, { useState, useEffect } from "react";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import createApiClientHttp from "../../../ApiClientHttp";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function ProductInHistory(props) {
  const product = props.currProduct;

  return (
    <div className="plan-box">
      <div>
        <h3>{product.productName}</h3>
        <p className="plan-price">${product.price}</p>
        <p className="plan-price-meal">
          purchased {product.quantity} from '{product.storeName}'
        </p>
      </div>
      <div>
        <p>Category: {product.category}</p>
      </div>
    </div>
  );
}

export default ProductInHistory;
