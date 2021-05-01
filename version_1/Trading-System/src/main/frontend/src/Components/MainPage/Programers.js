import "../../Design/grid.css";
import "../../Design/style.css";
import hadas from "../img/develops/hadas.jpeg";
import roee from "../img/develops/roee.jpeg";
import amit from "../img/develops/rubin.jpeg";
import reut from "../img/develops/reut.jpeg";
import nofet from "../img/develops/nofet.jpeg";
import elinor from "../img/develops/elinor.jpeg";

function Programers() {
  return (
    <section className="section-cities" id="programers">
      <div className="row">
        <h2>This system desigend by the greatest programers</h2>
      </div>
      <div className="row">
        <div className="col span-1-of-7 box">
          <img src={hadas} alt="Lisbon" />
          <h3>Hadas</h3>
          <div className="city-feature">
            <i className="ion-ios-person icon-small"></i>
            2400+ lines of code
          </div>
          <div className="city-feature">
            <i className="ion-ios-star icon-small"></i>
            web desiner
          </div>
          <div className="city-feature">
            <i className="ion-social-twitter icon-small"></i>
            <a href="#programers">zeirah@post.bgu.ac.il</a>
          </div>
        </div>
        <div className="col span-1-of-7 box">
          <img src={roee} alt="San Francisco" />
          <h3>Roee</h3>
          <div className="city-feature">
            <i className="ion-ios-person icon-small"></i>
            3200+ lines of code
          </div>
          <div className="city-feature">
            <i className="ion-ios-star icon-small"></i>
            client side
          </div>
          <div className="city-feature">
            <i className="ion-social-twitter icon-small"></i>
            <a href="#programers">benhr@post.bgu.ac.il</a>
          </div>
        </div>
        <div className="col span-1-of-7 box">
          <img src={amit} alt="Berlin" />
          <h3>Amit</h3>
          <div className="city-feature">
            <i className="ion-ios-person icon-small"></i>
            3400+ lines of code
          </div>
          <div className="city-feature">
            <i className="ion-ios-star icon-small"></i>
            communication
          </div>
          <div className="city-feature">
            <i className="ion-social-twitter icon-small"></i>
            <a href="#programers">amitrb@post.bgu.ac.il</a>
          </div>
        </div>
        <div className="col span-1-of-7 box">
          <img src={reut} alt="London" />
          <h3>Reut</h3>
          <div className="city-feature">
            <i className="ion-ios-person icon-small"></i>
            2600+ lines of code
          </div>
          <div className="city-feature">
            <i className="ion-ios-star icon-small"></i>
            version manager
          </div>
          <div className="city-feature">
            <i className="ion-social-twitter icon-small"></i>
            <a href="#programers">reutle@post.bgu.ac.il</a>
          </div>
        </div>
        <div className="col span-1-of-7 box">
          <img src={nofet} alt="London" />
          <h3>Nofet</h3>
          <div className="city-feature">
            <i className="ion-ios-person icon-small"></i>
            3100+ lines of code
          </div>
          <div className="city-feature">
            <i className="ion-ios-star icon-small"></i>
            server side
          </div>
          <div className="city-feature">
            <i className="ion-social-twitter icon-small"></i>
            <a href="#programers">nofetd@post.bgu.ac.il</a>
          </div>
        </div>
        <div className="col span-1-of-7 box">
          <img src={elinor} alt="London" />
          <h3>Elinor</h3>
          <div className="city-feature">
            <i className="ion-ios-person icon-small"></i>
            2400+ lines of code
          </div>
          <div className="city-feature">
            <i className="ion-ios-star icon-small"></i>
            testing and QA
          </div>
          <div className="city-feature">
            <i className="ion-social-twitter icon-small"></i>
            <a href="#programers">elinor@post.bgu.ac.il</a>
          </div>
        </div>
      </div>
    </section>
  );
}

export default Programers;
