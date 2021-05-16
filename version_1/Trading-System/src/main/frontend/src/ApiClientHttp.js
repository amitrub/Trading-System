import axios from "axios";

export const createApiClientHttp = () => {
  const UrlBase = "http://localhost:8080/";
  const guestURL = UrlBase.concat("api/");
  const subscriberURL = UrlBase.concat("api/subscriber/");
  const ownerURL = UrlBase.concat("api/owner/");
  // const managerURL = "/app/manager/";
  const adminURL = UrlBase.concat("api/admin/");

  return {
    //Guest
    ClearSystem: () => {
      let path = guestURL.concat(`clear_system`);
      return axios.get(path).then((res) => {
        return res.data;
      });
    },

    ConnectSystem: () => {
      let path = guestURL.concat(`home`);
      return axios.get(path).then((res) => {
        return res.data;
      });
    },

    Exit: (connID) => {
      let path = guestURL.concat(`exit`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    Register: (connID, name, pass) => {
      let path = guestURL.concat(`register`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        userName: name,
        password: pass,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    Login: (connID, name, pass) => {
      let path = guestURL.concat(`login`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        userName: name,
        password: pass,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    ShowAllStores: () => {
      let path = guestURL.concat(`stores`);
      return axios.get(path).then((res) => {
        return res.data;
      });
    },

    ShowStoreProducts: (storeID) => {
      let path = guestURL.concat(`store/${storeID}/products`);
      return axios.get(path).then((res) => {
        return res.data;
      });
    },

    Search: (
      name,
      ProductName,
      ProductCategory,
      minPrice,
      maxPrice,
      pRank,
      sRank
    ) => {
      let path = guestURL.concat(`search`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
      };
      const body = {
        name: name,
        ProductName: ProductName,
        ProductCategory: ProductCategory,
        minPrice: minPrice,
        maxPrice: maxPrice,
        pRank: pRank,
        sRank: sRank,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    AddProductToCart: (connID, storeID, productID, quantity) => {
      let path = guestURL.concat(`shopping_cart/add_product`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        storeID: storeID,
        productID: productID,
        quantity: quantity,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    ShowShoppingCart: (connID) => {
      let path = guestURL.concat(`shopping_cart`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    RemoveProductFromCart: (connID, storeID, productID) => {
      let path = guestURL.concat(`shopping_cart/remove_product`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        storeID: storeID,
        productID: productID,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    EditProductQuantityFromCart: (connID, storeID, productID, quantity) => {
      let path = guestURL.concat(`shopping_cart/edit_product`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        storeID: storeID,
        productID: productID,
        quantity: quantity,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    guestPurchase: (connID, name, credit_number, phone_number, address) => {
      let path = guestURL.concat(`shopping_cart/purchase`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        name: name,
        credit_number: credit_number,
        phone_number: phone_number,
        address: address,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    //Subscriber
    Logout: (connID, userID) => {
      let path = subscriberURL.concat(`${userID}/logout`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AddStore: (connID, userID, storeName) => {
      let path = subscriberURL.concat(`${userID}/add_store`);
      const headers = {
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        storeName: storeName,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    WriteComment: (connID, userID, storeID, productID, comment) => {
      let path = subscriberURL.concat(`${userID}/write_comment`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        storeID: storeID,
        productID: productID,
        comment: comment,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    ShowUserHistory: (connID, userID) => {
      let path = subscriberURL.concat(`${userID}/user_history`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    subscriberPurchase: (
      connID,
      userID,
      credit_number,
      phone_number,
      address
    ) => {
      let path = subscriberURL.concat(`${userID}/shopping_cart/purchase`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        credit_number: credit_number,
        phone_number: phone_number,
        address: address,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },
    //Owner
    ShowAllFoundedStores: (connID, userID) => {
      // console.log("ShowAllFoundedStores");
      // console.log(connID);
      // console.log(userID);
      let path = ownerURL.concat(`${userID}/founded_stores`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    ShowAllOwnedStores: (connID, userID) => {
      let path = ownerURL.concat(`${userID}/owned_stores`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    ShowAllManagedStores: (connID, userID) => {
      let path = ownerURL.concat(`${userID}/managed_stores`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AddProductToStore: (
      connID,
      userID,
      storeID,
      productName,
      category,
      quantity,
      price
    ) => {
      let path = ownerURL.concat(`${userID}/store/${storeID}/add_new_product`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        productName: productName,
        category: category,
        quantity: quantity,
        price: price,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    ChangeQuantityProduct: (connID, userID, storeID, productID, quantity) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/change_quantity_product/${productID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        quantity: quantity,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    EditProduct: (
      connID,
      userID,
      storeID,
      productID,
      productName,
      category,
      quantity,
      price
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_product/${productID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        productName: productName,
        category: category,
        quantity: quantity,
        price: price,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    RemoveProduct: (connID, userID, storeID, productID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_product/${productID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AddBuyingPolicy: (connID, userID, storeID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_buying_policy`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        // TODO
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    AddDiscountPolicy: (connID, userID, storeID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_discount_policy`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        // TODO
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    EditBuyingPolicy: (connID, userID, storeID, buyingPolicyID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_buying_policy/${buyingPolicyID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        // TODO
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    EditDiscountPolicy: (connID, userID, storeID, discountPolicyID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_discount_policy/${discountPolicyID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        // TODO
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    RemoveBuyingPolicy: (connID, userID, storeID, buyingPolicyID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_buying_policy/${buyingPolicyID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    RemoveDiscountPolicy: (connID, userID, storeID, discountPolicyID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_discount_policy/${discountPolicyID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AddNewOwner: (connID, userID, storeID, newOwnerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_owner/${newOwnerID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    RemoveOwner: (connID, userID, storeID, OwnerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_owner/${OwnerID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AddNewManager: (connID, userID, storeID, newManagerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_manager/${newManagerID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    EditManagerPermissions: (
      connID,
      userID,
      storeID,
      managerID,
      AddProduct,
      ReduceProduct,
      DeleteProduct,
      EditProduct,
      AppointmentOwner,
      AppointmentManager,
      EditManagerPermission,
      RemoveManager,
      GetInfoOfficials,
      GetInfoRequests,
      ResponseRequests,
      GetStoreHistory
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_discount_policy/${managerID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      const body = {
        AddProduct: AddProduct,
        ReduceProduct: ReduceProduct,
        DeleteProduct: DeleteProduct,
        EditProduct: EditProduct,
        AppointmentOwner: AppointmentOwner,
        AppointmentManager: AppointmentManager,
        EditManagerPermission: EditManagerPermission,
        RemoveManager: RemoveManager,
        GetInfoOfficials: GetInfoOfficials,
        GetInfoRequests: GetInfoRequests,
        ResponseRequests: ResponseRequests,
        GetStoreHistory: GetStoreHistory,
      };
      return axios.post(path, body, { headers: headers }).then((res) => {
        // console.log(res);
        return res.data;
      });
    },

    GetPossiblePermissionsToManager: (connID, userID) => {
      let path = ownerURL.concat(
        `${userID}/store/get_possible_permissions_to_manager`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    RemoveManager: (connID, userID, storeID, managerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_manager/${managerID}`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    ShowStoreWorkers: (connID, userID, storeID) => {
      let path = ownerURL.concat(`${userID}/store/${storeID}/workers`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    OwnerStoreHistory: (connID, userID, storeID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/store_history_owner`
      );
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    ShowOwnerStores: (connID, userID) => {
      let path = ownerURL.concat(`${userID}/stores_owner`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    ShowManagerStores: (connID, userID) => {
      let path = ownerURL.concat(`${userID}/stores_manager`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    // Admin
    AdminAllUsers: (connID, adminID) => {
      let path = adminURL.concat(`${adminID}/users`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AdminAllStores: (connID, adminID) => {
      let path = adminURL.concat(`${adminID}/stores`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AdminUserHistory: (connID, adminID, userID) => {
      let path = adminURL.concat(`${adminID}/user_history_admin/${userID}`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },

    AdminStoreHistory: (connID, adminID, storeID) => {
      let path = adminURL.concat(`${adminID}/store_history_admin/${storeID}`);
      const headers = {
        "Content-Type": "application/json; utf-8",
        Accept: "application/json",
        connID: connID,
      };
      return axios.get(path, { headers: headers }).then((res) => {
        return res.data;
      });
    },
  };
};

export default createApiClientHttp;
