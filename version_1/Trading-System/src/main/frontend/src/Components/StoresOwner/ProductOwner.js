import React, { Component } from "react";
import "../../Design/grid.css";
import "../../Design/style.css";

class ProductOwner extends Component {
  constructor(props) {
    super(props);

    this.state = {
      connID: "",
      userID: -1,
      storeID: -1,
      storeName: "",
      productID: -1,
      productName: "",
      price: -1,
      quantity: 0,
      category: "",
    };
  }

  componentDidMount() {
    const {
      connID,
      userID,
      storeID,
      storeName,
      storeRate,
      product,
    } = this.props;
    this.setState({
      connID: connID,
      userID: userID,
      storeID: storeID,
      storeName: storeName,
      storeRate: storeRate,
      productID: product.productID,
      productName: product.productName,
      price: product.price,
      quantity: product.quantity,
      category: product.category,
    });
  }

  submitBuyProductHandler = () => {
    console.log("ProductOwner");
  };
  render() {
    const {
      connID,
      userID,
      storeID,
      storeName,
      productID,
      productName,
      price,
      quantity,
      category,
    } = this.state;
    return (
      <div className="plan-box">
        <div>
          <h3>{productName}</h3>
          <p className="plan-price">${price}</p>
          <p className="plan-price-meal">
            {quantity} units on '{storeName}'
          </p>
        </div>
        <div>
          <p>Category: {category}</p>
        </div>
        <div>
          <form
            onSubmit={this.submitBuyProductHandler}
            method="post"
            action="#"
          >
            <div className="">
              <input
                type="number"
                name="quantity"
                id="quantity"
                placeholder="enter quantity"
                required
                min="1"
              />
            </div>
            <div className="btn btn-ghost">
              <input type="submit" value="Buy" />
            </div>
          </form>
        </div>
      </div>
    );
  }
}
export default ProductOwner;
