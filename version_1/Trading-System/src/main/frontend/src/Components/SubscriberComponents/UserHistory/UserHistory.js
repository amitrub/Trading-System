import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import ProductInHistory from "./ProductInHistory";
import PurchaseHistory from "./PurchaseHistory";

const apiHttp = createApiClientHttp();

function UserHistory(props) {
  const [userHistory, setUserHistory] = useState([]);

  async function fetchUserHistory() {
    const historyResponse = await apiHttp.ShowUserHistory(
      props.connID,
      props.userID
    );
    console.log(historyResponse);

    if (historyResponse.isErr) {
      console.log(historyResponse.message);
    } else {
      setUserHistory(historyResponse.returnObject.history);
    }
  }

  useEffect(() => {
    fetchUserHistory();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="userhistory">
        {/* Purchase History header */}
        <div className="row">
          <h2>
            <strong>{props.username}'s Purchase History</strong>
          </h2>
        </div>

        <section className="section-form">
          {userHistory.length > 0
            ? userHistory.map((currHistory, index) => (
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
      </section>
    </Fragment>
  );
}

export default UserHistory;
