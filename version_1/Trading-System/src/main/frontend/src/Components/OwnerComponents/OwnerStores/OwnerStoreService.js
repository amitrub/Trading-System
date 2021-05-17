import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import OwnerProduct from "./OwnerProduct";
import AddProduct from "../OwnerServices/AddProduct";
import EditProduct from "../OwnerServices/EditProduct";
import RemoveProduct from "../OwnerServices/RemoveProduct";
import ChangeQuantityProduct from "../OwnerServices/ChangeQuantityProduct";

const apiHttp = createApiClientHttp();

function OwnerStoreService(props) {
  const [productsOfStore, setProductsOfStore] = useState([]);
  const [showStore, setShowStore] = useState(false);
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [showChangeQuantityProduct, setShowChangeQuantityProduct] = useState(
    false
  );
  const [showEditProduct, setShowEditProduct] = useState(false);
  const [showRemoveProduct, setShowRemoveProduct] = useState(false);

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
    setShowChangeQuantityProduct(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);

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
    setShowChangeQuantityProduct(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);

    props.onRefresh();
  }
  function hideAddProductsHandler() {
    setShowAddProduct(false);
    props.onRefresh();
  }

  //Change quantity Btn
  function showChangeQuantityHandler() {
    setShowChangeQuantityProduct(true);
    setShowAddProduct(false);
    setShowStore(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);

    props.onRefresh();
  }
  function hideChangeQuantityHandler() {
    setShowChangeQuantityProduct(false);
    props.onRefresh();
  }

  //Edit prod Btn
  function showEditProductHandler() {
    setShowEditProduct(true);
    setShowAddProduct(false);
    setShowStore(false);
    setShowChangeQuantityProduct(false);
    setShowRemoveProduct(false);

    props.onRefresh();
  }
  function hideEditProductHandler() {
    setShowEditProduct(false);
    props.onRefresh();
  }

  //Remove prod Btn
  function showRemoveProductHandler() {
    setShowRemoveProduct(true);
    setShowEditProduct(false);
    setShowAddProduct(false);
    setShowStore(false);
    setShowChangeQuantityProduct(false);

    props.onRefresh();
  }
  function hideRemoveProductHandler() {
    setShowRemoveProduct(false);
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
          onClick={
            showChangeQuantityProduct
              ? hideChangeQuantityHandler
              : showChangeQuantityHandler
          }
        >
          {showChangeQuantityProduct ? "Hide" : "Change Quantity"}
        </button>
        {/* Edit product Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showEditProduct ? hideEditProductHandler : showEditProductHandler
          }
        >
          {showEditProduct ? "Hide" : "Edit product"}
        </button>
        {/* Remove product Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showRemoveProduct
              ? hideRemoveProductHandler
              : showRemoveProductHandler
          }
        >
          {showRemoveProduct ? "Hide" : "Remove product"}
        </button>
      </div>

      {/* Show Stores */}
      <div className="row">
        {showStore ? (
          productsOfStore && productsOfStore.length === 0 ? (
            <p>"No products on your store..."</p>
          ) : (
            productsOfStore.map((currProduct, index) => (
              <div className="col span-1-of-4">
                <li key={index} className="curr product">
                  <OwnerProduct
                    refresh={props.refresh}
                    onRefresh={props.onRefresh}
                    connID={props.connID}
                    currProduct={currProduct}
                  ></OwnerProduct>
                </li>
              </div>
            ))
          )
        ) : (
          ""
        )}
      </div>

      {/* Add Product */}
      <div className="row">
        {showAddProduct ? (
          <AddProduct
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            storeName={props.currStore.name}
          />
        ) : (
          ""
        )}
      </div>

      {/* Change Quantity */}
      <div className="row">
        {showChangeQuantityProduct ? (
          <ChangeQuantityProduct
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            storeName={props.currStore.name}
          />
        ) : (
          ""
        )}
      </div>
      {/* Edit Product */}
      <div className="row">
        {showEditProduct ? (
          <EditProduct
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            storeName={props.currStore.name}
          />
        ) : (
          ""
        )}
      </div>
      {/* Remvoe Product */}
      <div className="row">
        {showRemoveProduct ? (
          <RemoveProduct
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            storeName={props.currStore.name}
          />
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default OwnerStoreService;
