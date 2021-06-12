import React, { useState } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";
import StoreSale from "../DiscountSimple/StoreSale";
import ProductSale from "../DiscountSimple/ProductSale";
import CategorySale from "../DiscountSimple/CategorySale";

const apiHttp = createApiClientHttp();

function InsertDiscountSimpleNode(props) {
  const simpleType = props.type;

  return (
    <section>
      <div>
        {/* ******************************************************************************* */}
        {/* StoreSale */}
        {simpleType === "StoreSale" ? (
          <StoreSale
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={simpleType}
            mode={1} //Discount Policy
          ></StoreSale>
        ) : (
          ""
        )}

        {/* ******************************************************************************* */}
        {/* ProductSale */}
        {simpleType === "ProductSale" ? (
          <ProductSale
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={simpleType}
            mode={1} //Discount Policy
          ></ProductSale>
        ) : (
          ""
        )}

        {/* ******************************************************************************* */}
        {/* CategorySale */}
        {simpleType === "CategorySale" ? (
          <CategorySale
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
            type={simpleType}
            mode={1} //Discount Policy
          ></CategorySale>
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default InsertDiscountSimpleNode;
