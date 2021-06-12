import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import OwnerProduct from "./OwnerProduct";
import AddProduct from "../OwnerServices/AddProduct";
import EditProduct from "../OwnerServices/EditProduct";
import RemoveProduct from "../OwnerServices/RemoveProduct";
import ChangeQuantityProduct from "../OwnerServices/ChangeQuantityProduct";
import AddRemoveEmployee from "../OwnerServices/AddRemoveEmployee";
import EditPermissions from "../OwnerServices/EditPermissions";
import BuyingPolicy from "../OwnerServices/Policies/BuyingPolicies/BuyingPolicy";
import DiscountPolicy from "../OwnerServices/Policies/SellingPolicies/DiscountPolicy";
import DailyIncome from "../OwnerServices/DailyIncome";
import ShowBiddings from "../OwnerServices/ShowBiddings";

const apiHttp = createApiClientHttp();

function ManagerStoreService(props) {
  const [productsOfStore, setProductsOfStore] = useState([]);
  const [showStore, setShowStore] = useState(false);
  const [permissions, setPermissions] = useState([]);

  //Show Permissions
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [showChangeQuantity, setShowChangeQuantity] = useState(false);
  const [showEditProduct, setShowEditProduct] = useState(false);
  const [showRemoveProduct, setShowRemoveProduct] = useState(false);
  const [showAddOwner, setShowAddOwner] = useState(false);
  const [showRemoveOwner, setShowRemoveOwner] = useState(false);
  const [showAddManager, setShowAddManager] = useState(false);
  const [showRemoveManager, setShowRemoveManager] = useState(false);
  const [showPerssionsManager, setShowPerssionsManager] = useState(false);
  //--
  const [showAddBuyPolicy, setShowAddBuyPolicy] = useState(false);
  const [showAddSellPolicy, setShowAddSellPolicy] = useState(false);
  const [showDailyIncome, setShowDailyIncome] = useState(false);
  const [showBiddings, setShowBiddings] = useState(false);

  const store = props.currStore;

  function hasPermission(perm) {
    return permissions.includes(perm);
  }

  async function fetchStoreProducts() {
    const productsOfStoresResponse = await apiHttp.ShowStoreProducts(store.id);
    // console.log(productsOfStoresResponse);

    if (productsOfStoresResponse.isErr) {
      console.log(productsOfStoresResponse.message);
    } else {
      setProductsOfStore(productsOfStoresResponse.returnObject.products);
    }
  }

  async function fetchPermissions() {
    const permissionsResponse = await apiHttp.GetPossiblePermissionsToManager(
      props.connID,
      props.userID,
      props.currStore.id
    );
    console.log(permissionsResponse);

    if (permissionsResponse.isErr) {
      console.log(permissionsResponse.message);
    } else {
      setPermissions(permissionsResponse.returnObject.permissions);
    }
  }

  useEffect(() => {
    fetchStoreProducts();
    fetchPermissions();
  }, [props.refresh]);

  //Show Product Btn
  function submitLoadProducts() {
    setShowStore(true);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function sumbitHideProducts() {
    setShowStore(false);
    props.onRefresh();
  }

  //Add Product Btn
  function showAddProductsHandler() {
    setShowStore(false);
    setShowAddProduct(true);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideAddProductsHandler() {
    setShowAddProduct(false);
    props.onRefresh();
  }

  //Change quantity Btn
  function showChangeQuantityHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(true);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideChangeQuantityHandler() {
    setShowChangeQuantity(false);
    props.onRefresh();
  }

  //Edit prod Btn
  function showEditProductHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(true);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideEditProductHandler() {
    setShowEditProduct(false);
    props.onRefresh();
  }

  //Remove prod Btn
  function showRemoveProductHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(true);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideRemoveProductHandler() {
    setShowRemoveProduct(false);
    props.onRefresh();
  }

  //Add Owner Btn
  function showAddOwnerHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(true);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideAddOwnerHandler() {
    setShowAddOwner(false);

    props.onRefresh();
  }

  //Remove Owner Btn
  function showRemoveOwnerHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(true);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideRemoveOwnerHandler() {
    setShowRemoveOwner(false);

    props.onRefresh();
  }

  //Add Manager Btn
  function showAddManagerHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(true);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideAddManagerHandler() {
    setShowAddManager(false);

    props.onRefresh();
  }

  //Remove Manager Btn
  function showRemoveManagerHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(true);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideRemoveManagerHandler() {
    setShowRemoveManager(false);

    props.onRefresh();
  }

  //Edit Permissions Manager Btn
  function showPermissionsManagerHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(true);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hidePermissionsManagerHandler() {
    setShowPerssionsManager(false);

    props.onRefresh();
  }

  //Add Buying policy Btn
  function showAddBuyingPolicyHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(true);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideAddBuyingPolicyHandler() {
    setShowAddBuyPolicy(false);

    props.onRefresh();
  }

  //Add Selling policy Btn
  function showAddSellingPolicyHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(true);
    setShowDailyIncome(false);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideAddSellingPolicyHandler() {
    setShowAddSellPolicy(false);

    props.onRefresh();
  }

  //Daliy Income Btn
  function showDailyIncomeHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(true);
    setShowBiddings(false);

    props.onRefresh();
  }
  function hideDailyIncomeHandler() {
    setShowDailyIncome(false);

    props.onRefresh();
  }

  //Bidding Btn
  function showBiddingHandler() {
    setShowStore(false);
    setShowAddProduct(false);
    setShowChangeQuantity(false);
    setShowEditProduct(false);
    setShowRemoveProduct(false);
    setShowAddOwner(false);
    setShowRemoveOwner(false);
    setShowAddManager(false);
    setShowRemoveManager(false);
    setShowPerssionsManager(false);
    setShowAddBuyPolicy(false);
    setShowAddSellPolicy(false);
    setShowDailyIncome(false);
    setShowBiddings(true);

    props.onRefresh();
  }
  function hideBiddingHandler() {
    setShowBiddings(false);

    props.onRefresh();
  }

  return (
    <section className="section-plans js--section-plans" id="store">
      <div className="row">
        <h2>
          <strong>{store.name}</strong> rate:{store.storeRate}
        </h2>
        <p>If some services blocked, ask permissions from owners</p>
      </div>
      <div className="row">
        {/* Show products Btn */}
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
          disabled={!hasPermission("AddProduct")}
        >
          {showAddProduct ? "Hide" : "Add products"}
        </button>
        {/* Change Quantity Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showChangeQuantity
              ? hideChangeQuantityHandler
              : showChangeQuantityHandler
          }
          disabled={!hasPermission("ReduceProduct")}
        >
          {showChangeQuantity ? "Hide" : "Change Quantity"}
        </button>
        {/* Edit product Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showEditProduct ? hideEditProductHandler : showEditProductHandler
          }
          disabled={!hasPermission("EditProduct")}
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
          disabled={!hasPermission("DeleteProduct")}
        >
          {showRemoveProduct ? "Hide" : "Remove product"}
        </button>
      </div>
      <div className="row">
        <p>---------------</p>
      </div>
      <div className="row">
        {/* Add Owner Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showAddOwner ? hideAddOwnerHandler : showAddOwnerHandler}
          disabled={!hasPermission("AppointmentOwner")}
        >
          {showAddOwner ? "Hide" : "Add Owner"}
        </button>
        {/* Remove Owner Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showRemoveOwner ? hideRemoveOwnerHandler : showRemoveOwnerHandler
          }
          disabled={!hasPermission("AppointmentOwner")}
        >
          {showRemoveOwner ? "Hide" : "Remove Owner"}
        </button>
        {/* Add Manager Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showAddManager ? hideAddManagerHandler : showAddManagerHandler
          }
          disabled={!hasPermission("AppointmentManager")}
        >
          {showAddManager ? "Hide" : "Add Manager"}
        </button>
        {/* Remove Manager Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showRemoveManager
              ? hideRemoveManagerHandler
              : showRemoveManagerHandler
          }
          disabled={!hasPermission("RemoveManager")}
        >
          {showRemoveManager ? "Hide" : "Remove Manager"}
        </button>
        {/* Edit Permissions Manager Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showPerssionsManager
              ? hidePermissionsManagerHandler
              : showPermissionsManagerHandler
          }
          disabled={!hasPermission("EditManagerPermission")}
          // disabled
        >
          {showPerssionsManager ? "Hide" : "Permissions"}
        </button>
      </div>
      <div className="row">
        <p>---------------</p>
      </div>

      <div className="row">
        {/* Add Buying policy Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showAddBuyPolicy
              ? hideAddBuyingPolicyHandler
              : showAddBuyingPolicyHandler
          }
          disabled={!hasPermission("EditBuyingPolicy")}
        >
          {showAddBuyPolicy ? "Hide" : "Add Buying Policy"}
        </button>

        {/* Add Selling policy Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showAddSellPolicy
              ? hideAddSellingPolicyHandler
              : showAddSellingPolicyHandler
          }
          disabled={!hasPermission("EditDiscountPolicy")}
        >
          {showAddSellPolicy ? "Hide" : "Add Selling Policy"}
        </button>

        {/* Daliy Income Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={
            showDailyIncome ? hideDailyIncomeHandler : showDailyIncomeHandler
          }
          disabled={!hasPermission("GetDailyIncomeForStore")}
        >
          {showDailyIncome ? "Hide" : "Show Daliy Income"}
        </button>

        {/* Bidding Btn */}
        <button
          className="buttonus"
          value="load our stores..."
          onClick={showBiddings ? hideBiddingHandler : showBiddingHandler}
          disabled={!hasPermission("RequestBidding")}
        >
          {showBiddings ? "Hide" : "show Biddings"}
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
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
          />
        ) : (
          ""
        )}
      </div>
      {/* Change Quantity */}
      <div className="row">
        {showChangeQuantity ? (
          <ChangeQuantityProduct
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
          />
        ) : (
          ""
        )}
      </div>
      {/* Edit Product */}
      <div className="row">
        {showEditProduct ? (
          <EditProduct
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
          />
        ) : (
          ""
        )}
      </div>
      {/* Remvoe Product */}
      <div className="row">
        {showRemoveProduct ? (
          <RemoveProduct
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
          />
        ) : (
          ""
        )}
      </div>

      {/* ------------------------ */}
      {/* Add Owner */}
      <div className="row">
        {showAddOwner ? (
          <AddRemoveEmployee
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            job="Add Owner"
            label="addOwner"
            action={apiHttp.AddNewOwner}
          />
        ) : (
          ""
        )}
      </div>
      {/* Remvoe Owner */}
      <div className="row">
        {showRemoveOwner ? (
          <AddRemoveEmployee
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            job="Remvoe Owner"
            label="removeOwner"
            action={apiHttp.RemoveOwner}
          />
        ) : (
          ""
        )}
      </div>
      {/* Add Manger */}
      <div className="row">
        {showAddManager ? (
          <AddRemoveEmployee
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            job="Add Manger"
            label="addManager"
            action={apiHttp.AddNewManager}
          />
        ) : (
          ""
        )}
      </div>
      {/* Remvoe Manager */}
      <div className="row">
        {showRemoveManager ? (
          <AddRemoveEmployee
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
            job="Remvoe Manager"
            label="removeManager"
            action={apiHttp.RemoveManager}
          />
        ) : (
          ""
        )}
      </div>
      {/* Edit Manager Permissions */}
      <div className="row">
        {showPerssionsManager ? (
          <EditPermissions
            refresh={props.refresh}
            onRefresh={props.onRefresh}
            connID={props.connID}
            userID={props.userID}
            storeID={props.currStore.id}
          />
        ) : (
          ""
        )}
      </div>

      {/* Add Buying Policy */}
      <div className="row">
        {showAddBuyPolicy ? (
          <div>
            <BuyingPolicy
              refresh={props.refresh}
              onRefresh={props.onRefresh}
              connID={props.connID}
              userID={props.userID}
              storeID={props.currStore.id}
            />
          </div>
        ) : (
          ""
        )}
      </div>

      {/* Add Selling Policy */}
      <div className="row">
        {showAddSellPolicy ? (
          <div>
            <DiscountPolicy
              refresh={props.refresh}
              onRefresh={props.onRefresh}
              connID={props.connID}
              userID={props.userID}
              storeID={props.currStore.id}
            />
          </div>
        ) : (
          ""
        )}
      </div>

      {/* Daily Income */}
      <div className="row">
        {showDailyIncome ? (
          <div>
            <DailyIncome
              refresh={props.refresh}
              onRefresh={props.onRefresh}
              connID={props.connID}
              userID={props.userID}
              storeID={props.currStore.id}
            />
          </div>
        ) : (
          ""
        )}
      </div>

      {/* Daily Income */}
      <div className="row">
        {showBiddings ? (
          <div>
            <ShowBiddings
              refresh={props.refresh}
              onRefresh={props.onRefresh}
              connID={props.connID}
              userID={props.userID}
              storeID={props.currStore.id}
            />
          </div>
        ) : (
          ""
        )}
      </div>
    </section>
  );
}

export default ManagerStoreService;
