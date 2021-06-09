import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function AddBidding(props) {
  const [stores, setStores] = useState([]);
  const [chosenStoreID, setChosenStoreID] = useState("");
  const [storeProducts, setProductsOfStore] = useState([]);
  const [productId, setProductId] = useState("");
  const [quantity, setQuantity] = useState("");
  const [newPriceOffer, setNewPriceOffer] = useState("");
  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  // const chosenStoreID = props.storeID;

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

      if (productsOfStoresResponse.isErr) {
        console.log(productsOfStoresResponse.message);
      } else {
        setProductsOfStore(productsOfStoresResponse.returnObject.products);
      }
    }
  }

  async function submitBiddingHandler(event) {
    event.preventDefault();

    const biddingResponse = await apiHttp.submissionBidding(
      props.connID,
      props.userID,
      parseInt(chosenStoreID),
      parseInt(productId),
      parseInt(quantity),
      parseInt(newPriceOffer)
    );

    if (biddingResponse) {
      setPopupMsg(biddingResponse.message);
      setShowPopUp(true);
    }
    if (biddingResponse.isErr) {
      console.log(biddingResponse.message);
    }

    props.onRefresh();
  }

  function updateStoreID(event) {
    setChosenStoreID(event.target.value);
    props.onRefresh();
  }

  function updateProductId(event) {
    setProductId(event.target.value);
    props.onRefresh();
  }

  function updateQuantity(event) {
    setQuantity(event.target.value);
    props.onRefresh();
  }

  function updateNewPriceOffer(event) {
    setNewPriceOffer(event.target.value);
    props.onRefresh();
  }

  function onClosePopup() {
    setShowPopUp(false);
    props.closeAddBidding(false);
    props.onRefresh();
  }

  useEffect(() => {
    fetchStores();
    fetchStoreProducts();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="biddings">
        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitBiddingHandler}
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
                  onChange={updateStoreID}
                  about="Show number of results:"
                >
                  <option value={-1}> choose store </option>
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
                  value={productId}
                  required
                  onChange={updateProductId}
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

            {/* quantity */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Quantity</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Quantity"
                  id="Quantity"
                  required
                  onChange={updateQuantity}
                  placeholder={"type quantity"}
                />
              </div>
            </div>

            {/* castumer offer */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">New price offer</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Price"
                  id="Price"
                  required
                  onChange={updateNewPriceOffer}
                  placeholder={"offer new price"}
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="offer new price!" />
              </div>
            </div>
          </form>
        </div>

        {showPopUp ? (
          <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
        ) : (
          ""
        )}
      </section>
    </Fragment>
  );
}

export default AddBidding;
