import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";

const apiHttp = createApiClientHttp();

function AllMoneyWeek(props) {
  const [moneyWeek, setAllMoneyWeek] = useState([]);

  async function fetchAllMoneyWeek() {
    const moneyWeekResponse = await apiHttp.AdminAllMoneyWeek(
      props.connID,
      props.userID
    );

    console.log(moneyWeekResponse);

    if (moneyWeekResponse.isErr) {
      console.log(moneyWeekResponse.message);
    } else {
      setAllMoneyWeek(moneyWeekResponse.returnObject.DailyReview);
    }
  }

  useEffect(() => {
    fetchAllMoneyWeek();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>Weekly Money</h5>
          {moneyWeek && moneyWeek.length > 0
            ? moneyWeek.map((curr) => (
                <li>
                  <p>
                    {curr.date} : {curr.numOfViewers} $.
                  </p>
                </li>
              ))
            : ""}
        </div>
      </section>
    </Fragment>
  );
}

export default AllMoneyWeek;
