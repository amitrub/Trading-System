import React, { useState } from "react";
import { parse } from "uuid";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";
import QuanLimitProduct from "../BuyingSimple/QuanLimitProduct";
import QuanLimitCategory from "../BuyingSimple/QuanLimitCategory";
import QuanLimitStore from "../BuyingSimple/QuanLimitStore";

const apiHttp = createApiClientHttp();

function InsertBuyingSimpleNode(props) {
  const simpleType = props.type;
  const [nodeID, setNodeID] = useState([]);
  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  function onClosePopup() {
    setShowPopUp(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        {/* ******************************************************************************* */}
        {/* QuantityLimitForProduct */}
        {simpleType === "QuantityLimitForProduct" ? (
          <QuanLimitProduct
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={simpleType}
            mode={2} //Buying Policy
          ></QuanLimitProduct>
        ) : (
          ""
        )}

        {/* ******************************************************************************* */}
        {/* QuantityLimitForCategory */}
        {simpleType === "QuantityLimitForCategory" ? (
          <QuanLimitCategory
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={simpleType}
            mode={2} //Buying Policy
          ></QuanLimitCategory>
        ) : (
          ""
        )}

        {/* ******************************************************************************* */}
        {simpleType === "QuantityLimitForStore" ? (
          <QuanLimitStore
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={simpleType}
            mode={2} //Buying Policy
          ></QuanLimitStore>
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

export default InsertBuyingSimpleNode;
