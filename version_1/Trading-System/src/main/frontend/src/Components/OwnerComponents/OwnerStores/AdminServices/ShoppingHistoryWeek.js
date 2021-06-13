import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";

const apiHttp = createApiClientHttp();

function ShoppingHistoryWeek(props) {
  const [shoppingHistoryWeek, setShoppingHistoryWeek] = useState([]);

  async function fetchShoppingHistoryWeek() {
    const shoppingHistoryWeekResponse = await apiHttp.AdminAllShoppingHistoriesWeek(
      props.connID,
      props.userID
    );

    console.log(shoppingHistoryWeekResponse);

    if (shoppingHistoryWeekResponse.isErr) {
      console.log(shoppingHistoryWeekResponse.message);
    } else {
      setShoppingHistoryWeek(
        shoppingHistoryWeekResponse.returnObject.DailyReview
      );
    }
  }

  useEffect(() => {
    fetchShoppingHistoryWeek();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>Weekly Shopping Histories</h5>
          {shoppingHistoryWeek && shoppingHistoryWeek.length > 0
            ? shoppingHistoryWeek.map((curr) => (
                <li>
                  <p>
                    {curr.date} : {curr.numOfViewers} Shopping Histories.
                  </p>
                </li>
              ))
            : ""}
        </div>
      </section>
    </Fragment>
  );
}

export default ShoppingHistoryWeek;
