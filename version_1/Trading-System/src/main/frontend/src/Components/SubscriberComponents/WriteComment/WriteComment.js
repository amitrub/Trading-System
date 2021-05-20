import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function WriteComment(props) {
  const [stores, setStores] = useState([]);
  const [chosenStoreID, setChosenStore] = useState(-1);
  const [storeProducts, setProductsOfStore] = useState([]);
  const [productID, setProductID] = useState(-1);
  const [comment, setComment] = useState("");

  const [popupWriteComment, setPopupWriteComment] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function fetchStores() {
    const storesResponse = await apiHttp.ShowAllStores();
    // console.log(storesResponse);

    if (storesResponse.isErr) {
      console.log(storesResponse.message);
    } else {
      setStores(storesResponse.returnObject.stores);
    }
  }

  async function fetchStoreProducts() {
    if (chosenStoreID !== -1) {
      const productsOfStoresResponse = await apiHttp.ShowStoreProducts(
        chosenStoreID
      );
      //   console.log(productsOfStoresResponse);

      if (productsOfStoresResponse.isErr) {
        console.log(productsOfStoresResponse.message);
      } else {
        setProductsOfStore(productsOfStoresResponse.returnObject.products);
      }
    }
  }

  useEffect(() => {
    fetchStores();
    fetchStoreProducts();
  }, [props.refresh]);

  async function submitWriteCommentHandler(event) {
    event.preventDefault();
    // console.log("before writeComment");

    const writeCommentResponse = await apiHttp.WriteComment(
      props.connID,
      props.userID,
      chosenStoreID,
      productID,
      comment
    );

    // console.log("writeComment");
    console.log(writeCommentResponse);

    if (writeCommentResponse) {
      setPopupMsg(writeCommentResponse.message);
      setPopupWriteComment(true);
    }
    if (writeCommentResponse.isErr) {
      console.log(writeCommentResponse.message);
    }
    props.onRefresh();
  }

  function updateStoreID(event) {
    setChosenStore(event.target.value);
    props.onRefresh();
  }

  function updateProductID(event) {
    setProductID(event.target.value);
    props.onRefresh();
  }

  function updateComment(event) {
    setComment(event.target.value);
  }

  function onClosePopup() {
    setPopupWriteComment(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h2>Write your comment</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitWriteCommentHandler}
          >
            {/* Stores */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Store</label>
              </div>
              <div className="col span-2-of-3">
                <select
                  className="select"
                  value={chosenStoreID}
                  required
                  onChange={(e) => updateStoreID(e)}
                  about="Show number of results:"
                >
                  <option value={-1} disabled>
                    {" "}
                    choose store{" "}
                  </option>
                  {stores.map((store) => (
                    <option value={store.id}>
                      {store.id}:{store.name}
                    </option>
                  ))}
                </select>
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

            {/* Comment */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Comment</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="text"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateComment}
                  placeholder={"write your comment here"}
                />
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Submit!" />
              </div>
            </div>
          </form>
        </div>
      </div>
      {popupWriteComment ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default WriteComment;
