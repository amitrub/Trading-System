import React, { useState, useEffect } from "react";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";
import MyPopup from "../../../OtherComponents/MyPopup/MyPopup";
import createApiClientHttp from "../../../../ApiClientHttp";
import PurchaseHistory from "../../../SubscriberComponents/UserHistory/PurchaseHistory";

const apiHttp = createApiClientHttp();

function UserStoreHistory(props) {
  const [id, setID] = useState(-1);
  const [showPopup, setShowPopup] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");
  const [users, setUsers] = useState([]);
  const [stores, setStores] = useState([]);
  const [history, setHistory] = useState([]);

  async function fetchUsers() {
    const usersResponse = await apiHttp.GetAllSubscribers(
      props.connID,
      props.userID
    );
    // console.log(usersResponse);

    if (usersResponse.isErr) {
      console.log(usersResponse.message);
    } else {
      setUsers(usersResponse.returnObject.subscribers);
    }
  }

  async function fetchStores() {
    const storesResponse = await apiHttp.ShowAllStores();
    // console.log(storesResponse);

    if (storesResponse.isErr) {
      console.log(storesResponse.message);
    } else {
      setStores(storesResponse.returnObject.stores);
    }
  }

  useEffect(() => {
    fetchUsers();
    fetchStores();
  }, [props.refresh]);

  async function submitHistoryHandler(event) {
    event.preventDefault();
    console.log("before submitHistoryHandler");

    const adminResponse = await props.action(props.connID, props.userID, id);

    console.log(adminResponse);

    if (adminResponse) {
      setPopupMsg(adminResponse.message);
      setShowPopup(true);
    }
    if (adminResponse.isErr) {
      console.log(adminResponse.message);
    }

    setHistory(adminResponse.returnObject.history);
    props.onRefresh();
  }

  function updateID(event) {
    setID(event.target.value);
  }

  function onClosePopup() {
    setShowPopup(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h2>{props.job}</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitHistoryHandler}
          >
            {/* ID */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">ID</label>
              </div>
              <div className="col span-2-of-3">
                <select
                  className="select"
                  value={id}
                  onChange={(e) => updateID(e)}
                  //   about="Show number of results:"
                >
                  <option value={-1} disabled>
                    ---
                  </option>
                  {props.label === "userHistory"
                    ? users.map((currUser) => (
                        <option value={currUser.userID}>
                          {currUser.userName}
                        </option>
                      ))
                    : props.label === "storeHistory"
                    ? stores.map((currStore) => (
                        <option value={currStore.id}>{currStore.name}</option>
                      ))
                    : ""}
                </select>
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              {props.label === "userHistory" ? (
                <div className="col span-1-of-3">
                  <input type="submit" value="Show user history" />
                </div>
              ) : props.label === "storeHistory" ? (
                <div className="col span-1-of-3">
                  <input type="submit" value="Show store history" />
                </div>
              ) : (
                ""
              )}
            </div>
          </form>
        </div>

        <section className="section-form">
          {history.length > 0
            ? history.map((currHistory, index) => (
                <li key={index} className="curr product">
                  <PurchaseHistory
                    onRefresh={props.onRefresh}
                    connID={props.connID}
                    currHistory={currHistory}
                  />
                </li>
              ))
            : "No history, Go Shop bitch!"}
        </section>
      </div>
      {showPopup ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default UserStoreHistory;
