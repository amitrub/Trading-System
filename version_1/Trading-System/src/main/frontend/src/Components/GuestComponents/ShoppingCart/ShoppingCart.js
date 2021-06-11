import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import ProductInCart from "./ProductInCart";
import BidProductInCart from "./BidProductInCart";

const apiHttp = createApiClientHttp();

function ShoppingCart(props) {
  const [shoppingCart, setShoppingCart] = useState([]);
  const [shoppingCartBid, setShoppingCartBid] = useState([]);

  async function fetchShopingCart() {
    const showCartResponse = await apiHttp.ShowShoppingCart(props.connID);
    // console.log(showCartResponse);

    if (showCartResponse.isErr) {
      console.log(showCartResponse.message);
    } else {
      setShoppingCart(showCartResponse.returnObject.products);
    }
  }

  async function fetchShopingCartBid() {
    const showCartBidResponse = await apiHttp.ShowShoppingCartBid(props.connID);
    // console.log(showCartResponse);

    if (showCartBidResponse.isErr) {
      console.log(showCartBidResponse.message);
    } else {
      setShoppingCartBid(showCartBidResponse.returnObject.products);
    }
  }

  useEffect(() => {
    fetchShopingCart();
    fetchShopingCartBid();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        {/* Shopping Cart header */}
        <div className="row">
          <h2>
            <strong>{props.username}'s Shopping Cart</strong>
          </h2>
        </div>

        <h3> Regular Products </h3>

        <div className="row">
          {shoppingCart.length > 0 ? (
            <div>
              <div>
                {shoppingCart.map((currProduct, index) => (
                  <div className="col span-1-of-4">
                    <li key={index} className="curr product">
                      <ProductInCart
                        onRefresh={props.onRefresh}
                        connID={props.connID}
                        currProduct={currProduct}
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

        <h3> Bidding Products </h3>

        <div className="row">
          {shoppingCartBid.length > 0 ? (
            <div>
              <div>
                {shoppingCartBid.map((currProduct, index) => (
                  <div className="col span-1-of-4">
                    <li key={index} className="curr product">
                      <BidProductInCart
                        onRefresh={props.onRefresh}
                        connID={props.connID}
                        currProduct={currProduct}
                      ></BidProductInCart>
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
    </Fragment>
  );
}

export default ShoppingCart;
