import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function ChangeQuantityProduct(props) {
  const [productID, setProductID] = useState(-1);
  const [quantity, setQuantity] = useState(-1);

  const [popupChangeQuantity, setPopupChangeQuantity] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function submitChangeQuantityHandler(event) {
    event.preventDefault();
    // console.log("before ChangeQuantityProduct");

    const changeQuantityProductResponse = await apiHttp.ChangeQuantityProduct(
      props.connID,
      props.userID,
      props.storeID,
      productID,
      quantity
    );

    // console.log("ChangeQuantityProduct");
    // console.log(changeQuantityProductResponse);

    if (changeQuantityProductResponse) {
      setPopupMsg(changeQuantityProductResponse.message);
      setPopupChangeQuantity(true);
    }
    if (changeQuantityProductResponse.isErr) {
      console.log(changeQuantityProductResponse.message);
    }
    props.onRefresh();
  }

  function updateProductID(event) {
    setProductID(event.target.value);
  }

  function updateQuantity(event) {
    setQuantity(event.target.value);
  }

  function onClosePopup() {
    setPopupChangeQuantity(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h2>Add quantity to product</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitChangeQuantityHandler}
          >
            {/* product id */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Product ID</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="id"
                  id="id"
                  required
                  onChange={updateProductID}
                  placeholder={"choose prodID to add to"}
                />
              </div>
            </div>
            {/* Quantity */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Quantity</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Quantity"
                  placeholder="insert quantity"
                  required
                  onChange={updateQuantity}
                />
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Change quantity!" />
              </div>
            </div>
          </form>
        </div>
      </div>
      {popupChangeQuantity ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default ChangeQuantityProduct;
