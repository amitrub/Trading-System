import React, { useState } from "react";
import "../../Design/grid.css";
import "../../Design/style.css";
import createApiClient from "../../ApiClient";

const apiHtml = createApiClient();

function ProductInCart(props) {
  const [quantityToBuy, setQuantityToBuy] = useState(0);
  const product = props.currProduct;

  function insertQuantityToEdit(event) {
    setQuantityToBuy(event.target.value);
  }

  async function submitBuyProductHandler(event) {
    event.preventDefault();
    console.log(
      "Product: " +
        product.productName +
        " added to Cart! quantity added: " +
        quantityToBuy +
        "prodID=" +
        product.productID +
        " storeID=" +
        product.storeID
    );

    const responseAddProductToCart = await apiHtml.addProductToCart(
      props.clientConnection,
      props.connID,
      product.storeID,
      product.productID,
      quantityToBuy
    );

    console.log(responseAddProductToCart);
    props.onAddToCart(product, quantityToBuy);

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
              onChange={insertQuantityToEdit}
            />
          </div>
          <div className={"row"}>
            <div className={"col span-1-of-2"}>
              <div className="btn btn-ghost">
                <input type="submit" value="Remove from cart" />
              </div>
            </div>
            <div className={"col span-2-of-2"}>
              <div className="btn btn-ghost">
                <input type="submit" value="Edit quantity" />
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ProductInCart;
