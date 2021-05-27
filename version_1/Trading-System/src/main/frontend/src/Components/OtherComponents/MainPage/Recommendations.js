import "../../../Design/grid.css";
import "../../../Design/style.css";
import React, { Component }  from 'react';


function Recommendations() {
  return (
    <section className="section-testimonials" id="recommanditions">
      <div className="row">
        <h2>Our customers can't live without us</h2>
      </div>
      <div className="row">
        <div className="col span-1-of-3">
          <blockquote>
            Omnifood is just awesome! I just launched a startup which leaves me
            with no time for cooking, so Omnifood is a life-saver. Now that I
            got used to it, I couldn't live without my daily meals!
            <img alt="" src="resources/img/customer-1.jpg" />
            Alberto Duncan
          </blockquote>
        </div>
        <div className="col span-1-of-3">
          <blockquote>
            Inexpensive, healthy and great-tasting meals, delivered right to my
            home. We have lots of food delivery here in Lisbon, but no one comes
            even close to Omifood. Me and my family are so in love!
            <img alt="" src="resources/img/customer-2.jpg" />
            Joana Silva
          </blockquote>
        </div>
        <div className="col span-1-of-3">
          <blockquote>
            I was looking for a quick and easy food delivery service in San
            Franciso. I tried a lot of them and ended up with Omnifood. Best
            food delivery service in the Bay Area. Keep up the great work!
            <img alt="" src="resources/img/customer-3.jpg" />
            Milton Chapman
          </blockquote>
        </div>
      </div>
    </section>
  );
}

export default Recommendations;
