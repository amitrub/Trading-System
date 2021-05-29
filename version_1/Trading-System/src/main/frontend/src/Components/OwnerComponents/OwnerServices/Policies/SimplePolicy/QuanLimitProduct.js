import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
// import "../../../../../Design/grid.css";
// import "../../../../../Design/style.css";
// import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function QuanLimitProduct(props) {
  const [storeProducts, setProductsOfStore] = useState([]);
  const [productID, setProductID] = useState(-1);
  const [maxQuantity, setMaxQuantity] = useState("");

  async function submitLimitProductHandler(event) {
    console.log("ffffffffff");
    // event.preventDefault();
    // console.log("productId" + productID);
    // console.log("maxQuantity" + maxQuantity);
    // props.updateLimitProduct(productID, maxQuantity);
    // props.onRefresh();
  }

  //   const [popupWriteComment, setPopupWriteComment] = useState(false);
  //   const [popupMsg, setPopupMsg] = useState("");

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

  function updateProductID(event) {
    console.log("product id " + event.target.value);
    setProductID(event.target.value);
    props.onRefresh();
  }

  function updateMaxQuantity(event) {
    console.log(" category " + event.target.value);
    setMaxQuantity(event.target.value);
    props.onRefresh();
  }

  //   function onClosePopup() {
  //     setPopupWriteComment(false);
  //     props.onRefresh();
  //   }

  return (
    <section>
      <div>
        <div>
          <h5>limit max quantity for product</h5>
        </div>

        <div>
          <form
            method="post"
            className="contact-form"
            onSubmit={submitLimitProductHandler}
          >
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
                  onChange={(e) => updateProductID(e)}
                  about="Show number of results:"
                >
                  <option value={-1} disabled>
                    {" "}
                    choose product{" "}
                  </option>
                  {storeProducts.map((product) => (
                    <option value={product.productID}>
                      {product.productID}:{product.productName}
                    </option>
                  ))}
                </select>
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
    </section>
  );
}

export default QuanLimitProduct;
