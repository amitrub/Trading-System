import React from "react";
import "../../Design/grid.css";
import "../../Design/style.css";

function DownPage() {
  return (
    <footer id="downpage">
      <div className="row">
        <div className="col span-1-of-2">
          <ul className="footer-nav">
            <li>
              <a href="#programers">About us</a>
            </li>
            <li>
              <a href="#downpage">Blog</a>
            </li>
            <li>
              <a href="#downpage">Press</a>
            </li>
            <li>
              <a href="#downpage">iOS App</a>
            </li>
            <li>
              <a href="#downpage">Android App</a>
            </li>
          </ul>
        </div>
        <div class="col span-1-of-2">
          <ul class="social-links">
            <li>
              <a href="#downpage">
                <i className="ion-social-facebook"></i>
              </a>
            </li>
            <li>
              <a href="#downpage">
                <i className="ion-social-twitter"></i>
              </a>
            </li>
            <li>
              <a href="#downpage">
                <i className="ion-social-googleplus"></i>
              </a>
            </li>
            <li>
              <a href="#downpage">
                <i className="ion-social-instagram"></i>
              </a>
            </li>
          </ul>
        </div>
      </div>
      <div className="row">
        <p>
          This webpage was created for the course "Sadna"" by Mira . copyrights
          here!. This site was set-up with a lot of effor, working hours,
          collaboration and self-learning. Enjoy it as much as you can!
        </p>
        <p>
          Build with{" "}
          <i
            className="ion-ios-heart"
            // style="color: #ea0000; padding: 0 3px;"
          ></i>{" "}
          in the beautiful city of Be'er Sheva, Isreal, April 2021.
        </p>
      </div>
    </footer>
  );
}

export default DownPage;
