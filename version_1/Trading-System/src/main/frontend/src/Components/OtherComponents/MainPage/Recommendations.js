import "../../../Design/grid.css";
import "../../../Design/style.css";
import React, { Component } from "react";

function Recommendations() {
  return (
    <section className="section-testimonials" id="recommanditions">
      <div className="row">
        <h2>Our customers can't live without us</h2>
      </div>
      <div className="row">
        <div className="col span-1-of-3">
          <blockquote>
            AliBamba is just awesome! I just ordered a birthday gift for my
            nepuw, and I recieved it in 3 days!! AliBamba is a life-saver. Now
            that I got used to it, I couldn't live without my daily orders!
            <img alt="" src="resources/img/customer-1.jpg" />
            Alberto Duncan
          </blockquote>
        </div>
        <div className="col span-1-of-3">
          <blockquote>
            Inexpensive, big stock and great prices, delivered right to my home.
            We have lots of shops here in Tel Aviv, but no one comes even close
            to AliBamba. Me and my family are so in love!
            <img alt="" src="resources/img/customer-2.jpg" />
            Joana Silva
          </blockquote>
        </div>
        <div className="col span-1-of-3">
          <blockquote>
            I was looking for a quick and easy iphone case in Ramat Gan. I tried
            a lot of them and ended up with AliBamba. Best price I found from
            all stores and webs. Keep up the great work!
            <img alt="" src="resources/img/customer-3.jpg" />
            Milton Chapman
          </blockquote>
        </div>
      </div>
    </section>
  );
}

export default Recommendations;
