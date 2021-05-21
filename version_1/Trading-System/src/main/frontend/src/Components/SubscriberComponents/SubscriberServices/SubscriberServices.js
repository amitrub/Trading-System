import React, { useState } from "react";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import OpenStore from "../OpenStore/OpenStore";
import WriteComment from "../WriteComment/WriteComment";
import UserHistory from "../UserHistory/UserHistory";

function SubscriberServices(props) {
  const [showOpenStore, setShowOpenStore] = useState(false);
  const [showWriteComment, setShowWriteComment] = useState(false);
  const [showUserHistory, setShowUserHistory] = useState(false);

  //Open Store Btn
  function showOpenStoreHandler() {
    setShowOpenStore(true);
    setShowWriteComment(false);
    setShowUserHistory(false);

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

    props.onRefresh();
  }
  function hideUserHistoryHandler() {
    setShowUserHistory(false);
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
    </section>
  );
}

export default SubscriberServices;
