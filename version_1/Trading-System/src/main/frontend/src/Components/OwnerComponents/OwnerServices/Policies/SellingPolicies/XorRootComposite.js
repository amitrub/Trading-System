import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function XorRootComposite(props) {
  const [storeProducts, setProductsOfStore] = useState([]);
  const [storeSaleDiscount, setStoreSaleDiscount] = useState("");
  const [storeSaleExp, setStoreSaleExp] = useState("");
  const [productId, setProductId] = useState("");

  const [productDiscount, setProductDiscount] = useState("");
  const [productExp, setProductExp] = useState("");
  const [category, setCategory] = useState("");
  const [categoryDiscount, setCategoryDiscount] = useState("");
  const [categoryExp, setCategoryExp] = useState("");

  const [showPopUp, setShowPopUp] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  function updateStoreSaleDiscount(event) {
    setStoreSaleDiscount(event.target.value);
    props.onRefresh();
  }
  function updateStoreSaleExp(event) {
    setStoreSaleExp(event.target.value);
    props.onRefresh();
  }
  function updateProductId(event) {
    setProductId(event.target.value);
    props.onRefresh();
  }
  function updateProductDiscount(event) {
    setProductDiscount(event.target.value);
    props.onRefresh();
  }
  function updateProductExp(event) {
    setProductExp(event.target.value);
    props.onRefresh();
  }
  function updateCategory(event) {
    setCategory(event.target.value);
    props.onRefresh();
  }
  function updateCategoryDiscount(event) {
    setCategoryDiscount(event.target.value);
    props.onRefresh();
  }
  function updateCategoryExp(event) {
    setCategoryExp(event.target.value);
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

    const expression = makeJSONAndPolicy();
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

  function makeJSONAndPolicy() {
    let expression = {
      XorComposite: {
        StoreSale: {
          discount: parseInt(storeSaleDiscount),
          expression: storeSaleExp,
        },

        ProductSale: {
          productID: parseInt(productId),
          discount: parseInt(productDiscount),
          expression: productExp,
        },

        CategorySale: {
          category: category,
          discount: parseInt(categoryDiscount),
          expression: categoryExp,
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
          <h2>Or Tree Policy</h2>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitAndRootPolicyHandler}
          >
            <h5>Store Sale</h5>
            {/* Store Sale*/}
            <div className=" row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Discount</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateStoreSaleDiscount}
                />
              </div>
            </div>
            <div className=" row">
              <div className="col span-1-of-3">
                <label htmlFor="name">
                  Expressin(1 from all the options exp)
                </label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateStoreSaleExp}
                />
              </div>
            </div>

            <h5>Product Sale</h5>
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

            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Discount</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateProductDiscount}
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">
                  Expressin(1 from all the options exp)
                </label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateProductExp}
                />
              </div>
            </div>

            <h5>Category Sale</h5>
            {/* Category Sale*/}
            <div className=" row">
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
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Discount</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateCategoryDiscount}
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">
                  Expressin(1 from all the options exp)
                </label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="Name"
                  id="Name"
                  required
                  onChange={updateCategoryExp}
                />
              </div>
            </div>

            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Submit Xor Policy" />
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

export default XorRootComposite;
