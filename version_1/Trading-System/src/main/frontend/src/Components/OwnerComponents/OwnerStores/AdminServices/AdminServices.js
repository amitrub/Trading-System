import React, { useState } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";
import UserStoreHistory from "./UserStoreHistory";

const apiHttp = createApiClientHttp();

function AdminServices(props) {
  //Buttons
  const [showUserHistory, setShowUserHistory] = useState(false);
  const [showStoreHistory, setShowStoreHistory] = useState(false);

  // User History
  function hideUserHistoryHandler() {
    setShowUserHistory(false);
  }
  function showUserHistoryHandler() {
    setShowUserHistory(true);
    setShowStoreHistory(false);
  }

  // Store History
  function hideStoreHistoryHandler() {
    setShowStoreHistory(false);
  }
  function showStoreHistoryHandler() {
    setShowStoreHistory(true);
    setShowUserHistory(false);
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

        {/* User History */}
        <div>
          {showUserHistory ? (
            <div className="row">
              {showUserHistory ? (
                <UserStoreHistory
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  userID={props.userID}
                  job="User History"
                  label="userHistory"
                  action={apiHttp.AdminUserHistory}
                />
              ) : (
                ""
              )}
            </div>
          ) : (
            ""
          )}
        </div>
        {/* Store History */}
        <div>
          {showStoreHistory ? (
            <div className="row">
              {showStoreHistory ? (
                <UserStoreHistory
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  userID={props.userID}
                  job="Store History"
                  label="storeHistory"
                  action={apiHttp.AdminStoreHistory}
                />
              ) : (
                ""
              )}
            </div>
          ) : (
            ""
          )}
        </div>
      </div>
    </section>
  );
}

export default AdminServices;
