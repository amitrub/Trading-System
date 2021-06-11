import React, { useState, useEffect } from "react";
import InsertBuyingCompositeNode from "./InsertBuyingCompositeNode";
import OrRootPolicy from "./OrRootPolicy";
import ConditionsPolicy from "./CondtionsRootPolicy";
import JSONDisplay from "../JSONDisplay";
import createApiClientHttp from "../../../../../ApiClientHttp";

const apiHttp = createApiClientHttp();

function BuyingPolicy(props) {
  const [type, setType] = useState("choose type");
  const [fetchedExpression, setFetchedExpression] = useState({
    empty: "empty",
  });

  async function fetchBuildingExpression() {
    console.log("fetch Building Expression");
    const buyingPolicyResponse = await apiHttp.ShowBuyingPolicyBuildingTree(
      props.connID,
      props.userID,
      props.storeID
    );
    console.log(buyingPolicyResponse);

    if (buyingPolicyResponse.isErr) {
      console.log(buyingPolicyResponse.message);
    } else {
      setFetchedExpression(buyingPolicyResponse.returnObject.tree);
    }
  }

  useEffect(() => {
    fetchBuildingExpression();
  }, [props.refresh]);

  return (
    <section className="section-plans js--section-plans" id="store">
      <div>
        <div className="row">
          <h2>Buying Policy</h2>
        </div>

        <div>
          <h3>This is your current policies tree:</h3>
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
              {/* TOOD: change map to workersResponse.workers */}
              <option value={-1} disabled>
                {" "}
                choose id{" "}
              </option>
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

        <div>
          <p>{type}</p>
        </div>
      </div>

      {/* And Policy */}
      <div className="row">
        {type === "AndComposite" ? (
          <InsertBuyingCompositeNode
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

      {/* Condtions Policy */}
      <div className="row">
        {type === "CondComposite" ? (
          <ConditionsPolicy
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
          />
        ) : (
          ""
        )}
      </div>

      {/* Or Policy */}
      <div className="row">
        {type === "OrComposite" ? (
          <OrRootPolicy
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.storeID}
          />
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default BuyingPolicy;
