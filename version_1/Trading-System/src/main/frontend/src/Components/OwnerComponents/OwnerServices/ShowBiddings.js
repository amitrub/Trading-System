import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";
import Bid from "./Bid";

const apiHttp = createApiClientHttp();

function ShowBiddings(props) {
  const [biddingList, setBiddingList] = useState([]);

  async function fetchBiddings() {
    const biddingListResponse = await apiHttp.ShowAllBiddings(
      props.connID,
      props.userID,
      props.storeID
    );
    // console.log(biddingListResponse);

    if (biddingListResponse.isErr) {
      console.log(biddingListResponse.message);
    } else {
      setBiddingList(biddingListResponse.returnObject.Bids);
    }
  }

  useEffect(() => {
    fetchBiddings();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form">
        {biddingList && biddingList.length > 0 ? (
          biddingList.map((currBid, index) => (
            <li key={index} className="curr bid">
              <Bid
                refresh={props.refresh}
                onRefresh={props.onRefresh}
                connID={props.connID}
                userID={props.userID}
                userWhoOffer={currBid.userID}
                storeID={props.storeID}
                productID={currBid.productID}
                quantity={currBid.quantity}
                price={currBid.price}
              />
            </li>
          ))
        ) : (
          <p>No biddings waiting for you...</p>
        )}
      </section>
    </Fragment>
  );
}

export default ShowBiddings;
