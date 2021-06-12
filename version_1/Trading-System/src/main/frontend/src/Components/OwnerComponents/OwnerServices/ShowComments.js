import React, { useEffect, useState, Fragment } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function ShowComments(props) {
  const [commentsLists, setCommentsLists] = useState([]);

  async function fetchComments() {
    const commentsListResponse = await apiHttp.ShowStoreComments(
      props.connID,
      props.userID,
      props.storeID
    );
    console.log(commentsListResponse);

    if (commentsListResponse.isErr) {
      console.log(commentsListResponse.message);
    } else {
      setCommentsLists(commentsListResponse.returnObject.comments);
    }
  }

  useEffect(() => {
    fetchComments();
  }, [props.refresh]);

  return (
    <Fragment>
      <section className="section-form">
        {commentsLists && commentsLists.length > 0 ? (
          commentsLists.map((currComment, index) => (
            <li key={index} className="curr bid">
              <p>
                <strong>UserID: </strong> {currComment.userID}{" "}
              </p>
              <p>
                {" "}
                <strong>ProductID: </strong> {currComment.productID}{" "}
              </p>
              <p>
                {" "}
                <strong>Product name: </strong> {currComment.productName}{" "}
              </p>
              <p>
                {" "}
                <strong>Comment: </strong>
                {currComment.message}
              </p>
            </li>
          ))
        ) : (
          <p>No Comments waiting for you...</p>
        )}
      </section>
    </Fragment>
  );
}

export default ShowComments;
