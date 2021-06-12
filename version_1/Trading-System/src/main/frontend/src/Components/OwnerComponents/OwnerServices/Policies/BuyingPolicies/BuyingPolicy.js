import React, { useState, useEffect } from "react";
import InsertBuyingCompositeNode from "./InsertBuyingCompositeNode";
import InsertBuyingSimpleNode from "./InsertBuyingSimpleNode";
import JSONDisplay from "../JSONDisplay";
import createApiClientHttp from "../../../../../ApiClientHttp";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function BuyingPolicy(props) {
  const [type, setType] = useState("choose type");
  const [fetchedExpression, setFetchedExpression] = useState({
    emptyTree: "your buying policy building tree is empty, start build it :)",
  });
  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function fetchBuildingExpression() {
    // console.log("fetch Building Expression");
    const buyingPolicyResponse = await apiHttp.ShowBuyingPolicyBuildingTree(
      props.connID,
      props.userID,
      props.storeID
    );
    console.log("fetch Building Expression");
    console.log(buyingPolicyResponse);

    if (buyingPolicyResponse.isErr) {
      console.log(buyingPolicyResponse.message);
    } else {
      if (buyingPolicyResponse.returnObject.tree != null) {
        setFetchedExpression(buyingPolicyResponse.returnObject.tree);
      }
    }
  }

  useEffect(() => {
    fetchBuildingExpression();
  }, [props.refresh]);

  async function updateBuyingPolicy() {
    console.log("updateBuyingPolicy");
    const updateBuyingPolicyResponse = await apiHttp.AddBuyingPolicy(
      props.connID,
      props.userID,
      props.storeID,
      fetchedExpression
    );
    // console.log(updateBuyingPolicyResponse);

    if (updateBuyingPolicyResponse) {
      setPopupMsg(updateBuyingPolicyResponse.message);
      setShowPopUp(true);
    }
    if (updateBuyingPolicyResponse.isErr) {
      console.log(updateBuyingPolicyResponse.message);
    }
  }

  function onClosePopup() {
    setShowPopUp(false);
    props.onRefresh();
  }

  return (
    <section className="section-plans js--section-plans" id="store">
      <div>
        <div className="row">
          <h2>Buying Policy</h2>
        </div>

        <div>
          <h3>This is your buying Policy tree in building</h3>
        </div>

        <div>
          <h3>if your tree is ready, press below</h3>
          <button
            className="buttonus"
            value="load our stores..."
            onClick={updateBuyingPolicy}
          >
            Update
          </button>
        </div>

        <JSONDisplay value={fetchedExpression}></JSONDisplay>

        <div>
          <h4>
            Add your next node to the tree by choose his type, his father node
            ID, and his properties
          </h4>
          <h4>Choose type for your new node:</h4>
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
              <option value={"AndComposite"}> And (Composite) </option>
              <option value={"OrComposite"}> Or (Composite) </option>
              <option value={"CondComposite"} disabled>
                {" "}
                Condition (Composite){" "}
              </option>
              <option value={"QuantityLimitForProduct"}>
                {" "}
                Quantity Limit For Product (Simple){" "}
              </option>
              <option value={"QuantityLimitForCategory"}>
                {" "}
                Quantity Limit For Category (Simple){" "}
              </option>
              <option value={"QuantityLimitForStore"}>
                {" "}
                Quantity Limit For Store (Simple){" "}
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
            mode={2} //Buying Policy
          />
        ) : (
          ""
        )}
      </div>

      {/* Insert Simple Node */}
      <div className="row">
        {type === "QuantityLimitForProduct" ||
        type === "QuantityLimitForCategory" ||
        type === "QuantityLimitForStore" ? (
          <InsertBuyingSimpleNode
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

export default BuyingPolicy;
