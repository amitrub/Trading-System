import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import Product from "../../GuestComponents/Stores/Product";
import AddProduct from "../OwnerServices/AddProduct";

const apiHttp = createApiClientHttp();

function OwnerStoreService(props) {
  const [productsOfStore, setProductsOfStore] = useState([]);
  const [showStore, setShowStore] = useState(false);
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [showChangeQuantityProduct, setShowChangeQuantityProduct] = useState(
    false
  );
  const [showEditProduct, setShowEditProduct] = useState(false);

  const store = props.currStore;

  async function fetchStoreProducts() {
    const productsOfStoresResponse = await apiHttp.ShowStoreProducts(store.id);
    // console.log(productsOfStoresResponse);

    if (productsOfStoresResponse.isErr) {
      console.log(productsOfStoresResponse.message);
    } else {
      setProductsOfStore(productsOfStoresResponse.returnObject.products);
    }
  }

  useEffect(() => {
    fetchStoreProducts();
  }, [props.refresh]);

  //Show Product Btn
  function submitLoadProducts() {
    setShowStore(true);
    setShowAddProduct(false);
    props.onRefresh();
  }
  function sumbitHideProducts() {
    setShowStore(false);
    props.onRefresh();
  }

  //Add Product Btn
  function showAddProductsHandler() {
    setShowAddProduct(true);
    setShowStore(false);
    props.onRefresh();
  }
  function hideAddProductsHandler() {
    setShowAddProduct(false);
    props.onRefresh();
  }

  return (
    <section className="section-plans js--section-plans" id="store">
      <div className="row">
        <h2>
          <strong>{store.name}</strong> rate:{store.storeRate}
        </h2>
      </div>
      <div className="row">
        {/* Show Stores Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showStore ? sumbitHideProducts : submitLoadProducts}
        >
          {showStore ? "Hide" : "Show products"}
        </button>
        {/* Add Product Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showAddProduct ? hideAddProductsHandler : showAddProductsHandler
          }
        >
          {showAddProduct ? "Hide" : "Add products"}
        </button>
        {/* Change Quantity Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          //   onClick={
          //     showAddProduct ? hideAddProductsHandler : showAddProductsHandler
          //   }
        >
          {showChangeQuantityProduct ? "Hide" : "Change Quantity"}
        </button>
        {/* Edit product Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          //   onClick={
          //     showAddProduct ? hideAddProductsHandler : showAddProductsHandler
          //   }
        >
          {showEditProduct ? "Hide" : "Edit product"}
        </button>
      </div>

      <div className="row">
        {showStore ? (
          productsOfStore && productsOfStore.length === 0 ? (
            <p>"No products on your store..."</p>
          ) : (
            productsOfStore.map((currProduct, index) => (
              <div className="col span-1-of-4">
                <li key={index} className="curr product">
                  <Product
                    refresh={props.refresh}
                    onRefresh={props.onRefresh}
                    connID={props.connID}
                    currProduct={currProduct}
                  ></Product>
                </li>
              </div>
            ))
          )
        ) : (
          ""
        )}
      </div>

      <div className="row">
        {showAddProduct ? (
          <AddProduct
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.storeID}
            storeName={props.currStore.storeName}
          />
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default OwnerStoreService;
