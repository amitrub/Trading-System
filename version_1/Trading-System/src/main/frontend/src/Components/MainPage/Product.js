import React, { useState } from "react";
import createApiClient from "../../ApiClient";
import "./MainPageDesign/style.css";
import "./MainPageDesign/grid.css";

const api = createApiClient();

function Product(props) {
  const [storeID, setStoreID] = useState(-1);
  const [storeName, setStoreName] = useState("storeName");
  const [productID, setProductID] = useState(-1);
  const [productName, setProductName] = useState("productName");
  const [price, setPrice] = useState(0);
  const [category, setCategory] = useState("category");
  const [quantity, setQuantity] = useState(0);

  const product = props.currProduct;

  async function submitBuyProductHandler(event) {
    event.preventDefault();
    console.log("gjkbj");
    // setConnIDState(props.connID);

    // await api.register(
    //   props.clientConnection,
    //   props.connID,
    //   enteredName,
    //   enteredPass
    // );

    // props.onSubmitAddToCart();
  }

  return (
    <div className="plan-box">
      <div>
        <h3>{product.productName}</h3>
        <p className="plan-price">${product.price}</p>
        <p className="plan-price-meal">
          {product.quantity} units on '{product.storeName}'
        </p>
      </div>
      <div>
        <p>Category: {product.category}</p>
      </div>
      <div>
        <form onSubmit={submitBuyProductHandler} method="post" action="#">
          <div className="">
            <input
              type="number"
              name="quantity"
              id="quantity"
              placeholder="enter quantity"
              required
              min="1"
            />
          </div>
          <div className="btn btn-ghost">
            <input type="submit" value="Buy" />
          </div>
        </form>
      </div>
    </div>
  );
}

export default Product;
