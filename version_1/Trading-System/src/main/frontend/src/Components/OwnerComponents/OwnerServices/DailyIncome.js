import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
// import ProductInCart from "./ProductInCart";

const apiHttp = createApiClientHttp();

function DailyIncome(props) {
  const [dailyIncome, setDailyIncome] = useState([]);

  async function fetchDailyIncome() {
    const dailyIncomeResponse = await apiHttp.OwnerDailyIncomeForStore(
      props.connID,
      props.userID,
      props.storeID
    );
    // console.log(dailyIncomeResponse);

    if (dailyIncomeResponse.isErr) {
      console.log(dailyIncomeResponse.message);
    } else {
      setDailyIncome(dailyIncomeResponse.returnObject.DailyIncome);
    }
  }

  useEffect(() => {
    fetchDailyIncome();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>Your daily income is: {dailyIncome}$</h5>
        </div>
        {/* Shopping Cart header */}
        {/* <div className="row">
          <h2>
            <strong>{props.username}'s Shopping Cart</strong>
          </h2>
        </div>

        <div className="row">
          {shoppingCart.length > 0 ? (
            <div>
              <div>
                {shoppingCart.map((currProduct, index) => (
                  <div className="col span-1-of-4">
                    <li key={index} className="curr product">
                      <ProductInCart
                        onRefresh={props.onRefresh}
                        connID={props.connID}
                        currProduct={currProduct}
                      ></ProductInCart>
                    </li>
                  </div>
                ))}
              </div>
            </div>
          ) : (
            "No products, Go Shop bitch!"
          )}
        </div> */}
      </section>
    </Fragment>
  );
}

export default DailyIncome;
