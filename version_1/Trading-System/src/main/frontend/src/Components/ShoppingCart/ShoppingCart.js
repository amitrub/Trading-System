import React, { useEffect, useState } from "react";
import createApiClientHttp from "../../ApiClientHttp";
import "../../Design/grid.css";
import "../../Design/style.css";
import ProductInCart from "./ProductInCart";

const api = createApiClientHttp();

function ShoppingCart(props) {
  // const [showCart, setshowCart] = useState(false);
  const [shoppingCart, setShoppingCart] = useState([]);

  async function fetchShopingCart() {
    const showCartResponse = await api.ShowShoppingCart(props.connID);
    console.log(showCartResponse);

    if (showCartResponse.isErr) {
      console.log(showCartResponse.message);
    } else {
      setShoppingCart(showCartResponse.returnObject.products);
    }
  }

  async function sumbitPurchaseCart() {
    console.log("sumbitPurchaseCart");
  }

  useEffect(() => {
    fetchShopingCart();
    console.log("try");
  }, [props.refresh]);

  // function onShowCart() {
  //   setshowCart(false);
  // }

  // function onHideCart() {
  //   setshowCart(true);
  // }

  return (
    <section className="section-plans js--section-plans" id="shoppingcart">
      {/* Shopping Cart header */}
      <div className="row">
        <h2>
          <strong>{props.username}'s Shopping Cart</strong>
        </h2>
      </div>

      {/* Show/Hide Cart */}
      {/* <button
        className="buttonus"
        value="show/hide cart"
        onClick={showCart ? onShowCart : onHideCart}
      >
        {showCart ? "Hide cart" : "Show cart"}
      </button> */}

      {/* Show shopping Cart */}
      <div className="row">
        {shoppingCart.length > 0 ? (
          <div>
            <div>
              {shoppingCart.map((currProduct, index) => (
                <div className="col span-1-of-4">
                  <li key={index} className="curr product">
                    <ProductInCart
                      refresh={props.refresh}
                      onRefresh={props.onRefresh}
                      currProduct={currProduct}
                      clientConnection={props.clientConnection}
                      connID={props.connID}
                    ></ProductInCart>
                  </li>
                </div>
              ))}
            </div>
            <div>
              <button
                className="buttonus"
                value="Purchase"
                onClick={sumbitPurchaseCart}
              >
                Checkout
              </button>
            </div>
          </div>
        ) : (
          "No products, Go Shop bitch!"
        )}
      </div>
    </section>
  );
}

export default ShoppingCart;
