import React, { useState } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function EditProduct(props) {
  const [productName, setProductName] = useState("");
  const [productID, setProductID] = useState(-1);
  const [category, setCategory] = useState("");
  const [quantity, setQuantity] = useState(-1);
  const [price, setPrice] = useState(-1);

  const [popupEditProduct, setPopupEditProduct] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function submitEditProductHandler(event) {
    event.preventDefault();
    // console.log("before EditProduct");

    const editProductResponse = await apiHttp.EditProduct(
      props.connID,
      props.userID,
      props.storeID,
      productID,
      productName,
      category,
      quantity,
      price
    );

    // console.log("EditProduct");
    // console.log(editProductResponse);

    if (editProductResponse) {
      setPopupMsg(editProductResponse.message);
      setPopupEditProduct(true);
    }
    if (editProductResponse.isErr) {
      console.log(editProductResponse.message);
    }
    props.onRefresh();
  }

  function updateProductName(event) {
    setProductName(event.target.value);
  }

  function updateProductID(event) {
    setProductID(event.target.value);
  }

  function updateCategory(event) {
    setCategory(event.target.value);
  }

  function updateQuantity(event) {
    setQuantity(event.target.value);
  }

  function updatePrice(event) {
    setPrice(event.target.value);
  }

  function onClosePopup() {
    setPopupEditProduct(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h2>Edit product</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitEditProductHandler}
          >
            {/* product  ID */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Current ID</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="id"
                  id="id"
                  required
                  onChange={updateProductID}
                  placeholder={"product id to edit"}
                />
              </div>
            </div>
            {/* product name */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">New name</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="text"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateProductName}
                  placeholder={"how you call to your product?"}
                />
              </div>
            </div>
            {/* category */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">New Category</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="text"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateCategory}
                  placeholder={"choose category"}
                />
              </div>
            </div>
            {/* Quantity */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">New Quantity</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Quantity"
                  placeholder="insert quantity"
                  min="1"
                  onChange={updateQuantity}
                />
              </div>
            </div>
            {/* Price */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">New Price</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Price"
                  placeholder="insert price"
                  min="1"
                  onChange={updatePrice}
                />
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Edit Product!" />
              </div>
            </div>
          </form>
        </div>
      </div>
      {popupEditProduct ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default EditProduct;
