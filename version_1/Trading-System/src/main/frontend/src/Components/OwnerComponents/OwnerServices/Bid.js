import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function Bid(props) {
  const [showInputBoxes, setShowInputBoxes] = useState(false);
  const [quantity, setQuantity] = useState(null);
  const [newPrice, setNewPriceOffer] = useState(null);
  const [showPopup, setShowPopup] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  //====

  async function submitBiddingHandler(event, mode) {
    event.preventDefault();

    const responedResponse = await apiHttp.ResponsedToBidding(
      props.connID,
      props.userID,
      props.storeID,
      props.productID,
      props.userWhoOffer,
      quantity != null ? quantity : props.quantity,
      newPrice != null ? newPrice : props.price,
      mode
    );
    // console.log("afterafter");
    // console.log(responedResponse);

    setPopupMsg(responedResponse.message);
    setShowPopup(true);

    if (responedResponse.isErr) {
      console.log(responedResponse.message);
    } else {
      props.onRefresh();
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
  }

  function approveHandler(event) {
    setShowInputBoxes(false);
    setQuantity(props.quantity);
    setNewPriceOffer(props.price);
    submitBiddingHandler(event, 1);
  }

  function ignoreHandler(event) {
    setShowInputBoxes(false);
    setQuantity(props.quantity);
    setNewPriceOffer(props.price);
    submitBiddingHandler(event, 0);
  }

  function onClosePopupBid() {
    setShowPopup(false);
    props.onRefresh();
  }

  return (
    <Fragment>
      <section className="section-form" id="Bid">
        <div>
          <h5>
            The client {props.userWhoOffer}, offers the next bidding: buy{" "}
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
                onSubmit={(event) => submitBiddingHandler(event, 2)}
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
        {showPopup ? (
          <MyPopup errMsg={popupMsg} onClosePopup={onClosePopupBid}></MyPopup>
        ) : (
          ""
        )}
      </section>
    </Fragment>
  );
}

export default Bid;
