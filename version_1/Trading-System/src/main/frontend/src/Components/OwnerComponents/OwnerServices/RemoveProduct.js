import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function RemoveProduct(props) {
  const [productID, setProductID] = useState(-1);
  const [popupRemove, setPopupRemove] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function submitRemoveHandler(event) {
    event.preventDefault();
    // console.log("before submitRemoveHandler");

    const removeResponse = await apiHttp.RemoveProduct(
      props.connID,
      props.userID,
      props.storeID,
      productID
    );

    // console.log("removeResponse");
    // console.log(removeResponse);

    if (removeResponse) {
      setPopupMsg(removeResponse.message);
      setPopupRemove(true);
    }
    if (removeResponse.isErr) {
      console.log(removeResponse.message);
    }
    props.onRefresh();
  }

  function updateProductID(event) {
    setProductID(event.target.value);
  }

  function onClosePopup() {
    setPopupRemove(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h2>Remove product</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitRemoveHandler}
          >
            {/* product id */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Product ID</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="id"
                  id="id"
                  required
                  onChange={updateProductID}
                  placeholder={"choose prodID to add to"}
                />
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Remove Product!" />
              </div>
            </div>
          </form>
        </div>
      </div>
      {popupRemove ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default RemoveProduct;
