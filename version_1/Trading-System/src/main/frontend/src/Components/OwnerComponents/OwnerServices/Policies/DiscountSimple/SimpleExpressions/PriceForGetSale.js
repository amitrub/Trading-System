import React, { useState } from "react";
import createApiClientHttp from "../../../../../../ApiClientHttp";
import "../../../../../../Design/grid.css";
import "../../../../../../Design/style.css";
import MyPopup from "../../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function PriceForGetSale(props) {
  const [nodeID, setNodeID] = useState([]);
  const [price, setPrice] = useState("");
  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function submitPriceForGetSale(event) {
    event.preventDefault();
    console.log("submitPriceForGetSale");

    const insertNodeResponse = await apiHttp.AddNodeToBuildingTree(
      props.connID,
      props.userID,
      props.storeID,
      props.mode, //Discount Policy
      props.type,
      parseInt(nodeID),
      -1, // quantity,
      -1, //productID,
      -1, //maxQuantity,
      "-1", //category,
      -1, //numOfProductsForSale,
      parseInt(price), //priceForSale,
      -1, //quantityForSale,
      -1 //discount,
    );
    if (insertNodeResponse) {
      setPopupMsg(insertNodeResponse.message);
      setShowPopUp(true);
    }
    if (insertNodeResponse.isErr) {
      console.log(insertNodeResponse.message);
    }

    props.onRefresh();
  }

  function onClosePopup() {
    setShowPopUp(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h3>Insert Price-For-Get-Sale node (Expression)</h3>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitPriceForGetSale}
          >
            {/* Father Node ID */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Father Node ID</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="NodeID"
                  id="NodeID"
                  required
                  onChange={(e) => setNodeID(e.target.value)}
                  placeholder={"Simple Discount!!!"}
                />
              </div>
            </div>

            {/* PriceForGetSale */}
            <div className=" row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Price For Get Sale</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={(e) => setPrice(e.target.value)}
                  //   placeholder={"%"}
                />
              </div>
            </div>

            {/* End Of Form */}
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Insert Node" />
              </div>
            </div>
          </form>
        </div>
      </div>
      {showPopUp ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default PriceForGetSale;
