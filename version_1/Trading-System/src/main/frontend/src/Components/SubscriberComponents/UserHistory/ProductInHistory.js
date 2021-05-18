// import React, { useState } from "react";
// import "../../../Design/grid.css";
// import "../../../Design/style.css";
// import createApiClientHttp from "../../../ApiClientHttp";

// const apiHtml = createApiClientHttp();

// function ProductInHistory(props) {
//   const history = props.currHistory;
//   const products = history.products;

//   function submitNewQuantity(event) {
//     const strQuantity = event.target.value;
//     setEditedQuantity(parseInt(strQuantity));
//   }

//   async function submitEditQuantity() {
//     console.log("Edit quantity in cart");
//     const editCartResponse = await apiHtml.EditProductQuantityFromCart(
//       props.connID,
//       product.storeID,
//       product.productID,
//       editedQuantity
//     );

//     if (editCartResponse.isErr) {
//       console.log(editCartResponse.message);
//     } else {
//       //we make the shopping cart refresh on the other component
//       props.onRefresh();
//     }
//   }

//   async function sumbitRemoveProduct() {
//     console.log("sumbitRemoveProduct");
//     const removeProdFromCartResponse = await apiHtml.RemoveProductFromCart(
//       props.connID,
//       product.storeID,
//       product.productID
//     );

//     if (removeProdFromCartResponse.isErr) {
//       console.log(removeProdFromCartResponse.message);
//     } else {
//       //we make the shopping cart refresh on the other component
//       props.onRefresh();
//     }
//   }

//   return (
//     <div className="plan-box">
//       <div>
//         <h3>{product.productName}</h3>
//         <p className="plan-price">${product.price}</p>
//         <p className="plan-price-meal">{product.quantity} units in cart</p>
//         <p className="plan-price-meal">
//           from store '{product.storeName}' (id={product.storeID})
//         </p>
//       </div>
//       <div>
//         <p>Category: {product.category}</p>
//       </div>
//       <div>
//         {/* <form onSubmit={submitBuyProductHandler} method="post" action="#"> */}
//         {/* Edit Product Quantity */}
//         <div className="">
//           <input
//             type="number"
//             name="edit"
//             placeholder="insert new quantity"
//             min="1"
//             onChange={submitNewQuantity}
//           />
//           <button
//             className="buttonus"
//             value="edit product cart"
//             onClick={submitEditQuantity}
//           >
//             Edit quantity
//           </button>
//         </div>

//         <button
//           className="buttonus"
//           value="Remove from cart"
//           onClick={sumbitRemoveProduct}
//         >
//           Remove from cart
//         </button>
//         {/* </form> */}
//       </div>
//     </div>
//   );
// }

// export default ProductInHistory;
