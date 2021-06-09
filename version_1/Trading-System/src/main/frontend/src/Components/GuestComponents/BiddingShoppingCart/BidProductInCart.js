import React, { useState } from "react";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import createApiClientHttp from "../../../ApiClientHttp";

const apiHtml = createApiClientHttp();

function BidProductInCart(props) {
  const product = props.currProduct;

  async function sumbitRemoveProduct() {
    console.log("sumbitRemoveProduct");
    const removeProdFromCartResponse = await apiHtml.RemoveProductFromCart(
      props.connID,
      product.storeID,
      product.productID
    );

    if (removeProdFromCartResponse.isErr) {
      console.log(removeProdFromCartResponse.message);
    } else {
      props.onRefresh();
    }
  }

  return (
    <div className="plan-box">
      <div>
        <h3>{product.productName}</h3>
        <p className="plan-price">${product.price}</p>
        <p className="plan-price-meal">{product.quantity} units in cart</p>
        <p className="plan-price-meal">
          from store '{product.storeName}' (id={product.storeID})
        </p>
      </div>
      <div>
        <p>Category: {product.category}</p>
      </div>
      <div>
        {/* <form onSubmit={submitBuyProductHandler} method="post" action="#"> */}

        <button
          className="buttonus"
          value="Remove from cart"
          onClick={sumbitRemoveProduct}
        >
          Remove from cart
        </button>
        {/* </form> */}
      </div>
    </div>
  );
}

export default BidProductInCart;
