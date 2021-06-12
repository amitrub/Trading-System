import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";

const apiHttp = createApiClientHttp();

function SystemDailyIncome(props) {
  const [sysDailyIncome, setSysDailyIncome] = useState([]);

  async function fetchSysDailyIncome() {
    const sysDailyIncomeResponse = await apiHttp.AdminDailyIncomeForSystem(
      props.connID,
      props.userID
    );
    console.log(sysDailyIncomeResponse);

    if (sysDailyIncomeResponse.isErr) {
      console.log(sysDailyIncomeResponse.message);
    } else {
      setSysDailyIncome(sysDailyIncomeResponse.returnObject.DailyIncome);
    }
  }

  useEffect(() => {
    fetchSysDailyIncome();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>System daily income is: {sysDailyIncome}$</h5>
        </div>
      </section>
    </Fragment>
  );
}

export default SystemDailyIncome;
