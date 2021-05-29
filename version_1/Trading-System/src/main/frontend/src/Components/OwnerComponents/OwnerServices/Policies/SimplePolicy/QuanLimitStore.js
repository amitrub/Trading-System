import React, { useState, useEffect } from "react";
// import createApiClientHttp from "../../../../../ApiClientHttp";
// import "../../../../../Design/grid.css";
// import "../../../../../Design/style.css";
// import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

// const apiHttp = createApiClientHttp();

function QuanLimitStore(props) {
  //   const [storeProducts, setProductsOfStore] = useState([]);
  const [maxQuantity, setMaxQuantity] = useState("");

  //   const [popupWriteComment, setPopupWriteComment] = useState(false);
  //   const [popupMsg, setPopupMsg] = useState("");

  //   const chosenStoreID = props.storeID;

  //   async function fetchStoreProducts() {
  //     if (chosenStoreID !== -1) {
  //       const productsOfStoresResponse = await apiHttp.ShowStoreProducts(
  //         chosenStoreID
  //       );

  //       if (productsOfStoresResponse.isErr) {
  //         console.log(productsOfStoresResponse.message);
  //       } else {
  //         setProductsOfStore(productsOfStoresResponse.returnObject.products);
  //       }
  //     }
  //   }

  //   useEffect(() => {
  //     fetchStoreProducts();
  //   }, [props.refresh]);

  async function submitLimitStoreHandler(event) {
    event.preventDefault();
    props.updateLimitStore(maxQuantity);
    props.onRefresh();
  }

  function updateMaxQuantity(event) {
    setMaxQuantity(event.target.value);
  }

  //   function onClosePopup() {
  //     setPopupWriteComment(false);
  //     props.onRefresh();
  //   }

  return (
    <div>
      <div>
        <h5>limit max quantity for store</h5>

        <div>
          <form
            method="post"
            className="contact-form"
            onSubmit={submitLimitStoreHandler}
          >
            {/* Max Quantity */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Max Quantity</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateMaxQuantity}
                  //   placeholder={"write your comment here"}
                />
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="ok" />
              </div>
            </div>
          </form>
        </div>
      </div>
      {/* {popupWriteComment ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )} */}
    </div>
  );
}

export default QuanLimitStore;
