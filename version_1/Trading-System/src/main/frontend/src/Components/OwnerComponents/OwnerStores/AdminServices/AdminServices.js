import React, { useState } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";
import UserStoreHistory from "./UserStoreHistory";
import SystemDailyIncome from "./SystemDailyIncome";
import SubscriberWeek from "./SubscriberWeek";
import StoresWeek from "./StoresWeek";
import AllMoneyWeek from "./AllMoneyWeek";
import ShoppingHistoryWeek from "./ShoppingHistoryWeek";

const apiHttp = createApiClientHttp();

function AdminServices(props) {
  //Buttons
  const [showUserHistory, setShowUserHistory] = useState(false);
  const [showStoreHistory, setShowStoreHistory] = useState(false);
  const [showSystemDailyIncome, setShowSystemDailyIncome] = useState(false);
  const [showSubscriberWeek, setShowSubscriberWeek] = useState(false);
  const [showStoresWeek, setShowStoresWeek] = useState(false);
  const [showShoppingHistoryWeek, setShowShoppingHistoryWeek] = useState(false);
  const [showAllMoneyWeek, setShowAllMoneyWeek] = useState(false);

  // User History
  function hideUserHistoryHandler() {
    setShowUserHistory(false);
  }
  function showUserHistoryHandler() {
    setShowUserHistory(true);
    setShowStoreHistory(false);
    setShowSystemDailyIncome(false);
    setShowSubscriberWeek(false);
    setShowStoresWeek(false);
    setShowShoppingHistoryWeek(false);
    setShowAllMoneyWeek(false);
  }

  // Store History
  function hideStoreHistoryHandler() {
    setShowStoreHistory(false);
  }
  function showStoreHistoryHandler() {
    setShowStoreHistory(true);
    setShowUserHistory(false);
    setShowSystemDailyIncome(false);
    setShowSubscriberWeek(false);
    setShowStoresWeek(false);
    setShowShoppingHistoryWeek(false);
    setShowAllMoneyWeek(false);
  }

  // System Daily Income
  function hideSystemDailyIncomeHandler() {
    setShowSystemDailyIncome(false);
  }
  function showSystemDailyIncomeHandler() {
    setShowStoreHistory(false);
    setShowUserHistory(false);
    setShowSystemDailyIncome(true);
    setShowSubscriberWeek(false);
    setShowStoresWeek(false);
    setShowShoppingHistoryWeek(false);
    setShowAllMoneyWeek(false);
  }

  // Subscriber Week
  function hideSubscriberWeekHandler() {
    setShowSubscriberWeek(false);
  }
  function showSubscriberWeekHandler() {
    setShowStoreHistory(false);
    setShowUserHistory(false);
    setShowSystemDailyIncome(false);
    setShowSubscriberWeek(true);
    setShowStoresWeek(false);
    setShowShoppingHistoryWeek(false);
    setShowAllMoneyWeek(false);
  }

  // Stores Week
  function hideStoresWeeklyHandler() {
    setShowStoresWeek(false);
  }
  function showStoresWeeklyHandler() {
    setShowStoreHistory(false);
    setShowUserHistory(false);
    setShowSystemDailyIncome(false);
    setShowSubscriberWeek(false);
    setShowStoresWeek(true);
    setShowShoppingHistoryWeek(false);
    setShowAllMoneyWeek(false);
  }

  // Shopping History Week
  function hideShoppingHistoryWeeklyHandler() {
    setShowShoppingHistoryWeek(false);
  }
  function showShoppingHistoryWeeklyHandler() {
    setShowStoreHistory(false);
    setShowUserHistory(false);
    setShowSystemDailyIncome(false);
    setShowSubscriberWeek(false);
    setShowStoresWeek(false);
    setShowShoppingHistoryWeek(true);
    setShowAllMoneyWeek(false);
  }

  // All Money Week
  function hideAllMoneyWeeklyHandler() {
    setShowAllMoneyWeek(false);
  }
  function showAllMoneyWeeklyHandler() {
    setShowStoreHistory(false);
    setShowUserHistory(false);
    setShowSystemDailyIncome(false);
    setShowSubscriberWeek(false);
    setShowStoresWeek(false);
    setShowShoppingHistoryWeek(false);
    setShowAllMoneyWeek(true);
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
          {/* Show System Daily Income */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showSystemDailyIncome
                ? hideSystemDailyIncomeHandler
                : showSystemDailyIncomeHandler
            }
          >
            {showSystemDailyIncome ? "Hide" : "Sys Daily Income"}
          </button>
        </div>
        <div className="row">
          {/* Subscriber Week */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showSubscriberWeek
                ? hideSubscriberWeekHandler
                : showSubscriberWeekHandler
            }
          >
            {showSubscriberWeek ? "Hide" : "Weekly Subscribers"}
          </button>
          {/* Stores Week */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showStoresWeek ? hideStoresWeeklyHandler : showStoresWeeklyHandler
            }
          >
            {showStoresWeek ? "Hide" : "Weekly Stores"}
          </button>
          {/* Shopping History Week */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showShoppingHistoryWeek
                ? hideShoppingHistoryWeeklyHandler
                : showShoppingHistoryWeeklyHandler
            }
          >
            {showShoppingHistoryWeek ? "Hide" : "Weekly Shopping History"}
          </button>
          {/* All Money Week */}
          <button
            className="buttonus"
            value="load our stores..."
            onClick={
              showAllMoneyWeek
                ? hideAllMoneyWeeklyHandler
                : showAllMoneyWeeklyHandler
            }
          >
            {showAllMoneyWeek ? "Hide" : "Weekly Money"}
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
        {/* Show System Daily Income */}
        <div>
          {showSystemDailyIncome ? (
            <div className="row">
              {showSystemDailyIncome ? (
                <SystemDailyIncome
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  userID={props.userID}
                />
              ) : (
                ""
              )}
            </div>
          ) : (
            ""
          )}
        </div>
        {/* Subscriber Week */}
        <div>
          {showSubscriberWeek ? (
            <div className="row">
              {showSubscriberWeek ? (
                <SubscriberWeek
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  userID={props.userID}
                />
              ) : (
                ""
              )}
            </div>
          ) : (
            ""
          )}
        </div>
        {/* Stores Week */}
        <div>
          {showStoresWeek ? (
            <div className="row">
              {showStoresWeek ? (
                <StoresWeek
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  userID={props.userID}
                />
              ) : (
                ""
              )}
            </div>
          ) : (
            ""
          )}
        </div>
        {/* Shopping History Week */}
        <div>
          {showShoppingHistoryWeek ? (
            <div className="row">
              {showShoppingHistoryWeek ? (
                <ShoppingHistoryWeek
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  userID={props.userID}
                />
              ) : (
                ""
              )}
            </div>
          ) : (
            ""
          )}
        </div>
        {/* All Money Week */}
        <div>
          {showAllMoneyWeek ? (
            <div className="row">
              {showAllMoneyWeek ? (
                <AllMoneyWeek
                  refresh={props.refresh}
                  onRefresh={props.onRefresh}
                  connID={props.connID}
                  userID={props.userID}
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
