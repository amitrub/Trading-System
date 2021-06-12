import React, { useState, useEffect } from "react";
import InsertBuyingCompositeNode from "../BuyingPolicies/InsertBuyingCompositeNode";
import InsertExpressionSimpleNode from "./InsertExpressionSimpleNode";
import createApiClientHttp from "../../../../../ApiClientHttp";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function AddExpressionToSale(props) {
  const [type, setType] = useState("choose type");

  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  function onClosePopup() {
    setShowPopUp(false);
    props.onRefresh();
  }

  return (
    <section className="section-plans js--section-plans" id="store">
      <div>
        <div>
          <h4>
            Add expression node to sale (choose one of the sale node for valid
            tree!!!)
          </h4>
          <p>---</p>
        </div>

        {/* Add Expression To Sale */}
        <div className="row">
          <div className="col span-1-of-3">
            <label htmlFor="name">Add expression node to sale</label>
          </div>
          <div className="col span-2-of-3">
            <select
              className="select"
              value={type}
              onChange={(e) => setType(e.target.value)}
              about="Show number of results:"
            >
              <option value={-1}> choose expression type </option>
              <option value={"AndComposite"}> And (Composite) </option>
              <option value={"OrComposite"}> Or (Composite) </option>
              <option value={"CondComposite"} disabled>
                {" "}
                Condition (Composite){" "}
              </option>
              <option value={"NumOfProductsForGetSale"}>
                {" "}
                Num Of Products For Get Sale{" "}
              </option>
              <option value={"PriceForGetSale"}> Price For Get Sale </option>
              <option value={"QuantityForGetSale"}>
                {" "}
                Quantity For Get Sale{" "}
              </option>
            </select>
          </div>
        </div>
      </div>

      {/* Insert Conposite Node (And, Or, Cond)*/}
      <div className="row">
        {type === "AndComposite" ||
        type === "OrComposite" ||
        type === "CondComposite" ? (
          <InsertBuyingCompositeNode
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={type}
            mode={1} //Discount Policy
          />
        ) : (
          ""
        )}
      </div>

      {/* Insert Simple Node */}
      <div className="row">
        {type === "NumOfProductsForGetSale" ||
        type === "PriceForGetSale" ||
        type === "QuantityForGetSale" ? (
          <InsertExpressionSimpleNode
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={type}
          />
        ) : (
          ""
        )}
      </div>
      {showPopUp ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default AddExpressionToSale;
