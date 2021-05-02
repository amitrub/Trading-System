import React, { useState } from "react";
import createApiClient from "../../ApiClient";
import "../../Design/grid.css";
import "../../Design/style.css";
import ProductInCart from "./ProductInCart";

const api = createApiClient();

function ShoppingCart(props) {
  const [showCart, setshowCart] = useState(false);
  let prodKey = 1;

  //   async function submitLoadProducts() {
  //     console.log("submit Load Products");
  //     await api.getAllProductsOfStore(
  //       props.clientConnection,
  //       props.connID,
  //       props.currStore.id
  //     );
  //   }

  return (
    <section className="section-plans js--section-plans" id="store">
      {/* Shopping Cart header */}
      <div className="row">
        <h2>
          <strong>{props.username}'s Shopping Cart</strong>
        </h2>
      </div>

      {/* Show/Hide Cart */}
      <button
        className="buttonus"
        value="show/hide cart"
        onClick={showCart ? setshowCart(false) : setshowCart(true)}
      >
        {showCart ? "Hide cart" : "Show cart"}
      </button>

      {/* Show shopping Cart */}
      <div className="row">
        {showCart
          ? props.shoppingCart.map((currProduct) => (
              <div className="col span-1-of-4">
                <li
                  key={"cart/".concat(
                    currProduct.productID
                      .toString()
                      .concat((prodKey++).toString())
                  )}
                  className="curr product"
                >
                  <ProductInCart
                    currProduct={currProduct}
                    clientConnection={props.clientConnection}
                    connID={props.connID}
                  ></ProductInCart>
                </li>
              </div>
            ))
          : ""}
      </div>
    </section>
  );
}

export default ShoppingCart;
