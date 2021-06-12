import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../../ApiClientHttp";
import "../../../../Design/grid.css";
import "../../../../Design/style.css";

const apiHttp = createApiClientHttp();

function SubscriberWeek(props) {
  const [subscriberWeek, setSubscriberWeek] = useState([]);

  async function fetchSubscriberWeek() {
    const subscriberWeekResponse = await apiHttp.AdminAllSubscribersWeek(
      props.connID,
      props.userID
    );
    console.log(subscriberWeekResponse);

    if (subscriberWeekResponse.isErr) {
      console.log(subscriberWeekResponse.message);
    } else {
      setSubscriberWeek(subscriberWeekResponse.returnObject.DailyReview);
    }
  }

  useEffect(() => {
    fetchSubscriberWeek();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form" id="shoppingcart">
        <div>
          <h5>Weekly Subscriber Viewers</h5>
          {subscriberWeek && subscriberWeek.length > 0
            ? subscriberWeek.map((curr) => (
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

export default SubscriberWeek;
