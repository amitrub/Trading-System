import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function CondtionsPolicyRoot(props) {
  const [storeProducts, setProductsOfStore] = useState([]);
  const [productId, setProductId] = useState("");
  const [productMaxQuant, setProductMaxQuant] = useState("");
  const [category, setCategory] = useState("");
  const [categoryMaxQuant, setCategoryMaxQuant] = useState("");

  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  function updateProductId(event) {
    setProductId(event.target.value);
    props.onRefresh();
  }
  function updateMaxQuant(event) {
    setProductMaxQuant(event.target.value);
    props.onRefresh();
  }
  function updateCategory(event) {
    setCategory(event.target.value);
    props.onRefresh();
  }
  function updateCategoryMacQuant(event) {
    setCategoryMaxQuant(event.target.value);
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
  async function submitCompositeRootPolicyHandler(event) {
    event.preventDefault();

    const expression = makeJSONAndPolicy(
      productId,
      productMaxQuant,
      category,
      categoryMaxQuant
    );
    // console.log(expression);
    const policyResponse = await apiHttp.AddBuyingPolicy(
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
      Conditioning: {
        cond: {
          QuantityLimitForProduct: {
            maxQuantity: parseInt(b),
            productID: parseInt(a),
          },
        },
        condIf: {
          QuantityLimitForCategory: {
            maxQuantity: parseInt(d),
            category: c,
          },
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
          <h2>Conditions Tree Policy</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitCompositeRootPolicyHandler}
          >
            <h5>cond: Quantity Limit For Product</h5>
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
                  onChange={updateMaxQuant}
                  //   placeholder={"write your comment here"}
                />
              </div>
            </div>

            <h5> cond if: Quantity Limit For Category</h5>

            {/* category */}
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
                  placeholder={"choose category"}
                />
              </div>
            </div>

            {/* Max Quantity */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Max Quantity Per Category</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateCategoryMacQuant}
                  //   placeholder={"write your comment here"}
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Submit Conditions Policy" />
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

export default CondtionsPolicyRoot;
