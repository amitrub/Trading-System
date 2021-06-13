import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";

const apiHttp = createApiClientHttp();

function ShowStoreWorkers(props) {
  const [storeWorkers, setBiddingList] = useState([]);

  async function fetchStoreWorkers() {
    const storeWorkersResponse = await apiHttp.ShowStoreWorkers(
      props.connID,
      props.userID,
      props.storeID
    );
    console.log(storeWorkersResponse);

    if (storeWorkersResponse.isErr) {
      console.log(storeWorkersResponse.message);
    } else {
      setBiddingList(storeWorkersResponse.returnObject.workers);
    }
  }

  useEffect(() => {
    fetchStoreWorkers();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form">
        {storeWorkers && storeWorkers.length > 0 ? (
          storeWorkers.map((currWorker, index) => (
            <li key={index} className="curr bid">
              <p>{currWorker}</p>
              <p>-----</p>
            </li>
          ))
        ) : (
          <p>No biddings waiting for you...</p>
        )}
      </section>
    </Fragment>
  );
}

export default ShowStoreWorkers;
