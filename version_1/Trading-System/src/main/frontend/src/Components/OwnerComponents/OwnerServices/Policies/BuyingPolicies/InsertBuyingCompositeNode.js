import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function InsertBuyingCompositeNode(props) {
  const [nodeID, setNodeID] = useState([]);
  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function submitInsertBuyingCompositeNode(event) {
    event.preventDefault();
    // console.log("submitInsertBuyingCompositeNode");

    const insertNodeResponse = await apiHttp.AddNodeToBuildingTree(
      props.connID,
      props.userID,
      props.storeID,
      props.mode,
      props.type,
      parseInt(nodeID),
      -1, // quantity,
      -1, //productID,
      -1, //maxQuantity,
      "-1", //category,
      -1, //numOfProductsForSale,
      -1, //priceForSale,
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
          <h3>
            <strong>Insert And node (Composite)</strong>
          </h3>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitInsertBuyingCompositeNode}
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
                  placeholder={
                    "You need to choose where do you want to insert the And node to the tree"
                  }
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Insert Composite Node" />
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

export default InsertBuyingCompositeNode;
