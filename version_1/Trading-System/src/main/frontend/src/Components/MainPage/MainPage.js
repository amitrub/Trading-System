import "./MainPageDesign/style.css";
import "./MainPageDesign/grid.css";
// import './MainPageDesign/queries.css'
import fashion from "../img/sadna-fashion.jpeg";
import electronics from "../img/sadna-elctorincs.jpeg";
import pets from "../img/sadna-dogs.jpeg";
import kitchen from "../img/sadna-kitchen.jpeg";

function mainPage() {
  return (
    <div>
      {/* <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />

        <link
          rel="stylesheet"
          type="text/css"
          href="vendors/css/normalize.css"
        />
        <link rel="stylesheet" type="text/css" href="vendors/css/grid.css" />
        <link
          rel="stylesheet"
          type="text/css"
          href="vendors/css/ionicons.min.css"
        />
        <link rel="stylesheet" type="text/css" href="vendors/css/animate.css" />
        <link rel="stylesheet" type="text/css" href="resources/css/style.css" />
        <link
          rel="stylesheet"
          type="text/css"
          href="resources/css/queries.css"
        />
        <link
          href="http://fonts.googleapis.com/css?family=Lato:100,300,400,300italic"
          rel="stylesheet"
          type="text/css"
        />
        <title>AliBamba</title>
      </head> */}

      {/* --------------------- HEADER ---------------------- */}
      {/* <body> */}
      <header>
        <nav>
          <div className="row">
            {/* <img src="resources/img/online-shopping-logo.jpeg" alt="Omnifood logo" className="logo"> </img> */}
            <ul className="main-nav js--main-nav" id="home">
              <li>
                <a href="#home">home</a>
              </li>
              <li>
                <a href="#sign">sign</a>
              </li>
              <li>
                <a href="#programers">about us</a>
              </li>
              <li>
                <a href="#recommanditions">recommanditions</a>
              </li>
            </ul>
            <a className="mobile-nav-icon js--nav-icon">
              <i className="ion-navicon-round"></i>
            </a>
          </div>
        </nav>
        <div className="hero-text-box">
          <h1>
            <strong>welcome to Ali - Bamba.</strong>
          </h1>
          <h1>the best trading system for you</h1>
          <a className="btn btn-full js--scroll-to-plans" href="#connect">
            Let's shop!
          </a>
          <a className="btn btn-ghost js--scroll-to-start" href="#connect">
            <strong>Connect</strong>
          </a>
        </div>
      </header>

      {/* --------------------- FEATURES ---------------------- */}

      <section className="section-features js--section-features" id="features">
        <div className="row">
          <h2>Shop now &mdash; Get it tomorrow</h2>
          <p className="long-copy">
            Hello, we're AliBamba, your new trading system service. We know
            you're always busy. No time for going to shopping malls. So let us
            take care of that, we're really good at it, we promise!
          </p>
        </div>

        <div className="row">
          <div className="col span-1-of-4 box">
            <i className="ion-ios-infinite-outline icon-big"></i>
            <h3>Up to 365 days/year</h3>
            <p>
              Never cook again! We really mean that. Our subscription plans
              include up to 365 days/year coverage. You can also choose to order
              more flexibly if that's your style.
            </p>
          </div>
          <div className="col span-1-of-4 box">
            <i className="ion-ios-stopwatch-outline icon-big"></i>
            <h3>Ready in 20 minutes</h3>
            <p>
              You're only twenty minutes away from your delicious and super
              healthy meals delivered right to your home. We work with the best
              chefs in each town to ensure that you're 100% happy.
            </p>
          </div>
          <div className="col span-1-of-4 box">
            <i className="ion-ios-nutrition-outline icon-big"></i>
            <h3>100% organic</h3>
            <p>
              All our vegetables are fresh, organic and local. Animals are
              raised without added hormones or antibiotics. Good for your
              health, the environment, and it also tastes better!
            </p>
          </div>
          <div className="col span-1-of-4 box">
            <i className="ion-ios-cart-outline icon-big"></i>
            <h3>Order anything</h3>
            <p>
              We don't limit your creativity, which means you can order whatever
              you feel like. You can also choose from our menu containing over
              100 delicious meals. It's up to you!
            </p>
          </div>
        </div>
      </section>

      {/* --------------------- PHOTOS ---------------------- */}

      <section className="section-meals">
        <ul className="meals-showcase clearfix">
          <li>
            <figure className="fashion-photo">
              <img
                src={fashion}
                alt="Korean bibimbap with egg and vegetables"
              />
            </figure>
          </li>
          <li>
            <figure className="electronics-photo">
              <img
                src={electronics}
                alt="Simple italian pizza with cherry tomatoes"
              />
            </figure>
          </li>
          <li>
            <figure className="pets-photo">
              <img src={pets} alt="Chicken breast steak with vegetables" />
            </figure>
          </li>
          <li>
            <figure className="kitchen-photo">
              <img src={kitchen} alt="Autumn pumpkin soup" />
            </figure>
          </li>
        </ul>
      </section>

      {/* --------------------- CONNECT ---------------------- */}
      {/* <Register /> */}
      {/* <section className="section-form" id="connect">
        <div className="row">
          <h2>We're happy to hear from you</h2>
        </div>
        <div className="row">
          <form method="post" action="#" className="contact-form">
            <div className="row">
              <div className="col span-1-of-3">
                <label for="name">Name</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="text"
                  name="name"
                  id="name"
                  placeholder="Your name"
                  required
                />
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label for="email">Email</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="email"
                  name="email"
                  id="email"
                  placeholder="Your email"
                  required
                />
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label for="find-us">How did you find us?</label>
              </div>
              <div className="col span-2-of-3">
                <select name="find-us" id="find-us">
                  <option value="friends" selected>
                    Friends
                  </option>
                  <option value="search">Search engine</option>
                  <option value="ad">Advertisement</option>
                  <option value="other">Other</option>
                </select>
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>Newsletter?</label>
              </div>
              <div className="col span-2-of-3">
                <input type="checkbox" name="news" id="news" checked /> Yes,
                please
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>Drop us a line</label>
              </div>
              <div className="col span-2-of-3">
                <textarea name="message" placeholder="Your message"></textarea>
              </div>
            </div>
            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-2-of-3">
                <input type="submit" value="Send it!" />
              </div>
            </div>
          </form>
        </div>
      </section> */}

      {/* --------------------- RECCOMADITIONS ---------------------- */}

      {/* <section className="section-testimonials" id="recommanditions">
        <div className="row">
          <h2>Our customers can't live without us</h2>
        </div>
        <div className="row">
          <div className="col span-1-of-3">
            <blockquote>
              Omnifood is just awesome! I just launched a startup which leaves
              me with no time for cooking, so Omnifood is a life-saver. Now that
              I got used to it, I couldn't live without my daily meals!
              <img src="resources/img/customer-1.jpg" />
              Alberto Duncan
            </blockquote>
          </div>
          <div className="col span-1-of-3">
            <blockquote>
              Inexpensive, healthy and great-tasting meals, delivered right to
              my home. We have lots of food delivery here in Lisbon, but no one
              comes even close to Omifood. Me and my family are so in love!
              <img src="resources/img/customer-2.jpg" />
              Joana Silva
            </blockquote>
          </div>
          <div className="col span-1-of-3">
            <blockquote>
              I was looking for a quick and easy food delivery service in San
              Franciso. I tried a lot of them and ended up with Omnifood. Best
              food delivery service in the Bay Area. Keep up the great work!
              <img src="resources/img/customer-3.jpg" />
              Milton Chapman
            </blockquote>
          </div>
        </div>
      </section> */}

      {/* --------------------- PROGRAMERS PHOTOS ---------------------- */}

      {/* <section className="section-cities" id="programers">
        <div className="row">
          <h2>This system desigend by the greatest programers</h2>
        </div>
        <div className="row">
          <h1>ddd</h1>
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
              <a href="#">zeirah@post.bgu.ac.il</a>
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
              <a href="#">benhr@post.bgu.ac.il</a>
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
              <a href="#">amitrb@post.bgu.ac.il</a>
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
              <a href="#">reutle@post.bgu.ac.il</a>
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
              <a href="#">nofetd@post.bgu.ac.il</a>
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
              <a href="#">elinor@post.bgu.ac.il</a>
            </div>
          </div>
        </div>
      </section> */}

      {/* </body> */}
    </div>
  );
}

export default mainPage;
