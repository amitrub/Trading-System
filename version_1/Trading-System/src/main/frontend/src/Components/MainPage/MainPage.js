import "../Navbar/Navbar.css";
import "../../Design/grid.css";
import "../../Design/style.css";
import fashion from "../img/sadna-fashion.jpeg";
import electronics from "../img/sadna-elctorincs.jpeg";
import pets from "../img/sadna-dogs.jpeg";
import kitchen from "../img/sadna-kitchen.jpeg";

function mainPage(props) {
  return (
    <div>
      {/* <head> */}
      {/* <meta name="viewport" content="width=device-width, initial-scale=1.0" />

      <link
        rel="stylesheet"
        type="text/css"
        href="../../Design/normalize.css"
      />
      <link rel="stylesheet" type="text/css" href="../../Design/grid.css" />
      <link
        rel="stylesheet"
        type="text/css"
        href="vendors/css/ionicons.min.css"
      />
      <link rel="stylesheet" type="text/css" href="../../Design/animate.css" />
      <link rel="stylesheet" type="text/css" href="../../Design/style.css" />
      <link rel="stylesheet" type="text/css" href="../../Design/queries.css" />
      <link
        href="http://fonts.googleapis.com/css?family=Lato:100,300,400,300italic"
        rel="stylesheet"
        type="text/css"
      />
      <title>AliBamba</title> */}
      {/* </head> */}

      {/* --------------------- HEADER ---------------------- */}
      {/* <body> */}
      <header>
        <nav>
          <div className="row">
            {/* <img src="resources/img/online-shopping-logo.jpeg" alt="Omnifood logo" className="logo"> </img> */}
            <ul className="main-nav js--main-nav" id="home">
              <li>
                <a href="#">hello {props.username}</a>
              </li>
              <li>
                <a href="#stores">stores</a>
              </li>
              <li>
                <a href="#owners">owners</a>
              </li>
              <li>
                <a href="#shoppingcart">shopping cart</a>
              </li>
              <li>
                <a href="#programers">about us</a>
              </li>
              <li>
                <a href="#recommanditions">recommanditions</a>
              </li>
            </ul>
            <a className="mobile-nav-icon js--nav-icon" href="//#region">
              <i className="ion-navicon-round"></i>
            </a>
          </div>
        </nav>
        <div className="hero-text-box">
          <h1>
            <strong>Welcome to AliBamba.</strong>
          </h1>
          <h1>the best trading system for you</h1>
          <a
            className="btn btn-full js--scroll-to-plans"
            href="#stores"
            // onSubmit={getStores()}
          >
            Let's shop!
          </a>
          <a className="btn btn-ghost js--scroll-to-start" href="#sign">
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

      {/* </body> */}

      {/* <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
      <script src="//cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
      <script src="//cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.jsdelivr.net/selectivizr/1.0.3b/selectivizr.min.js"></script>
      <script src="vendors/js/jquery.waypoints.min.js"></script>
      <script src="resources/js/script.js"></script> */}
    </div>
  );
}

export default mainPage;
