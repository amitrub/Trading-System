import axios from "axios";

export const createApiClient = () => {
  const guestURL = "http://localhost:8080/app/";
  const subscriberURL = "http://localhost:8080/app/subscriber";
  const ownerURL = "http://localhost:8080/app/owner";
  const managerURL = "http://localhost:8080/app/manager";
  const adminURL = "http://localhost:8080/app/admin";

  return {
    //Guest
    getTest: (clientConnection, connID) => {
      let path = `/app/test`;
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    connectSystem: () => {
      let path = guestURL.concat(`home`);
      return axios.get(path).then((res) => {
        console.log("ApiClient:\n" + res);
        return res.data;
      });
    },

    register: (clientConnection) => {
      let path = guestURL.concat(`register`);
      // const headers = {
      //     'Content-Type': 'application/json; utf-8',
      //     'Accept': 'application/json',
      //     'connID': connID
      //   }
      const data = {
        message: "test",
      };

      clientConnection.publish({
        destination: "/app/sendMessage",
        body: JSON.stringify(data),
      });
    },

    login: (clientConnection) => {
      let path = guestURL.concat(`login`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    getAllStores: (clientConnection) => {
      let path = guestURL.concat(`stores`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    getAllProductsOfStore: (clientConnection, storeID) => {
      let path = guestURL.concat(`store/${storeID}/products`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    search: (clientConnection) => {
      let path = guestURL.concat(`search`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    addProductToCart: (clientConnection) => {
      let path = guestURL.concat(`shopping_cart/add_product`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    showShoppingCart: (clientConnection) => {
      let path = guestURL.concat(`shopping_cart`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    removeProductFromCart: (clientConnection) => {
      let path = guestURL.concat(`shopping_cart/remove_product`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    editProductQuantityFromCart: (clientConnection) => {
      let path = guestURL.concat(`shopping_cart/edit_product`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    guestPurchase: (clientConnection) => {
      let path = guestURL.concat(`shopping_cart/purchase`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    //Subscriber
    logout: (clientConnection, userID) => {
      let path = subscriberURL.concat(`${userID}/logout`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    addStore: (clientConnection, userID) => {
      let path = subscriberURL.concat(`${userID}/add_store`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    writeComment: (clientConnection, userID) => {
      let path = subscriberURL.concat(`${userID}/write_comment`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    showUserHistory: (clientConnection, userID) => {
      let path = subscriberURL.concat(`${userID}/user_history`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    subscriberPurchase: (clientConnection, userID) => {
      let path = subscriberURL.concat(`${userID}/shopping_cart/purchase`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    //Owner
    addProductToStore: (clientConnection, userID, storeID) => {
      let path = ownerURL.concat(`${userID}/store/${storeID}/add_new_product`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    changeQuantityProduct: (clientConnection, userID, storeID, productID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/change_quantity_product/${productID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    editProduct: (clientConnection, userID, storeID, productID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_product/${productID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    removeProduct: (clientConnection, userID, storeID, productID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_product/${productID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    addBuyingPolicy: (clientConnection, userID, storeID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_buying_policy`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    addDiscountPolicy: (clientConnection, userID, storeID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_discount_policy`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    editBuyingPolicy: (clientConnection, userID, storeID, buyingPolicyID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_buying_policy/${buyingPolicyID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    editDiscountPolicy: (
      clientConnection,
      userID,
      storeID,
      discountPolicyID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_discount_policy/${discountPolicyID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    removeBuyingPolicy: (clientConnection, userID, storeID, buyingPolicyID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_buying_policy/${buyingPolicyID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    removeDiscountPolicy: (
      clientConnection,
      userID,
      storeID,
      discountPolicyID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_discount_policy/${discountPolicyID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    addNewOwner: (clientConnection, userID, storeID, newOwnerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_owner/${newOwnerID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    removeOwner: (clientConnection, userID, storeID, OwnerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_owner/${OwnerID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    addNewManager: (clientConnection, userID, storeID, newManagerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_manager/${newManagerID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    editManagerPermissions: (clientConnection, userID, storeID, managerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_manager/${managerID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    getPossiblePermissionsToManager: (clientConnection, userID) => {
      let path = ownerURL.concat(
        `${userID}/store/get_possible_permissions_to_manager`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    removeManager: (clientConnection, userID, storeID, managerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_manager/${managerID}`
      );
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    showStoreWorkers: (clientConnection, userID, storeID) => {
      let path = ownerURL.concat(`${userID}/store/${storeID}/workers`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    ownerStoreHistory: (clientConnection, userID, storeID) => {
      let path = ownerURL.concat(`${userID}/store_history_owner/${storeID}`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    showOwnerStores: (clientConnection, userID) => {
      let path = ownerURL.concat(`${userID}/stores_owner`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },

    showManagerStores: (clientConnection, userID) => {
      let path = ownerURL.concat(`${userID}/stores_manager`);
      return clientConnection.get(path).then((res) => {
        console.log(res);
        return res.data;
      });
    },
  };
};

export default createApiClient;
