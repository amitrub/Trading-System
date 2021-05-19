import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import ProductInHistory from "./ProductInHistory";

const apiHttp = createApiClientHttp();

function UserHistory(props) {
  const [userHistory, setUserHistory] = useState([]);

  async function fetchUserHistory() {
    const historyResponse = await apiHttp.ShowUserHistory(
      props.connID,
      props.userID
    );

    console.log(historyResponse);
    const history = historyResponse.returnObject.history;
    const products = history.products;
    // const p = product[0]
    console.log(history);

    if (historyResponse.isErr) {
      console.log(historyResponse.message);
    } else {
      setUserHistory(historyResponse.returnObject.history);
    }
  }

  useEffect(() => {
    fetchUserHistory();
  }, [props.refresh]);

  return (
    <p>checkkkk</p>
    // <Fragment>
    //   <section className="section-form" id="shoppingcart">
    //     {/* Purchase History header */}
    //     <div className="row">
    //       <h2>
    //         <strong>{props.username}'s Purchase History</strong>
    //       </h2>
    //     </div>

    //     <div className="row">
    //       {userHistory.length > 0 ? (
    //         <div>
    //           <div>
    //             {userHistory.map((currHistory, index) => (
    //               <div className="col span-1-of-4">
    //                 <li key={index} className="curr product">
    //                   <ProductInHistory
    //                     onRefresh={props.onRefresh}
    //                     connID={props.connID}
    //                     currHistory={currHistory}
    //                   />
    //                 </li>
    //               </div>
    //             ))}
    //           </div>
    //         </div>
    //       ) : (
    //         "No history, Go Shop bitch!"
    //       )}
    //     </div>
    //   </section>
    // </Fragment>
  );
}

export default UserHistory;
