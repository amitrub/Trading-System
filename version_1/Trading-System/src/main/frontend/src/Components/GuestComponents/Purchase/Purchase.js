import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function Purchase(props) {
  const [showPurchaseForm, setShowPurchaseForm] = useState(false);
  const [guestName, setGuestName] = useState("");
  const [creditNumber, setCreditNumber] = useState("");
  const [phone, setPhone] = useState("");
  const [adrress, setAdrress] = useState("");
  const [popupPurchase, setPopupPurchase] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  function submitCheckoutHideForm() {
    setShowPurchaseForm(false);
  }

  function submitCheckoutShowForm() {
    setShowPurchaseForm(true);
  }

  async function submitPurchaseHandler(event) {
    event.preventDefault();
    // console.log("purchase");
    let purchaseResponse = "";

    if (props.userID === -1) {
      purchaseResponse = await apiHttp.guestPurchase(
        props.connID,
        guestName,
        creditNumber,
        phone,
        adrress
      );
    } else {
      purchaseResponse = await apiHttp.subscriberPurchase(
        props.connID,
        props.userID,
        creditNumber,
        phone,
        adrress
      );
    }

    console.log("purchase");
    console.log(purchaseResponse);

    if (purchaseResponse) {
      setPopupMsg(purchaseResponse.message);
      setPopupPurchase(true);
    }
    if (purchaseResponse.isErr) {
      console.log(purchaseResponse.message);
    }
    props.onRefresh();
    setShowPurchaseForm(false);
    setGuestName("");
    setCreditNumber("");
    setPhone("");
    setAdrress("");
  }

  function updateGusetName(event) {
    setGuestName(event.target.value);
  }

  function updateCreditNumber(event) {
    setCreditNumber(event.target.value);
  }

  function updatePhone(event) {
    setPhone(event.target.value);
  }

  function updateAdrress(event) {
    setAdrress(event.target.value);
  }

  function onClosePopupPurchase() {
    setPopupPurchase(false);
  }

  return (
    <section>
      <div>
        <button
          className="buttonus"
          value="Checkout"
          onClick={
            showPurchaseForm ? submitCheckoutHideForm : submitCheckoutShowForm
          }
        >
          {showPurchaseForm ? "Hide Checkout" : "Checkout"}
        </button>
      </div>

      {showPurchaseForm ? (
        <div>
          {props.userID === -1 ? (
            <div className="row">
              <h2>Guest purchase details</h2>
            </div>
          ) : (
            <div className="row">
              <h2>Subscriber purchase details</h2>
            </div>
          )}

          <div className="row">
            <form
              method="post"
              className="contact-form"
              onSubmit={submitPurchaseHandler}
            >
              {/* input name */}
              {props.userID === -1 ? (
                <div className="row">
                  <div className="col span-1-of-3">
                    <label htmlFor="name">Full Name</label>
                  </div>
                  <div className="col span-2-of-3">
                    <input
                      type="text"
                      name="Name"
                      id="Name"
                      required
                      onChange={updateGusetName}
                      value={guestName}
                    />
                  </div>
                </div>
              ) : (
                ""
              )}
              {/* input credit_number */}
              <div className="row">
                <div className="col span-1-of-3">
                  <label htmlFor="name">Credit Number</label>
                </div>
                <div className="col span-2-of-3">
                  <input
                    type="text"
                    name="credit"
                    id="credit"
                    required
                    onChange={updateCreditNumber}
                    value={creditNumber}
                  />
                </div>
              </div>
              {/* input phone_number */}
              <div className="row">
                <div className="col span-1-of-3">
                  <label htmlFor="name">Phone</label>
                </div>
                <div className="col span-2-of-3">
                  <input
                    type="text"
                    name="phone"
                    id="phone"
                    required
                    onChange={updatePhone}
                    value={phone}
                  />
                </div>
              </div>
              {/* input adrress */}
              <div className="row">
                <div className="col span-1-of-3">
                  <label htmlFor="name">Adrress</label>
                </div>
                <div className="col span-2-of-3">
                  <input
                    type="text"
                    name="adrress"
                    id="adrress"
                    required
                    onChange={updateAdrress}
                    value={adrress}
                  />
                </div>
              </div>
              <div className="row">
                <div className="col span-1-of-3">
                  <label>&nbsp;</label>
                </div>
                <div className="col span-1-of-3">
                  <input type="submit" value="Purchase" />
                </div>
              </div>
            </form>
          </div>
        </div>
      ) : (
        ""
      )}
      {popupPurchase ? (
        <MyPopup
          errMsg={popupMsg}
          onClosePopup={onClosePopupPurchase}
        ></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default Purchase;
