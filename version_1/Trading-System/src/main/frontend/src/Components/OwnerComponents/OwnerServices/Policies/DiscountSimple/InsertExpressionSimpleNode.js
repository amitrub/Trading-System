import React, { useState } from "react";
import { parse } from "uuid";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import NumOfProductsForGetSale from "../DiscountSimple/SimpleExpressions/NumOfProductsForGetSale";
import PriceForGetSale from "../DiscountSimple/SimpleExpressions/PriceForGetSale";
import QuantityForGetSale from "../DiscountSimple/SimpleExpressions/QuantityForGetSale";

const apiHttp = createApiClientHttp();

function InsertExpressionSimpleNode(props) {
  const expType = props.type;

  return (
    <section>
      <div>
        {/* ******************************************************************************* */}
        {/* NumOfProductsForGetSale */}
        {expType === "NumOfProductsForGetSale" ? (
          <NumOfProductsForGetSale
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={expType}
            mode={1} //Discount Policy
          ></NumOfProductsForGetSale>
        ) : (
          ""
        )}

        {/* ******************************************************************************* */}
        {/* PriceForGetSale */}
        {expType === "PriceForGetSale" ? (
          <PriceForGetSale
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={expType}
            mode={1} //Discount Policy
          ></PriceForGetSale>
        ) : (
          ""
        )}

        {/* ******************************************************************************* */}
        {expType === "QuantityForGetSale" ? (
          <QuantityForGetSale
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={expType}
            mode={1} //Discount Policy
          ></QuantityForGetSale>
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default InsertExpressionSimpleNode;
