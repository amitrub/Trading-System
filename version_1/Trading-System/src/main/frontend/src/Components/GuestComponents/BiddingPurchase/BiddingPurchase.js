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

  useEffect(() => {
    //TODO: fix this: function and remove the //
    // fetchBiddings();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>bidding purchaseeeeeeeeeeee</h5>
        </div>
      </section>
    </Fragment>
  );
}

export default ShowBiddings;
