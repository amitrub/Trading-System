import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function ShowBiddings(props) {
  const [biddingList, setBiddingList] = useState([]);
  const [userID, setUserID] = useState("");
  const [quantity, setQuantity] = useState("");
  const [productID, setProductID] = useState("");
  const [newPrice, setNewPrice] = useState("");
  //====
  const [status, setStatus] = useState("");

  async function fetchBiddings() {
    const biddingListResponse = await apiHttp.displayAllBiddings(
      props.connID,
      props.userID,
      props.storeID
    );
    // console.log(biddingListResponse);

    if (biddingListResponse.isErr) {
      console.log(biddingListResponse.message);
    } else {
      setBiddingList(biddingListResponse.returnObject.DailyIncome);
    }
  }

  function submitBiddingHandler(newPrice, newQuantity, status) {}

  useEffect(() => {
    //TODO: fix this: function and remove the //
    // fetchBiddings();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>
            The client {userID} offers the next bidding: buy {quantity} of
            product num {productID} in the price {newPrice} per unit{" "}
          </h5>
        </div>

        <div className="row">
          {/* Ignore */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={(e) => submitBiddingHandler(-1, -1, 0)}
          >
            Ignore
            {/* {showStore ? "Hide" : "Show products"} */}
          </button>

          {/* Aprrove */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={(e) => submitBiddingHandler(-1, -1, 1)}
          >
            Approve
            {/* {showStore ? "Hide" : "Show products"} */}
          </button>

          {/* New Offer */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={(e) => submitBiddingHandler(-1, -1, 1)}
          >
            New Offer
            {/* {showStore ? "Hide" : "Show products"} */}
          </button>
        </div>
      </section>
    </Fragment>
  );
}

export default ShowBiddings;
