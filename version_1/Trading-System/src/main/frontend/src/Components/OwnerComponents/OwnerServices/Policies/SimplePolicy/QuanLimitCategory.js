import React, { useState, useEffect } from "react";
// import createApiClientHttp from "../../../../../ApiClientHttp";
// import "../../../../../Design/grid.css";
// import "../../../../../Design/style.css";
// import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

// const apiHttp = createApiClientHttp();

function QuanLimitCategory(props) {
  //   const [storeProducts, setProductsOfStore] = useState([]);
  const [category, setCategory] = useState("-1");
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

  async function submitLimitCategoryHandler(event) {
    event.preventDefault();
    props.updateLimitCategory(category, maxQuantity);
    props.onRefresh();
  }

  function updateCategory(event) {
    setCategory(event.target.value);
    // props.onRefresh();
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
        <h5>limit max quantity for category</h5>
        <div>
          <form
            method="post"
            className="contact-form"
            onSubmit={submitLimitCategoryHandler}
          >
            {/* Category */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Category</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="text"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateCategory}
                  placeholder={"type a category"}
                />
              </div>
            </div>

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

export default QuanLimitCategory;
