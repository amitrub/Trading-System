import React, { useState, useEffect } from "react";
// import createApiClientHttp from "../../../../../ApiClientHttp";
// import "../../../../../Design/grid.css";
// import "../../../../../Design/style.css";
// import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

// const apiHttp = createApiClientHttp();

function PolicyRoot(props) {
  const simplePoliciesList = [
    { id: 1, label: "limit quantity by product" },
    { id: 2, label: "limit quantity by category" },
    { id: 3, label: "limit quantity by store" },
  ];

  const [showByProduct, setShowByProduct] = useState(false);
  const [showByCategory, setShowByCategory] = useState(false);
  const [showByStore, setShowByStore] = useState(false);

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

  // useEffect(() => {
  //   fetchStoreProducts();
  // }, [props.refresh]);

  async function submitPolicyRootHandler(event) {
    event.preventDefault();
    // props.updateLimitProduct(productID, maxQuantity);
    props.onRefresh();
  }

  function updateSimplePolicy(event) {
    // setProductID(event.target.value);
    props.onRefresh();
  }

  //   function onClosePopup() {
  //     setPopupWriteComment(false);
  //     props.onRefresh();
  //   }

  return (
    <section>
      <div>
        <div className="row">
          <h5>time to build yout tree!</h5>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitPolicyRootHandler}
          >
            {/* right side */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">right leaf</label>
              </div>
              <div className="col span-2-of-3">
                <select
                  className="select"
                  // value={productID}
                  required
                  onChange={(e) => updateSimplePolicy(e)}
                  about="Show number of results:"
                >
                  <option value={-1} disabled>
                    {" "}
                    right side{" "}
                  </option>
                  {simplePoliciesList.map((row) => (
                    <option value={row.id}>{row.label}</option>
                  ))}
                </select>
              </div>
            </div>

            {/* left side */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">left leaf</label>
              </div>
              <div className="col span-2-of-3">
                <select
                  className="select"
                  // value={productID}
                  required
                  onChange={(e) => updateSimplePolicy(e)}
                  about="Show number of results:"
                >
                  <option value={-1} disabled>
                    {" "}
                    left side{" "}
                  </option>
                  {simplePoliciesList.map((row) => (
                    <option value={row.id}>{row.label}</option>
                  ))}
                </select>
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
    </section>
  );
}

export default PolicyRoot;
