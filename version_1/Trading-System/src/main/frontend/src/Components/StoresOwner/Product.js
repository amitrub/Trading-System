import React from "react";
import createApiClient from "../../ApiClient";
import "../../Design/grid.css";
import "../../Design/style.css";

const api = createApiClient();

function Product(props) {
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
