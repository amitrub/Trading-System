import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";

const apiHttp = createApiClientHttp();

function StoresWeek(props) {
  const [storesWeek, setStoresWeek] = useState([]);

  async function fetchStoresWeek() {
    const storesWeekResponse = await apiHttp.AdminAllStoresWeek(
      props.connID,
      props.userID
    );

    console.log(storesWeekResponse);

    if (storesWeekResponse.isErr) {
      console.log(storesWeekResponse.message);
    } else {
      setStoresWeek(storesWeekResponse.returnObject.DailyReview);
    }
  }

  useEffect(() => {
    fetchStoresWeek();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>Weekly Stores Viewers</h5>
          {storesWeek && storesWeek.length > 0
            ? storesWeek.map((curr) => (
                <li>
                  <p>
                    {curr.date} : {curr.numOfViewers} Viewrs.
                  </p>
                </li>
              ))
            : ""}
        </div>
      </section>
    </Fragment>
  );
}

export default StoresWeek;
