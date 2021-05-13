import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../ApiClientHttp";
import "../../Design/grid.css";
import "../../Design/style.css";
import ProductInCart from "./ProductInCart";
import Purchase from "../Purchase/Purchase";

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
    <Fragment>
      <section className="section-form" id="shoppingcart">
        {/* Shopping Cart header */}
        <div className="row">
          <h2>
            <strong>{props.username}'s Shopping Cart</strong>
          </h2>
        </div>

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
            </div>
          ) : (
            "No products, Go Shop bitch!"
          )}
        </div>
      </section>
      {shoppingCart.length !== 0 ? (
        <Purchase
          refresh={props.refresh}
          onRefresh={props.onRefresh}
          connID={props.connID}
        ></Purchase>
      ) : (
        ""
      )}
    </Fragment>
  );
}

export default ShoppingCart;
