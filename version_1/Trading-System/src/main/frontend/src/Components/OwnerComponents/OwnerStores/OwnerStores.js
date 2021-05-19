import React, { useEffect, useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import OwnerStoreService from "./OwnerStoreService";
import ManagerStoreService from "./ManagerStoreService";

const apiHttp = createApiClientHttp();

function StoresOwner(props) {
  const [founderStores, setFounderStores] = useState([]);
  const [ownerStores, setOwnerStores] = useState([]);
  const [managerStores, setManagerStores] = useState([]);

  //Buttons
  const [showOwnerStores, setShowOwnerStores] = useState(false);
  const [showManagedStores, setShowManagedStores] = useState(false);
  const [showFoundedStores, setShowFoundedStores] = useState(false);
  const [showAdminStores, setShowAdminStores] = useState(false);

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
          <strong>Owners and so...</strong>
        </h2>
      </div>

      {props.userID === -1 ? (
        <div>
          <p>You are guest, login for owner permissions!</p>
        </div>
      ) : (
        <div>
          <div className="row">
            {/* Admins Btn */}
            <button
              className="buttonus"
              value="load our stores..."
              onClick={
                showAdminStores
                  ? () => setShowAdminStores(false)
                  : () => setShowAdminStores(true)
              }
            >
              {showAdminStores ? "Hide" : "Admins"}
            </button>
            {/* Founders Btn */}
            <button
              className="buttonus"
              value="load our stores..."
              onClick={
                showFoundedStores
                  ? () => setShowFoundedStores(false)
                  : () => setShowFoundedStores(true)
              }
            >
              {showFoundedStores ? "Hide" : "Founders"}
            </button>
            {/* Owners Btn */}
            <button
              className="buttonus"
              value="load our stores..."
              onClick={
                showOwnerStores
                  ? () => setShowOwnerStores(false)
                  : () => setShowOwnerStores(true)
              }
            >
              {showOwnerStores ? "Hide" : "Owners"}
            </button>
            {/* Managers Btn */}
            <button
              className="buttonus"
              value="load our stores..."
              onClick={
                showManagedStores
                  ? () => setShowManagedStores(false)
                  : () => setShowManagedStores(true)
              }
            >
              {showManagedStores ? "Hide" : "Managers"}
            </button>
          </div>

          {/* Founders */}
          {showFoundedStores ? (
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
          ) : (
            ""
          )}

          {/* Owners */}
          {showOwnerStores ? (
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
          ) : (
            ""
          )}

          {/* Managers */}
          {showManagedStores ? (
            <div>
              <h2>Managed stores</h2>
              {managerStores && managerStores.length !== 0 ? (
                managerStores.map((currStore, index) => (
                  <li key={index} className="curr store">
                    <ManagerStoreService
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
          ) : (
            ""
          )}
        </div>
      )}
    </section>
  );
}

export default StoresOwner;
