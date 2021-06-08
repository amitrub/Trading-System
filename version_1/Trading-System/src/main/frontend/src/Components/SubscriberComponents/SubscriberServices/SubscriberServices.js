import React, { useState } from "react";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import OpenStore from "../OpenStore/OpenStore";
import WriteComment from "../WriteComment/WriteComment";
import UserHistory from "../UserHistory/UserHistory";
import AddBidding from "../AddBidding/AddBidding";

function SubscriberServices(props) {
  const [showOpenStore, setShowOpenStore] = useState(false);
  const [showWriteComment, setShowWriteComment] = useState(false);
  const [showUserHistory, setShowUserHistory] = useState(false);
  const [showAddBidding, setShowAddBidding] = useState(false);

  //Open Store Btn
  function showOpenStoreHandler() {
    setShowOpenStore(true);
    setShowWriteComment(false);
    setShowUserHistory(false);
    setShowAddBidding(false);

    props.onRefresh();
  }
  function hideOpenStoreHandler() {
    setShowOpenStore(false);
    props.onRefresh();
  }

  //Write Comment Btn
  function showWriteCommentHandler() {
    setShowOpenStore(false);
    setShowWriteComment(true);
    setShowUserHistory(false);
    setShowAddBidding(false);

    props.onRefresh();
  }
  function hideWriteCommentHandler() {
    setShowWriteComment(false);
    props.onRefresh();
  }

  //User History Btn
  function showUserHistoryHandler() {
    setShowOpenStore(false);
    setShowWriteComment(false);
    setShowUserHistory(true);
    setShowAddBidding(false);

    props.onRefresh();
  }
  function hideUserHistoryHandler() {
    setShowUserHistory(false);
    props.onRefresh();
  }

  //Add Bidding Btn
  function showAddBiddingHandler() {
    setShowOpenStore(false);
    setShowWriteComment(false);
    setShowUserHistory(false);
    setShowAddBidding(true);

    props.onRefresh();
  }
  function hideAddBiddingHandler() {
    setShowAddBidding(false);
    props.onRefresh();
  }

  return (
    <section className="section-plans js--section-plans" id="store">
      <div className="row">
        {/* Open Store Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showOpenStore ? hideOpenStoreHandler : showOpenStoreHandler}
        >
          {showOpenStore ? "Hide" : "Open Store"}
        </button>
        {/* Write Comment Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showWriteComment ? hideWriteCommentHandler : showWriteCommentHandler
          }
        >
          {showWriteComment ? "Hide" : "Write Comment"}
        </button>
        {/* User History Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showUserHistory ? hideUserHistoryHandler : showUserHistoryHandler
          }
        >
          {showUserHistory ? "Hide" : "Purchase History"}
        </button>

        {/* Add Bidding Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showAddBidding ? hideAddBiddingHandler : showAddBiddingHandler
          }
        >
          {showAddBidding ? "Hide" : "Add New Bidding"}
        </button>
      </div>

      {/* Open Store */}
      <div className="row">
        {showOpenStore ? (
          <OpenStore
            connID={props.connID}
            userID={props.userID}
            username={props.username}
            onRefresh={props.onRefresh}
          />
        ) : (
          ""
        )}
      </div>

      {/* Write Comment */}
      <div className="row">
        {showWriteComment ? (
          <WriteComment
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
          />
        ) : (
          ""
        )}
      </div>

      {/* UserHistory */}
      <div className="row">
        {showUserHistory ? (
          <UserHistory
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            username={props.username}
          />
        ) : (
          ""
        )}
      </div>

      {/* AddBiddign */}
      <div className="row">
        {showAddBidding ? (
          <AddBidding
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
          />
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default SubscriberServices;
