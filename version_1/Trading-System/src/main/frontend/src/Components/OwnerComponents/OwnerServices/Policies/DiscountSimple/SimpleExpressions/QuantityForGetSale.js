import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../../ApiClientHttp";
import "../../../../../../Design/grid.css";
import "../../../../../../Design/style.css";
import MyPopup from "../../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function QuantityForGetSale(props) {
  const [nodeID, setNodeID] = useState([]);
  const [storeProducts, setProductsOfStore] = useState([]);
  const [productID, setProductID] = useState(-1);
  const [quantity, setQuantity] = useState("");
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

  async function submitQuantityForGetSale(event) {
    event.preventDefault();
    // console.log("submitQuantityForGetSale");

    const insertNodeResponse = await apiHttp.AddNodeToBuildingTree(
      props.connID,
      props.userID,
      props.storeID,
      props.mode, //Discount Policy
      props.type,
      parseInt(nodeID),
      -1, // quantity,
      parseInt(productID), //productID,
      -1, //maxQuantity,
      "-1", //category,
      -1, //numOfProductsForSale,
      -1, //priceForSale,
      parseInt(quantity), //quantityForSale,
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
            <strong>Insert Quantity-For-Get-Sale node (Expression)</strong>
          </h3>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitQuantityForGetSale}
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

            {/* Quantity */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Quantity</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={(e) => setQuantity(e.target.value)}
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

export default QuantityForGetSale;
