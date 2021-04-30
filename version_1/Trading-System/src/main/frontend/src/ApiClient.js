import axios from "axios";

export const createApiClient = () => {
  const guestURL = "/app/";
  const subscriberURL = "/app/subscriber/";
  const ownerURL = "/app/owner/";
  const managerURL = "/app/manager/";
  const adminURL = "/app/admin/";

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

    register: (clientConnection, connID, name, pass) => {
      let path = guestURL.concat(`register`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          userName: name,
          password: pass,
        }),
      });
    },

    login: (clientConnection, connID, name, pass) => {
      let path = guestURL.concat(`login`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          userName: name,
          password: pass,
        }),
      });
    },

    getAllStores: (clientConnection, connID) => {
      let path = guestURL.concat(`stores`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    getAllProductsOfStore: (clientConnection, connID, storeID) => {
      let path = guestURL.concat(`store/${storeID}/products`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    search: (
      clientConnection,
      connID,
      name,
      prodName,
      prodCat,
      min,
      max,
      prank,
      srank
    ) => {
      let path = guestURL.concat(`search`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          name: name,
          ProductName: prodName,
          ProductCategory: prodCat,
          minPrice: min,
          maxPrice: max,
          pRank: prank,
          sRank: srank,
        }),
      });
    },

    addProductToCart: (
      clientConnection,
      connID,
      storeID,
      productID,
      quantity
    ) => {
      let path = guestURL.concat(`shopping_cart/add_product`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          storeID: storeID,
          productID: productID,
          quantity: quantity,
        }),
      });
    },

    showShoppingCart: (clientConnection, connID) => {
      let path = guestURL.concat(`shopping_cart`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    removeProductFromCart: (clientConnection, connID, storeID, productID) => {
      let path = guestURL.concat(`shopping_cart/remove_product`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          storeID: storeID,
          productID: productID,
        }),
      });
    },

    editProductQuantityFromCart: (
      clientConnection,
      connID,
      storeID,
      productID,
      quantity
    ) => {
      let path = guestURL.concat(`shopping_cart/edit_product`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          storeID: storeID,
          productID: productID,
          quantity: quantity,
        }),
      });
    },

    guestPurchase: (
      clientConnection,
      connID,
      name,
      credit_number,
      phone_number,
      address
    ) => {
      let path = guestURL.concat(`shopping_cart/purchase`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          name: name,
          credit_number: credit_number,
          phone_number: phone_number,
          address: address,
        }),
      });
    },

    //Subscriber
    logout: (clientConnection, connID, userID) => {
      let path = subscriberURL.concat(`${userID}/logout`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    addStore: (clientConnection, connID, userID, storeName) => {
      let path = subscriberURL.concat(`${userID}/add_store`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          storeName: storeName,
        }),
      });
    },

    writeComment: (clientConnection, connID, userID, storeID) => {
      let path = subscriberURL.concat(`${userID}/write_comment`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          storeID: storeID,
        }),
      });
    },

    showUserHistory: (clientConnection, connID, userID, productID, comment) => {
      let path = subscriberURL.concat(`${userID}/user_history`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          productID: productID,
          comment: comment,
        }),
      });
    },

    subscriberPurchase: (
      clientConnection,
      connID,
      userID,
      credit_number,
      phone_number,
      address
    ) => {
      let path = subscriberURL.concat(`${userID}/shopping_cart/purchase`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          credit_number: credit_number,
          phone_number: phone_number,
          address: address,
        }),
      });
    },

    //Owner
    addProductToStore: (
      clientConnection,
      connID,
      userID,
      storeID,
      productName,
      category,
      quantity
    ) => {
      let path = ownerURL.concat(`${userID}/store/${storeID}/add_new_product`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          productName: productName,
          category: category,
          quantity: quantity,
        }),
      });
    },

    changeQuantityProduct: (
      clientConnection,
      connID,
      userID,
      storeID,
      productID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/change_quantity_product/${productID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          quantity: quantity,
        }),
      });
    },

    editProduct: (
      clientConnection,
      connID,
      userID,
      storeID,
      productID,
      productName,
      category,
      price,
      quantity
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_product/${productID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
          productName: productName,
          category: category,
          price: price,
          quantity: quantity,
        }),
      });
    },

    removeProduct: (clientConnection, connID, userID, storeID, productID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_product/${productID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    addBuyingPolicy: (clientConnection, connID, userID, storeID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_buying_policy`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    addDiscountPolicy: (clientConnection, connID, userID, storeID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_discount_policy`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    editBuyingPolicy: (
      clientConnection,
      connID,
      userID,
      storeID,
      buyingPolicyID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_buying_policy/${buyingPolicyID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    editDiscountPolicy: (
      clientConnection,
      connID,
      userID,
      storeID,
      discountPolicyID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/edit_discount_policy/${discountPolicyID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    removeBuyingPolicy: (
      clientConnection,
      connID,
      userID,
      storeID,
      buyingPolicyID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_buying_policy/${buyingPolicyID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    removeDiscountPolicy: (
      clientConnection,
      connID,
      userID,
      storeID,
      discountPolicyID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_discount_policy/${discountPolicyID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    addNewOwner: (clientConnection, connID, userID, storeID, newOwnerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_owner/${newOwnerID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    removeOwner: (clientConnection, connID, userID, storeID, OwnerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_owner/${OwnerID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    addNewManager: (
      clientConnection,
      connID,
      userID,
      storeID,
      newManagerID
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_manager/${newManagerID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    editManagerPermissions: (
      clientConnection,
      connID,
      userID,
      storeID,
      managerID,
      AddProduct,
      ReduceProduct,
      DeleteProduct,
      EditProduct,
      AppointmentOwner,
      EditManagerPermission,
      RemoveManager,
      GetInfoOfficials,
      GetInfoRequests,
      ResponseRequests,
      GetStoreHistory
    ) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/add_new_manager/${managerID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
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
        }),
      });
    },

    getPossiblePermissionsToManager: (clientConnection, connID, userID) => {
      let path = ownerURL.concat(
        `${userID}/store/get_possible_permissions_to_manager`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    removeManager: (clientConnection, connID, userID, storeID, managerID) => {
      let path = ownerURL.concat(
        `${userID}/store/${storeID}/remove_manager/${managerID}`
      );
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    showStoreWorkers: (clientConnection, connID, userID, storeID) => {
      let path = ownerURL.concat(`${userID}/store/${storeID}/workers`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    ownerStoreHistory: (clientConnection, connID, userID, storeID) => {
      let path = ownerURL.concat(`${userID}/store_history_owner/${storeID}`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    showOwnerStores: (clientConnection, connID, userID) => {
      let path = ownerURL.concat(`${userID}/stores_owner`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },

    showManagerStores: (clientConnection, connID, userID) => {
      let path = ownerURL.concat(`${userID}/stores_manager`);
      clientConnection.publish({
        destination: path,
        body: JSON.stringify({
          connID: connID,
        }),
      });
    },
  };
};

export default createApiClient;
