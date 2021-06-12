import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function QuanLimitProduct(props) {
  const [nodeID, setNodeID] = useState([]);
  const [storeProducts, setProductsOfStore] = useState([]); // QuantityLimitPerProduct
  const [productID, setProductID] = useState(-1); //QuantityLimitPerProduct
  const [productMaxQuant, setProductMaxQuant] = useState(""); //QuantityLimitPerProduct
  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");
  const chosenStoreID = props.storeID;

  async function fetchStoreProducts() {
    if (chosenStoreID !== -1) {
      const productsOfStoresResponse = await apiHttp.ShowStoreProducts(
        chosenStoreID
      );

      if (productsOfStoresResponse.isErr) {
        console.log(productsOfStoresResponse.message);
      } else {
        setProductsOfStore(productsOfStoresResponse.returnObject.products);
      }
    }
  }

  useEffect(() => {
    fetchStoreProducts();
  }, [props.refresh]);

  async function submitQuantityPerProductNode(event) {
    event.preventDefault();
    // console.log("submitQuantityPerProductNode");

    const insertNodeResponse = await apiHttp.AddNodeToBuildingTree(
      props.connID,
      props.userID,
      props.storeID,
      props.mode, //Buying Policy
      props.type,
      parseInt(nodeID),
      -1, // quantity,
      parseInt(productID), //productID,
      parseInt(productMaxQuant), //maxQuantity,
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
            <strong>Insert Quantity-Limit-For-Product node (Simple)</strong>
          </h3>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitQuantityPerProductNode}
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

            {/* Products */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Product</label>
              </div>
              <div className="col span-2-of-3">
                <select
                  className="select"
                  value={productID}
                  required
                  onChange={(e) => setProductID(e.target.value)}
                  about="Show number of results:"
                >
                  <option value={-1}> choose product </option>
                  {storeProducts.map((product) => (
                    <option value={product.productID}>
                      {product.productID}:{product.productName}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            {/* Max Quantity Product*/}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Max Quantity Per Product</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={(e) => setProductMaxQuant(e.target.value)}
                  //   placeholder={"write your comment here"}
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

export default QuanLimitProduct;
