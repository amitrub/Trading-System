import React, { useState } from "react";
import "../../Design/grid.css";
import "../../Design/style.css";
import createApiClientHttp from "../../ApiClientHttp";

const apiHtml = createApiClientHttp();

function Product(props) {
  const [quantityToBuy, setQuantityToBuy] = useState(0);
  const product = props.currProduct;

  function insertQuantity(event) {
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

    const quantityToBuyInt = parseInt(quantityToBuy);
    const responseAddProductToCart = await apiHtml.AddProductToCart(
      props.connID,
      product.storeID,
      product.productID,
      quantityToBuyInt
    );

    console.log("Product.js:");
    console.log(responseAddProductToCart);

    props.onRefresh();
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
              placeholder="enter quantity"
              required
              min="1"
              onChange={insertQuantity}
            />
          </div>
          <div className="btn btn-ghost">
            <input type="submit" value="Add to cart!" />
          </div>
        </form>
      </div>
    </div>
  );
}

export default Product;
