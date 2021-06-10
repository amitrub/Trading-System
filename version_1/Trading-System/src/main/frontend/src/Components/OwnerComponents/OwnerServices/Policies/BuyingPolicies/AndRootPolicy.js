import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";
import JSONDisplay from "../JSONDisplay";

const apiHttp = createApiClientHttp();

function AndPolicyRoot(props) {
  const [storeProducts, setProductsOfStore] = useState([]);
  const [productId, setProductId] = useState("");
  const [productMaxQuant, setProductMaxQuant] = useState("");
  const [category, setCategory] = useState("");
  const [categoryMaxQuant, setCategoryMaxQuant] = useState("");
  const [storeMaxQuant, setStoreMaxQuant] = useState("");

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
  function updateStoreMaxQuant(event) {
    setStoreMaxQuant(event.target.value);
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
      productId,
      productMaxQuant,
      category,
      categoryMaxQuant,
      storeMaxQuant
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

  function makeJSONAndPolicy(a, b, c, d, e) {
    let expression = {
      AndComposite: {
        QuantityLimitForProduct: {
          maxQuantity: parseInt(b),
          productID: parseInt(a),
        },
        QuantityLimitForCategory: {
          maxQuantity: parseInt(d),
          category: c,
        },
        QuantityLimitForStore: {
          maxQuantity: parseInt(e),
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

  let testExp = {
    AndComposite: {
      NodeID: 1,
      QuantityLimitForProduct: {
        NodeID: 1.1,
        maxQuantity: 7,
        productID: 7,
      },
      QuantityLimitForCategory: {
        NodeID: 1.2,
        maxQuantity: 7,
        category: "c",
      },
      QuantityLimitForStore: {
        NodeID: 1.3,
        maxQuantity: 7,
      },
    },
  };

  return (
    <section>
      <JSONDisplay expression={testExp}></JSONDisplay>

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
            <h5>Limit by Product AND</h5>
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
            <h5>Limit by Category AND</h5>
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
            <h5>Limit by Store</h5>

            {/* Max Quantity Store*/}
            <div className=" row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Max Quantity Per Store</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateStoreMaxQuant}
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
