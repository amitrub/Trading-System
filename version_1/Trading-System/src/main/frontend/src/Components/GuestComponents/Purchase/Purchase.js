import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function Purchase(props) {
  const [showPurchaseForm, setShowPurchaseForm] = useState(false);
  const [guestName, setGuestName] = useState("");
  const [creditNumber, setCreditNumber] = useState("");
  const [month, setMonth] = useState("");
  const [year, setYear] = useState("");
  const [cvv, setCVV] = useState("");
  const [idNum, setIDNum] = useState("");
  const [phone, setPhone] = useState("");
  const [adrress, setAdrress] = useState("");
  const [city, setCity] = useState("");
  const [country, setCountry] = useState("");
  const [mailNum, setMailNum] = useState("");
  const [popupPurchase, setPopupPurchase] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  const [cartSize, setCartSize] = useState(0);
  const [cartSize2, setCartSize2] = useState(0);

  async function fetchShopingCart() {
    const showCartResponse = await apiHttp.ShowShoppingCart(props.connID);
    // console.log(showCartResponse);

    if (showCartResponse.isErr) {
      console.log(showCartResponse.message);
    } else {
      setCartSize(showCartResponse.returnObject.products.length);
    }
  }

  async function fetchShopingCartBid() {
    const showCartBidResponse = await apiHttp.ShowShoppingCartBid(props.connID);
    // console.log(showCartResponse);

    if (showCartBidResponse.isErr) {
      console.log(showCartBidResponse.message);
    } else {
      setCartSize2(showCartBidResponse.returnObject.products.length);
    }
  }

  useEffect(() => {
    fetchShopingCart();
    fetchShopingCartBid();
  }, [props.refresh]);

  function submitCheckoutHideForm() {
    setShowPurchaseForm(false);
  }

  function submitCheckoutShowForm() {
    setShowPurchaseForm(true);
  }

  async function submitPurchaseHandler(event) {
    event.preventDefault();
    console.log("purchase");
    let purchaseResponse = "";

    if (props.userID === -1) {
      purchaseResponse = await apiHttp.guestPurchase(
        props.connID,
        guestName,
        creditNumber,
        month,
        year,
        cvv,
        idNum,
        adrress,
        city,
        country,
        mailNum
      );
    } else {
      purchaseResponse = await apiHttp.subscriberPurchase(
        props.connID,
        props.userID,
        creditNumber,
        month,
        year,
        cvv,
        idNum,
        adrress,
        city,
        country,
        mailNum
      );
    }

    // console.log("purchase");
    console.log(purchaseResponse);

    if (purchaseResponse) {
      setPopupMsg(purchaseResponse.message);
      setPopupPurchase(true);
    }
    if (purchaseResponse.isErr) {
      console.log(purchaseResponse.message);
    }
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

  function updateMonth(event) {
    setMonth(event.target.value);
  }

  function updateYear(event) {
    setYear(event.target.value);
  }

  function updateCVV(event) {
    setCVV(event.target.value);
  }

  function updateIDNum(event) {
    setIDNum(event.target.value);
  }

  function updateCity(event) {
    setCity(event.target.value);
  }

  function updateCountry(event) {
    setCountry(event.target.value);
  }

  function updateMailNum(event) {
    setMailNum(event.target.value);
  }

  function onClosePopupPurchase() {
    setPopupPurchase(false);
    props.onRefresh();
  }

  function getTextInputComp(label, func, value, holder) {
    return (
      <div className="row">
        <div className="col span-1-of-3">
          <label htmlFor="name">{label}</label>
        </div>
        <div className="col span-2-of-3">
          <input
            type="text"
            name={label}
            id={label}
            required
            onChange={func}
            value={value}
            placeholder={holder}
          />
        </div>
      </div>
    );
  }

  return (
    <section>
      {cartSize !== 0 || cartSize2 !== 0 ? (
        <div>
          <div>
            <button
              className="buttonus"
              value="Checkout"
              onClick={
                showPurchaseForm
                  ? submitCheckoutHideForm
                  : submitCheckoutShowForm
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
                  {props.userID === -1
                    ? getTextInputComp(
                        "Full Name",
                        updateGusetName,
                        guestName,
                        "hi nice to meet you guestii"
                      )
                    : ""}
                  {/* input credit_number */}
                  {getTextInputComp(
                    "Credit Number",
                    updateCreditNumber,
                    creditNumber,
                    "enter your number without spaces and '-'"
                  )}
                  {/* input Month */}
                  {getTextInputComp(
                    "Month",
                    updateMonth,
                    month,
                    "enter expiration month, number from 1 to 12"
                  )}
                  {/* input Year */}
                  {getTextInputComp(
                    "Year",
                    updateYear,
                    year,
                    "enter expiration year"
                  )}
                  {/* input CVV */}
                  {getTextInputComp(
                    "CVV",
                    updateCVV,
                    cvv,
                    "enter 3 digits below card"
                  )}
                  {/* input ID Num */}
                  {getTextInputComp(
                    "ID Num",
                    updateIDNum,
                    idNum,
                    "enter your ID"
                  )}
                  {/* input adrress */}
                  {getTextInputComp(
                    "Adrress",
                    updateAdrress,
                    adrress,
                    "where you is your home?"
                  )}
                  {/* input City */}
                  {getTextInputComp(
                    "City",
                    updateCity,
                    city,
                    "where you live???"
                  )}
                  {/* input Country */}
                  {getTextInputComp(
                    "Country",
                    updateCountry,
                    country,
                    "where you live I ask youuu???"
                  )}
                  {/* input Mailbox */}
                  {getTextInputComp(
                    "Mailbox",
                    updateMailNum,
                    mailNum,
                    "ta doaar you know"
                  )}
                  {/* input phone_number */}
                  {getTextInputComp(
                    "Phone",
                    updatePhone,
                    phone,
                    "never mind never mind"
                  )}
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
        </div>
      ) : (
        ""
      )}
    </section>
  );
}

export default Purchase;
