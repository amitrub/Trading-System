import React, { useState } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";
import AllUsersHistories from "./AllUsersHistories/AllUsersHistories";

const apiHttp = createApiClientHttp();

function AdminServices(props) {
  //Buttons
  const [showUsersHistories, setShowUsersHistories] = useState(false);
  const [showAllStores, setShowAllStores] = useState(false);
  const [showUserHistory, setShowUserHistory] = useState(false);
  const [showStoreHistory, setShowStoreHistory] = useState(false);

  // Users Histories
  function hideUsersHistoriesHandler() {
    setShowUsersHistories(false);
  }
  function showUsersHistoriesHandler() {
    setShowUsersHistories(true);
  }

  // All Stores Btn
  function hideAllStoresHandler() {
    setShowAllStores(false);
  }
  function showAllStoresHandler() {
    setShowAllStores(true);
  }

  // User History
  function hideUserHistoryHandler() {
    setShowUserHistory(false);
  }
  function showUserHistoryHandler() {
    setShowUserHistory(true);
  }

  // Store History
  function hideStoreHistoryHandler() {
    setShowStoreHistory(false);
  }
  function showStoreHistoryHandler() {
    setShowStoreHistory(true);
  }

  return (
    <section className="section-form" id="owners">
      <div className="row">
        <h2>
          <strong>Administrator Permissions</strong>
        </h2>
      </div>

      <div>
        <div className="row">
          {/* All Users Histories Btn */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showUsersHistories
                ? hideUsersHistoriesHandler
                : showUsersHistoriesHandler
            }
          >
            {showUsersHistories ? "Hide" : "All User's History"}
          </button>
          {/* All Stores Btn */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showAllStores ? hideAllStoresHandler : showAllStoresHandler
            }
          >
            {showAllStores ? "Hide" : "All Stores"}
          </button>
          {/* User History Btn */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showUserHistory ? hideUserHistoryHandler : showUserHistoryHandler
            }
          >
            {showUserHistory ? "Hide" : "User history"}
          </button>
          {/* Store History Btn */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showStoreHistory
                ? hideStoreHistoryHandler
                : showStoreHistoryHandler
            }
          >
            {showStoreHistory ? "Hide" : "Store History"}
          </button>
        </div>

        {/* All Users Histories  */}
        <div>
          {showUsersHistories ? (
            <AllUsersHistories userID={props.userID} connID={props.connID} />
          ) : (
            ""
          )}
        </div>
        {/* All Stores Btn  */}
        <div>{showAllStores ? "Hide" : "All Stores"}</div>
        {/* User History Btn  */}
        <div>{showUserHistory ? "Hide" : "User history"}</div>
        {/* Store History Btn  */}
        <div>{showStoreHistory ? "Hide" : "Store History"}</div>
      </div>
    </section>
  );
}

export default AdminServices;
