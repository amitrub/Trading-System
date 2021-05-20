import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../../../ApiClientHttp";
import "../../../../../Design/grid.css";
import "../../../../../Design/style.css";
import MyPopup from "../../../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function AllUsersHistories(props) {
  const [users, setUsers] = useState([]);
  const [x, setX] = useState(false);

  const [popupAddProduct, setPopupAddProduct] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function fetchAllUsers() {
    const allSubscribersResponse = await apiHttp.GetAllSubscribers(
      props.connID,
      props.userID
    );
    console.log(allSubscribersResponse);

    if (allSubscribersResponse.isErr) {
      console.log(allSubscribersResponse.message);
    } else {
      setUsers(allSubscribersResponse.returnObject.subscribers);
    }
  }

  useEffect(() => {
    fetchAllUsers();
  }, [x]);

  function show() {
    setX(!x);
  }

  //   async function submitAddProductHandler(event) {
  //     event.preventDefault();
  //     // console.log("before AddProduct");

  //     const addProductResponse = await apiHttp.AddProductToStore(
  //       props.connID,
  //       props.userID,
  //       props.storeID,
  //       productName,
  //       category,
  //       quantity,
  //       price
  //     );

  //     // console.log("AddProduct");
  //     // console.log(addProductResponse);

  //     if (addProductResponse) {
  //       setPopupMsg(addProductResponse.message);
  //       setPopupAddProduct(true);
  //     }
  //     if (addProductResponse.isErr) {
  //       console.log(addProductResponse.message);
  //     }
  //     props.onRefresh();
  //   }

  //   function updateProductName(event) {
  //     setProductName(event.target.value);
  //   }

  //   function updateCategory(event) {
  //     setCategory(event.target.value);
  //   }

  //   function updateQuantity(event) {
  //     setQuantity(event.target.value);
  //   }

  //   function updatePrice(event) {
  //     setPrice(event.target.value);
  //   }

  //   function onClosePopupAddProduct() {
  //     setPopupAddProduct(false);
  //     props.onRefresh();
  //   }

  return (
    <section>
      <div>
        <p>ddddddddddddddd</p>
      </div>
    </section>
  );
}

export default AllUsersHistories;

// {/* <div>
//         <div className="row">
//           <h2>All Users Histories</h2>
//         </div>

//         <div className="row">
//           <form
//             method="post"
//             className="contact-form"
//             onSubmit={submitAddProductHandler}
//           >
//             {/* product name */}
//             <div className="row">
//               <div className="col span-1-of-3">
//                 <label htmlFor="name">Product name</label>
//               </div>
//               <div className="col span-2-of-3">
//                 <input
//                   type="text"
//                   name="Name"
//                   id="Name"
//                   required
//                   onChange={updateProductName}
//                   placeholder={"how you call to your product?"}
//                 />
//               </div>
//             </div>
//             {/* category */}
//             <div className="row">
//               <div className="col span-1-of-3">
//                 <label htmlFor="name">Category</label>
//               </div>
//               <div className="col span-2-of-3">
//                 <input
//                   type="text"
//                   name="Name"
//                   id="Name"
//                   required
//                   onChange={updateCategory}
//                   placeholder={"choose category"}
//                 />
//               </div>
//             </div>
//             {/* Quantity */}
//             <div className="row">
//               <div className="col span-1-of-3">
//                 <label htmlFor="name">Quantity</label>
//               </div>
//               <div className="col span-2-of-3">
//                 <input
//                   type="number"
//                   name="Quantity"
//                   placeholder="insert quantity"
//                   min="1"
//                   onChange={updateQuantity}
//                 />
//               </div>
//             </div>
//             {/* Price */}
//             <div className="row">
//               <div className="col span-1-of-3">
//                 <label htmlFor="name">Price</label>
//               </div>
//               <div className="col span-2-of-3">
//                 <input
//                   type="number"
//                   name="Price"
//                   placeholder="insert price"
//                   min="1"
//                   onChange={updatePrice}
//                 />
//               </div>
//             </div>
//             <div className="row">
//               <div className="col span-1-of-3">
//                 <label>&nbsp;</label>
//               </div>
//               <div className="col span-1-of-3">
//                 <input type="submit" value="Add Product!" />
//               </div>
//             </div>
//           </form>
//         </div>
//       </div>
//       {popupAddProduct ? (
//         <MyPopup
//           errMsg={popupMsg}
//           onClosePopup={onClosePopupAddProduct}
//         ></MyPopup>
//       ) : (
//         ""
//       )} */}
