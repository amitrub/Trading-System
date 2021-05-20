import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import Product from "../Stores/Product";

const apiHttp = createApiClientHttp();

function Search(props) {
  const [searchedProducts, setSearchedProducts] = useState([]);
  const [showSearchedProducts, setShowSearchedProducts] = useState(false);

  const [ProductName, setProductName] = useState(true);
  const [ProductCategory, setProductCategory] = useState(false);
  const [searchData, setSearchData] = useState("");
  const [searchMinPrice, setSearchMinPrice] = useState(-1);
  const [searchMaxPrice, setSearchMaxPrice] = useState(-1);
  const [searchPRank, setSearchPRank] = useState(-1);
  const [searchSRank, setSearchSRank] = useState(-1);

  let prodKey = 1;

  function submitSwitchVis() {
    setProductName(ProductName ? false : true);
    setProductCategory(ProductCategory ? false : true);
  }
  function updateSearchData(event) {
    setSearchData(event.target.value);
  }
  function updateSearchMinPrice(event) {
    const min = event.target.value;
    const minFloat = parseFloat(min);
    if (minFloat) {
      setSearchMinPrice(minFloat);
    } else {
      setSearchMinPrice(-1);
    }
  }
  function updateSearchMaxPrice(event) {
    const max = event.target.value;
    const maxFloat = parseFloat(max);
    if (maxFloat) {
      setSearchMaxPrice(maxFloat);
    } else {
      setSearchMaxPrice(-1);
    }
  }
  function updateSearchPRank(event) {
    let val = -1;
    switch (event.target.value) {
      case "1":
        val = 1;
        break;
      case "2":
        val = 2;
        break;
      case "3":
        val = 3;
        break;
      case "4":
        val = 4;
        break;
      case "5":
        val = 5;
        break;
      default:
        val = -1;
        break;
    }
    setSearchPRank(val);
  }
  function updateSearchSRank(event) {
    let val = -1;
    switch (event.target.value) {
      case "1":
        val = 1;
        break;
      case "2":
        val = 2;
        break;
      case "3":
        val = 3;
        break;
      case "4":
        val = 4;
        break;
      case "5":
        val = 5;
        break;
      default:
        val = -1;
        break;
    }
    setSearchSRank(val);
  }

  async function submitSearchHandler(event) {
    event.preventDefault();

    setShowSearchedProducts(true);
    props.onRefresh();
  }

  async function fetchSearchedProducts() {
    const searchResponse = await apiHttp.Search(
      searchData,
      ProductName,
      ProductCategory,
      searchMinPrice,
      searchMaxPrice,
      searchPRank,
      searchSRank
    );

    // console.log(searchResponse);

    if (searchResponse.isErr) {
      console.log(searchResponse.message);
    } else {
      setSearchedProducts(searchResponse.returnObject.products);
    }
  }

  useEffect(() => {
    fetchSearchedProducts();
  }, [props.refresh]);

  return (
    <section className="section-form">
      <div className="row">
        <h2>Search mode</h2>
      </div>
      <div className="row">
        <form
          method="post"
          // action="#"
          className="contact-form"
          onSubmit={submitSearchHandler}
        >
          {/* Checkbox Search By */}
          <div className="row">
            <div className="col span-1-of-3">
              <label>Search By ...</label>
            </div>
            <div className="col span-2-of-3">
              <input
                type="checkbox"
                name="ProductName"
                id="ProductName"
                checked={ProductName}
                onChange={submitSwitchVis}
              />{" "}
              Name?{"      "}
              <input
                type="checkbox"
                name="ProductName"
                id="ProductName"
                checked={ProductName ? false : true}
                onChange={submitSwitchVis}
              />{" "}
              Category?
            </div>
          </div>
          {/* input search */}
          <div className="row">
            <div className="col span-1-of-3">
              <label htmlFor="name">Name / Category</label>
            </div>
            <div className="col span-2-of-3">
              <input
                type="text"
                name="search"
                id="search"
                required
                onChange={updateSearchData}
                value={searchData}
              />
            </div>
          </div>
          {/* min price search */}
          <div className="row">
            <div className="col span-1-of-3">
              <label htmlFor="name">min price</label>
            </div>
            <div className="col span-2-of-3">
              <input
                type="text"
                name="minPrice"
                id="minPrice"
                placeholder="type positive number or leave empty. etc 2.7"
                onChange={updateSearchMinPrice}
                value={searchMinPrice}
              />
            </div>
          </div>
          {/* max price search */}
          <div className="row">
            <div className="col span-1-of-3">
              <label htmlFor="name">max price</label>
            </div>
            <div className="col span-2-of-3">
              <input
                type="text"
                name="maxPrice"
                id="maxPrice"
                placeholder="type positive number or leave empty. etc 10.5"
                onChange={updateSearchMaxPrice}
                value={searchMaxPrice}
              />
            </div>
          </div>
          {/* product rank search */}
          <div className="row">
            <div className="col span-1-of-3">
              <label htmlFor="name">product rank</label>
            </div>
            <div className="col span-2-of-3">
              <select
                name="product-rank"
                id="product-rank"
                onChange={updateSearchPRank}
              >
                <option value="">none</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
            </div>
          </div>
          {/* store rank search */}
          <div className="row">
            <div className="col span-1-of-3">
              <label htmlFor="name">store rank</label>
            </div>
            <div className="col span-2-of-3">
              <select
                name="product-rank"
                id="product-rank"
                onChange={updateSearchSRank}
              >
                <option value="">none</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
            </div>
          </div>
          <div className="row">
            <div className="col span-1-of-3">
              <label>&nbsp;</label>
            </div>
            <div className="col span-2-of-3">
              <input type="submit" value="search" />
            </div>
          </div>
        </form>
      </div>

      {/* Show Searched Product */}
      {showSearchedProducts ? (
        searchedProducts.map((currProduct) => (
          <div className="col span-1-of-4">
            <li key={prodKey++} className="curr product">
              <Product
                refresh={props.refresh}
                onRefresh={props.onRefresh}
                connID={props.connID}
                currProduct={currProduct}
              ></Product>
            </li>
          </div>
        ))
      ) : (
        <div className="row">
          <h3>searching for you...</h3>
        </div>
      )}
    </section>
  );
}

export default Search;
