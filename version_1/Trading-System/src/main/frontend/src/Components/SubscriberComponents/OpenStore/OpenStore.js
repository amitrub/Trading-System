import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function OpenStore(props) {
  const [showOpenStoreForm, setShowOpenStoreForm] = useState(false);
  const [storeName, setStoreName] = useState("");
  const [popupOpenStore, setPopupOpenStore] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  function hideForm() {
    setShowOpenStoreForm(false);
  }

  function showForm() {
    setShowOpenStoreForm(true);
  }

  async function submitOpenStoreHandler(event) {
    event.preventDefault();
    // console.log("openStore");

    const openStoreResponse = await apiHttp.AddStore(
      props.connID,
      props.userID,
      storeName
    );

    // console.log("openStore");
    // console.log(openStoreResponse);

    if (openStoreResponse) {
      setPopupMsg(openStoreResponse.message);
      setPopupOpenStore(true);
    }
    if (openStoreResponse.isErr) {
      console.log(openStoreResponse.message);
    }
    props.onRefresh();
    setShowOpenStoreForm(false);
    setStoreName("");
  }

  function updateStoreName(event) {
    setStoreName(event.target.value);
  }

  function onClosePopupOpenStore() {
    setPopupOpenStore(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <button
          className="buttonus"
          value="Checkout"
          onClick={showOpenStoreForm ? hideForm : showForm}
        >
          {showOpenStoreForm ? "Hide Open store" : "Open new store"}
        </button>
      </div>

      {showOpenStoreForm ? (
        <div>
          <div className="row">
            <h2>Open {props.username}'s new store</h2>
          </div>

          <div className="row">
            <form
              method="post"
              className="contact-form"
              onSubmit={submitOpenStoreHandler}
            >
              {/* input name */}
              <div className="row">
                <div className="col span-1-of-3">
                  <label htmlFor="name">Store Name</label>
                </div>
                <div className="col span-2-of-3">
                  <input
                    type="text"
                    name="Name"
                    id="Name"
                    required
                    onChange={updateStoreName}
                    placeholder={"choose uniqe name for your store..."}
                  />
                </div>
              </div>
              <div className="row">
                <div className="col span-1-of-3">
                  <label>&nbsp;</label>
                </div>
                <div className="col span-1-of-3">
                  <input type="submit" value="Open!" />
                </div>
              </div>
            </form>
          </div>
        </div>
      ) : (
        ""
      )}
      {popupOpenStore ? (
        <MyPopup
          errMsg={popupMsg}
          onClosePopup={onClosePopupOpenStore}
        ></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default OpenStore;
