import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import PurchaseHistory from "../../SubscriberComponents/UserHistory/PurchaseHistory";

const apiHttp = createApiClientHttp();

function ShowStoreHistory(props) {
  const [storeHistory, setStoreHistory] = useState([]);

  async function fetchStoreHistory() {
    const storeHistoryResponse = await apiHttp.OwnerStoreHistory(
      props.connID,
      props.userID,
      props.storeID
    );
    // console.log(storeHistoryResponse);

    if (storeHistoryResponse.isErr) {
      console.log(storeHistoryResponse.message);
    } else {
      setStoreHistory(storeHistoryResponse.returnObject.history);
    }
  }

  useEffect(() => {
    fetchStoreHistory();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form">
        {storeHistory && storeHistory.length > 0 ? (
          storeHistory.map((currHistory, index) => (
            <li key={index} className="curr bid">
              <PurchaseHistory
                onRefresh={props.onRefresh}
                connID={props.connID}
                currHistory={currHistory}
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

export default ShowStoreHistory;
