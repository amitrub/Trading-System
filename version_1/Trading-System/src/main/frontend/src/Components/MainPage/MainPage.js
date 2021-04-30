
import './MainPageDesign/style.css'
import './MainPageDesign/grid.css'
import fashion from '../img/sadna-fashion.jpeg'
import electronics from '../img/sadna-elctorincs.jpeg'
import pets from '../img/sadna-dogs.jpeg'
import kitchen from '../img/sadna-kitchen.jpeg'
import hadas from '../img/develops/hadas.jpeg'
import roee from '../img/develops/roee.jpeg'
import amit from '../img/develops/rubin.jpeg'
import reut from '../img/develops/reut.jpeg'
import nofet from '../img/develops/nofet.jpeg'
import elinor from '../img/develops/elinor.jpeg'




function mainPage(){

    return (
        <div>

        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        
        <link rel="stylesheet" type="text/css" href="vendors/css/normalize.css"/>
        <link rel="stylesheet" type="text/css" href="vendors/css/grid.css"/> 
        <link rel="stylesheet" type="text/css" href="vendors/css/ionicons.min.css"/> 
        <link rel="stylesheet" type="text/css" href="vendors/css/animate.css"/> 
        <link rel="stylesheet" type="text/css" href="resources/css/style.css"/> 
        <link rel="stylesheet" type="text/css" href="resources/css/queries.css"/> 
        <link href='http://fonts.googleapis.com/css?family=Lato:100,300,400,300italic' rel='stylesheet' type='text/css' /> 
        <title>Omnifood</title>
        </head>

        {/* --------------------- HEADER ---------------------- */}
    <body>
        <header>
            <nav>
                <div className="row">
                    {/* <img src="resources/img/online-shopping-logo.jpeg" alt="Omnifood logo" class="logo"> </img> */}
                    <ul class="main-nav js--main-nav">
                        <li><a href="#features">cooking classes</a></li>
                        <li><a href="#works">blog</a></li>
                        <li><a href="#cities">gellery</a></li>
                        <li><a href="#plans">recommanditions</a></li>
                    </ul>
                    <a className="mobile-nav-icon js--nav-icon"><i class="ion-navicon-round"></i></a>
                </div>
            </nav>
            <div className="hero-text-box">
                <h1><strong>welcome to Ali - Bamba.</strong></h1>
                <h1>the best trading system for you</h1>
                <a className="btn btn-full js--scroll-to-plans" href="#">Let's shop!</a>
                <a className="btn btn-ghost js--scroll-to-start" href="#"><strong>Connect</strong></a>
            </div>

        </header>

        {/* --------------------- FEATURES ---------------------- */}

        <section className="section-features js--section-features" id="features">
            <div className="row">
                <h2>Shop now &mdash; Get it tomorrow</h2>
                <p className="long-copy">
                    Hello, we're AliBamba, your new trading system service. We know you're always busy. No time for going to shopping malls. So let us take care of that, we're really good at it, we promise!
                </p>
            </div>
            
            <div className="row js--wp-1">
                <div className="col span-1-of-4 box">
                    <i className="ion-ios-infinite-outline icon-big"></i>
                    <h3>Up to 365 days/year</h3>
                    <p>
                        Never cook again! We really mean that. Our subscription plans include up to 365 days/year coverage. You can also choose to order more flexibly if that's your style.
                    </p>
                </div>
                <div className="col span-1-of-4 box">
                    <i className="ion-ios-stopwatch-outline icon-big"></i>
                    <h3>Ready in 20 minutes</h3>
                    <p>
                        You're only twenty minutes away from your delicious and super healthy meals delivered right to your home. We work with the best chefs in each town to ensure that you're 100% happy.
                    </p>
                </div>
                <div className="col span-1-of-4 box">
                    <i className="ion-ios-nutrition-outline icon-big"></i>
                    <h3>100% organic</h3>
                    <p>
                        All our vegetables are fresh, organic and local. Animals are raised without added hormones or antibiotics. Good for your health, the environment, and it also tastes better!
                    </p>
                </div>
                <div className="col span-1-of-4 box">
                    <i className="ion-ios-cart-outline icon-big"></i>
                    <h3>Order anything</h3>
                    <p>
                        We don't limit your creativity, which means you can order whatever you feel like. You can also choose from our menu containing over 100 delicious meals. It's up to you!
                    </p>
                </div>
            </div>   
        </section>

        {/* --------------------- PHOTOS ---------------------- */}

        <section className="section-meals">
            <ul className="meals-showcase clearfix">
                <li>
                    <figure className="fashion-photo">
                        <img src={fashion} alt="Korean bibimbap with egg and vegetables" />
                    </figure>
                </li>
                <li>
                    <figure className="electronics-photo">
                        <img src={electronics} alt="Simple italian pizza with cherry tomatoes" />
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

        {/* --------------------- PROGRAMERS PHOTOS ---------------------- */}


        <section class="section-cities" id="cities">
            <div class="row">
                <h2>This system desigend by the greatest programers</h2>
            </div>
            <div class="row js--wp-3">
                <div class="col span-1-of-6 box">
                    <img src={hadas} alt="Lisbon" />
                    <h3>Hadas</h3>
                    <div class="city-feature">
                        <i class="ion-ios-person icon-small"></i>
                        2400+ lines of code
                    </div>
                    <div class="city-feature">
                        <i class="ion-ios-star icon-small"></i>
                        web desiner 
                    </div>
                    <div class="city-feature">
                        <i class="ion-social-twitter icon-small"></i>
                        <a href="#">zeirah@post.bgu.ac.il</a>
                    </div>
                </div>
                <div class="col span-1-of-6 box">
                    <img src={roee} alt="San Francisco" />
                    <h3>Roee</h3>
                    <div class="city-feature">
                        <i class="ion-ios-person icon-small"></i>
                        3200+ lines of code
                    </div>
                    <div class="city-feature">
                        <i class="ion-ios-star icon-small"></i>
                        client side
                    </div>
                    <div class="city-feature">
                        <i class="ion-social-twitter icon-small"></i>
                        <a href="#">benhr@post.bgu.ac.il</a>
                    </div>
                </div>
                <div class="col span-1-of-6 box">
                    <img src={amit} alt="Berlin" />
                    <h3>Amit</h3>
                    <div class="city-feature">
                        <i class="ion-ios-person icon-small"></i>
                        3400+ lines of code
                    </div>
                    <div class="city-feature">
                        <i class="ion-ios-star icon-small"></i>
                        communication 
                    </div>
                    <div class="city-feature">
                        <i class="ion-social-twitter icon-small"></i>
                        <a href="#">amitrb@post.bgu.ac.il</a>
                    </div>
                </div>
                <div class="col span-1-of-6 box">
                    <img src={reut} alt="London" />
                    <h3>Reut</h3>
                    <div class="city-feature">
                        <i class="ion-ios-person icon-small"></i>
                        2600+ lines of code
                    </div>
                    <div class="city-feature">
                        <i class="ion-ios-star icon-small"></i>
                        version manager
                    </div>
                    <div class="city-feature">
                        <i class="ion-social-twitter icon-small"></i>
                        <a href="#">reutle@post.bgu.ac.il</a>
                    </div>
                </div>
                <div class="col span-1-of-6 box">
                    <img src={nofet} alt="London" />
                    <h3>Nofet</h3>
                    <div class="city-feature">
                        <i class="ion-ios-person icon-small"></i>
                        3100+ lines of code
                    </div>
                    <div class="city-feature">
                        <i class="ion-ios-star icon-small"></i>
                        server side
                    </div>
                    <div class="city-feature">
                        <i class="ion-social-twitter icon-small"></i>
                        <a href="#">nofetd@post.bgu.ac.il</a>
                    </div>
                </div>
                <div class="col span-1-of-6 box">
                    <img src={elinor} alt="London" />
                    <h3>Elinor</h3>
                    <div class="city-feature">
                        <i class="ion-ios-person icon-small"></i>
                        2400+ lines of code
                    </div>
                    <div class="city-feature">
                        <i class="ion-ios-star icon-small"></i>
                        testing and QA
                    </div>
                    <div class="city-feature">
                        <i class="ion-social-twitter icon-small"></i>
                        <a href="#">elinor@post.bgu.ac.il</a>
                    </div>
                </div>
            </div>
            
        </section>




        </body>

        </div>
    )
}

export default mainPage;

