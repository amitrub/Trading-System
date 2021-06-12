import React, { useState, useEffect } from "react";
import InsertBuyingCompositeNode from "../BuyingPolicies/InsertBuyingCompositeNode";
import InsertDiscountSimpleNode from "../SellingPolicies/InsertDiscountSimpleNode";
import JSONDisplay from "../JSONDisplay";
import createApiClientHttp from "../../../../../ApiClientHttp";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";
import AddExpressionToSale from "../DiscountSimple/AddExpressionToSale";

const apiHttp = createApiClientHttp();

function DiscountPolicy(props) {
  const [type, setType] = useState("choose type");
  const [fetchedExpression, setFetchedExpression] = useState({
    emptyTree: "your buying policy building tree is empty, start build it :)",
  });
  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function fetchBuildingExpression() {
    // console.log("fetch Building Expression");
    const discountPolicyResponse = await apiHttp.ShowDiscountPolicyBuildingTree(
      props.connID,
      props.userID,
      props.storeID
    );
    // console.log("fetch Building Expression");
    // console.log(discountPolicyResponse);

    if (discountPolicyResponse.isErr) {
      console.log(discountPolicyResponse.message);
    } else {
      if (discountPolicyResponse.returnObject.tree != null) {
        setFetchedExpression(discountPolicyResponse.returnObject.tree);
      }
    }
  }

  useEffect(() => {
    fetchBuildingExpression();
  }, [props.refresh]);

  async function updateDiscountPolicy() {
    console.log("updateDiscountPolicy");
    // const updateDiscountPolicyResponse = await apiHttp.AddDiscountPolicy(
    //   props.connID,
    //   props.userID,
    //   props.storeID,
    //   fetchedExpression
    // );
    // // console.log(updateDiscountPolicyResponse);

    // if (updateDiscountPolicyResponse) {
    //   setPopupMsg(updateDiscountPolicyResponse.message);
    //   setShowPopUp(true);
    // }
    // if (updateDiscountPolicyResponse.isErr) {
    //   console.log(updateDiscountPolicyResponse.message);
    // }
  }

  function onClosePopup() {
    setShowPopUp(false);
    props.onRefresh();
  }

  return (
    <section className="section-plans js--section-plans" id="store">
      <div>
        <div className="row">
          <h2>Discount Policy</h2>
        </div>

        <div>
          <h3>This is your discount Policy tree in building</h3>
        </div>

        <div>
          <h3>if your tree is ready, press below</h3>
          <button
            className="buttonus"
            value="load our stores..."
            onClick={updateDiscountPolicy}
          >
            Update
          </button>
        </div>

        <JSONDisplay value={fetchedExpression}></JSONDisplay>

        <AddExpressionToSale
          refresh={props.refresh}
          onRefresh={props.onRefresh}
          connID={props.connID}
          userID={props.userID}
          storeID={props.storeID}
          type={type}
          mode={1} //Discount Policy
        ></AddExpressionToSale>

        <div>
          <h4>Add the next node to your policy, Choose type and props:</h4>
          <p>---</p>
        </div>

        {/* Buying Policy Type */}
        <div className="row">
          <div className="col span-1-of-3">
            <label htmlFor="name">Add policy node</label>
          </div>
          <div className="col span-2-of-3">
            <select
              className="select"
              value={type}
              onChange={(e) => setType(e.target.value)}
              about="Show number of results:"
            >
              <option value={-1}> choose node type </option>
              <option value={"AddComposite"}> Add (Composite) </option>
              <option value={"MaxComposite"}> Max (Composite) </option>
              <option value={"XorComposite"}> Xor (Composite) </option>
              <option value={"StoreSale"}> Store Sale (Simple) </option>
              <option value={"ProductSale"}> Product Sale (Simple) </option>
              <option value={"CategorySale"}> Category Sale (Simple) </option>
            </select>
          </div>
        </div>
      </div>

      {/* Insert Conposite Node (And, Or, Cond)*/}
      <div className="row">
        {type === "AddComposite" ||
        type === "MaxComposite" ||
        type === "XorComposite" ? (
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
        {type === "StoreSale" ||
        type === "ProductSale" ||
        type === "CategorySale" ? (
          <InsertDiscountSimpleNode
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

export default DiscountPolicy;
