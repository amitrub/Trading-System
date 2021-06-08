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
      </section>
    </Fragment>
  );
}

export default DailyIncome;
