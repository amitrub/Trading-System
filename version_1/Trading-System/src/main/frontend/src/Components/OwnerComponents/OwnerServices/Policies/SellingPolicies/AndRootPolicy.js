import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function AndPolicyRoot(props) {
  const [storeProducts, setProductsOfStore] = useState([]);
  const [productId, setProductId] = useState("");
  const [quantityForSale, setQuantityForSale] = useState("");
  const [numOfProductsForSale, setNumOfProductsForSale] = useState("");
  const [priceForSale, setPriceForSale] = useState("");

  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  function updateNumOfProductsForSale(event) {
    setNumOfProductsForSale(event.target.value);
    props.onRefresh();
  }
  function updatePriceForSale(event) {
    setPriceForSale(event.target.value);
    props.onRefresh();
  }

  function updateProductId(event) {
    setProductId(event.target.value);
    props.onRefresh();
  }
  function updateQuantityForSale(event) {
    setQuantityForSale(event.target.value);
    props.onRefresh();
  }

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
  async function submitAndRootPolicyHandler(event) {
    event.preventDefault();

    const expression = makeJSONAndPolicy(
      numOfProductsForSale,
      priceForSale,
      productId,
      quantityForSale
    );
    // console.log(expression);
    const policyResponse = await apiHttp.AddDiscountPolicy(
      props.connID,
      props.userID,
      props.storeID,
      expression
    );
    if (policyResponse) {
      setPopupMsg(policyResponse.message);
      setShowPopUp(true);
    }
    if (policyResponse.isErr) {
      console.log(policyResponse.message);
    }

    props.onRefresh();
  }

  function makeJSONAndPolicy(a, b, c, d) {
    let expression = {
      AndComposite: {
        NumOfProductsForGetSale: {
          numOfProductsForSale: parseInt(a),
        },

        PriceForGetSale: {
          priceForSale: parseInt(b),
        },

        QuantityForGetSale: {
          productID: parseInt(c),
          quantityForSale: parseInt(d),
        },
      },
    };

    return expression;
  }

  function onClosePopup() {
    setShowPopUp(false);
    props.onRefresh();
  }

  useEffect(() => {
    fetchStoreProducts();
  }, [props.refresh]);

  return (
    <section>
      <div>
        <div className="row">
          <h2>And Tree Policy</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitAndRootPolicyHandler}
          >
            <h5>Num Of Products For Get Sale</h5>
            {/* Num Of Products For Get Sale */}
            <div className=" row">
              <div className="col span-1-of-3">
                <label htmlFor="name">num Of Products For Sale</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateNumOfProductsForSale}
                  //   placeholder={"write your comment here"}
                />
              </div>
            </div>
            <h5>Price For Get Sale</h5>
            {/* Price For Get Sale*/}
            <div className=" row">
              <div className="col span-1-of-3">
                <label htmlFor="name">price For Sale</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updatePriceForSale}
                  //   placeholder={"write your comment here"}
                />
              </div>
            </div>

            <h5>Quantity For Get Sale</h5>
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

            {/* Quantity For Sale */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Quantity For Sale</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateQuantityForSale}
                  //   placeholder={"write your comment here"}
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Submit And Policy" />
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

export default AndPolicyRoot;
