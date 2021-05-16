import React, { useEffect, useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import Store from "../../GuestComponents/Stores/Store";
import OwnerStoreService from "./OwnerStoreService";

const apiHttp = createApiClientHttp();

function StoresOwner(props) {
  const [founderStores, setFounderStores] = useState([]);
  const [ownerStores, setOwnerStores] = useState([]);
  const [managerStores, setManagerStores] = useState([]);
  const [showStores, setShowStores] = useState(false);

  async function fetchFoundedStores() {
    const foundedStoresResponse = await apiHttp.ShowAllFoundedStores(
      props.connID,
      props.userID
    );
    // console.log(foundedStoresResponse);

    if (foundedStoresResponse.isErr) {
      // console.log(foundedStoresResponse.message);
    } else {
      setFounderStores(foundedStoresResponse.returnObject.founderStores);
    }
  }

  async function fetchOwnerStores() {
    const ownerStoresResponse = await apiHttp.ShowAllOwnedStores(
      props.connID,
      props.userID
    );
    // console.log(ownerStoresResponse);

    if (ownerStoresResponse.isErr) {
      // console.log(ownerStoresResponse.message);
    } else {
      setOwnerStores(ownerStoresResponse.returnObject.ownerStores);
    }
  }

  async function fetchManagedStores() {
    const managerStoresResponse = await apiHttp.ShowAllManagedStores(
      props.connID,
      props.userID
    );
    // console.log(managerStoresResponse);

    if (managerStoresResponse.isErr) {
      // console.log(managerStoresResponse.message);
    } else {
      setManagerStores(managerStoresResponse.returnObject.managerStores);
    }
  }

  function submitShowStores() {
    // console.log("get Stores");
    setShowStores(true);
    props.onRefresh();
  }

  function submitCloseStores() {
    // console.log("close Stores");
    setShowStores(false);
  }

  useEffect(() => {
    fetchFoundedStores();
    fetchOwnerStores();
    fetchManagedStores();
    // console.log("useEffectStores");
  }, [props.refresh]);

  return (
    <section className="section-form" id="owners">
      <div className="row">
        <h2>
          <strong>Owners mode</strong>
        </h2>
      </div>

      {props.userID === -1 ? (
        <div>
          <p>You are guest, login for owner permissions!</p>
        </div>
      ) : (
        <div>
          <h2>
            <strong>My Stores</strong>
          </h2>
          <button
            className="buttonus"
            value="load our stores..."
            onClick={showStores ? submitCloseStores : submitShowStores}
          >
            {showStores ? "Hide Your Stores" : "Show All Your Stores"}
          </button>

          {showStores ? (
            <div>
              {
                <div>
                  <h2>Founded stores</h2>
                  {founderStores && founderStores.length !== 0 ? (
                    founderStores.map((currStore, index) => (
                      <li key={index} className="curr store">
                        <OwnerStoreService
                          refresh={props.refresh}
                          onRefresh={props.onRefresh}
                          connID={props.connID}
                          userID={props.userID}
                          currStore={currStore}
                        />
                      </li>
                    ))
                  ) : (
                    <p>you are not a founder</p>
                  )}
                </div>
              }

              {
                <div>
                  <h2>Owner stores</h2>
                  {ownerStores && ownerStores.length !== 0 ? (
                    ownerStores.map((currStore, index) => (
                      <li key={index} className="curr store">
                        <OwnerStoreService
                          refresh={props.refresh}
                          onRefresh={props.onRefresh}
                          connID={props.connID}
                          userID={props.userID}
                          currStore={currStore}
                        />
                      </li>
                    ))
                  ) : (
                    <p>you are not a owner</p>
                  )}
                </div>
              }

              {
                <div>
                  <h2>Managed stores</h2>
                  {managerStores && managerStores.length !== 0 ? (
                    managerStores.map((currStore, index) => (
                      <li key={index} className="curr store">
                        <OwnerStoreService
                          refresh={props.refresh}
                          onRefresh={props.onRefresh}
                          connID={props.connID}
                          userID={props.userID}
                          currStore={currStore}
                        />
                      </li>
                    ))
                  ) : (
                    <p>you are not a manager</p>
                  )}
                </div>
              }
            </div>
          ) : (
            ""
          )}
        </div>
      )}
    </section>
  );
}

export default StoresOwner;
