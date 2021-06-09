import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function Bid(props) {
  const [showInputBoxes, setShowInputBoxes] = useState(false);
  const [quantity, setQuantity] = useState(props.quantity);
  //   const [productID, setProductID] = useState("");
  const [newPrice, setNewPriceOffer] = useState(props.price);
  //====
  const [mode, setMode] = useState("");

  async function submitBiddingHandler(event, mode) {
    event.preventDefault();

    const responedResponse = await apiHttp.ResponsedToBidding(
      props.connID,
      props.userID,
      props.storeID,
      props.productID,
      quantity,
      newPrice,
      mode
    );
    console.log("afterafter");
    console.log(responedResponse);

    if (responedResponse.isErr) {
      console.log(responedResponse.message);
    } else {
      //TODO HADAS !!!!!
      //   setBiddingList(biddingListResponse.returnObject.DailyIncome);
    }
  }

  function updateQuantity(event) {
    setQuantity(event.target.value);
    props.onRefresh();
  }

  function updateNewPriceOffer(event) {
    setNewPriceOffer(event.target.value);
    props.onRefresh();
  }

  function newOfferHandler(event) {
    setShowInputBoxes(true);
    setMode(2);
  }

  async function approveHandler(event) {
    await setShowInputBoxes(false);
    await setQuantity(props.quantity);
    await setNewPriceOffer(props.price);
    await setMode(1);
    await submitBiddingHandler(event, 1);
  }

  async function ignoreHandler(event) {
    setShowInputBoxes(false);
    setQuantity(props.quantity);
    setNewPriceOffer(props.price);
    setMode(0);
    await submitBiddingHandler(event, 0);
  }

  return (
    <Fragment>
      <section className="section-form" id="Bid">
        <div>
          <h5>
            The client {props.userID}, offers the next bidding: buy{" "}
            {props.quantity} units of product ID {props.productID} in the price{" "}
            {props.price} per unit{" "}
          </h5>
        </div>

        <div className="row">
          {/* Ignore */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={ignoreHandler}
          >
            Ignore
            {/* {showStore ? "Hide" : "Show products"} */}
          </button>

          {/* Aprrove */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={approveHandler}
          >
            Approve
            {/* {showStore ? "Hide" : "Show products"} */}
          </button>

          {/* New Offer */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={newOfferHandler}
          >
            New Offer
            {/* {showStore ? "Hide" : "Show products"} */}
          </button>

          {showInputBoxes ? (
            <Fragment>
              <form
                method="post"
                className="contact-form"
                onSubmit={submitBiddingHandler}
              >
                {/* quantity */}
                <div className="row">
                  <div className="col span-1-of-3">
                    <label htmlFor="name">Quantity</label>
                  </div>
                  <div className="col span-2-of-3">
                    <input
                      type="number"
                      name="Quantity"
                      id="Quantity"
                      required
                      onChange={updateQuantity}
                      placeholder={"type quantity"}
                    />
                  </div>
                </div>

                {/* castumer offer */}
                <div className="row">
                  <div className="col span-1-of-3">
                    <label htmlFor="name">New price offer</label>
                  </div>
                  <div className="col span-2-of-3">
                    <input
                      type="number"
                      name="Price"
                      id="Price"
                      required
                      onChange={updateNewPriceOffer}
                      placeholder={"offer new price"}
                    />
                  </div>
                </div>

                <div className="row">
                  <div className="col span-1-of-3">
                    <label>&nbsp;</label>
                  </div>
                  <div className="col span-1-of-3">
                    <input type="submit" value="offer new price!" />
                  </div>
                </div>
              </form>
            </Fragment>
          ) : (
            ""
          )}
        </div>
      </section>
    </Fragment>
  );
}

export default Bid;
