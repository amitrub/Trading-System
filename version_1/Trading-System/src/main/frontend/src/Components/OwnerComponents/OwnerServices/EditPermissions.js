import React, { useState, useEffect } from "react";
import createApiClientHttp from "../../../ApiClientHttp";
import "../../../Design/grid.css";
import "../../../Design/style.css";
import MyPopup from "../../OtherComponents/MyPopup/MyPopup";

const apiHttp = createApiClientHttp();

function EditPermissions(props) {
  //All permissioms
  const [addProduct, setAddProduct] = useState(false);
  const [reduceProduct, setReduceProduct] = useState(false);
  const [deleteProduct, setDeleteProduct] = useState(false);
  const [editProduct, setEditProduct] = useState(false);
  const [appointmentOwner, setAppointmentOwner] = useState(false);
  const [appointmentManager, setAppointmentManager] = useState(false);
  const [editManagerPermission, setEditManagerPermission] = useState(false);
  const [removeManager, setRemoveManager] = useState(false);
  const [getInfoOfficials, setGetInfoOfficials] = useState(false);
  const [getInfoRequests, setGetInfoRequests] = useState(false);
  const [responseRequests, setResponseRequests] = useState(false);
  const [getStoreHistory, setGetStoreHistory] = useState(false);

  const [managerID, setManagerID] = useState(-1);
  const [popupPermissions, setPopupPermissions] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  async function submitEditPermissionsHandler(event) {
    event.preventDefault();
    console.log("before EditPermissions");
    // console.log(permissions);

    const editPermissionsResponse = await apiHttp.EditManagerPermissions(
      props.connID,
      props.userID,
      props.storeID,
      managerID,
      addProduct,
      reduceProduct,
      deleteProduct,
      editProduct,
      appointmentOwner,
      appointmentManager,
      editManagerPermission,
      removeManager,
      getInfoOfficials,
      getInfoRequests,
      responseRequests,
      getStoreHistory
    );

    // console.log("EditPermissions");
    // console.log(editPermissionsResponse);

    if (editPermissionsResponse) {
      setPopupMsg(editPermissionsResponse.message);
      setPopupPermissions(true);
    }
    if (editPermissionsResponse.isErr) {
      console.log(editPermissionsResponse.message);
    }
    props.onRefresh();
  }

  function updateManagerID(event) {
    setManagerID(event.target.value);
  }

  function onClosePopup() {
    setPopupPermissions(false);
    props.onRefresh();
  }

  return (
    <section>
      <div>
        <div className="row">
          <h2>Edit Manager Permissions</h2>
          <h4>
            type ID of the manager and choose the permissions you want for
            him...
          </h4>
          <div className="row">-----------</div>
        </div>

        <div className="row">
          <form
            method="post"
            className="contact-form"
            onSubmit={submitEditPermissionsHandler}
          >
            {/* TODO: CHANGE TO ROLL THE EXIST MANAGERS */}
            {/* Manager ID */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Manager ID</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="number"
                  name="ID"
                  id="ID"
                  required
                  onChange={updateManagerID}
                  placeholder={"CHANGE TO ENROLL FROM THE EXISTING MANAGERS"}
                />
              </div>
            </div>

            {/* Checkbox Add Product */}
            {/* {permissions.includes("AddProduct") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Add Product</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="addProduct"
                  id="addProduct"
                  checked={addProduct}
                  onChange={() => setAddProduct(!addProduct)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox Reduce Product */}
            {/* {permissions.includes("ReduceProduct") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Reduce Product</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="reduceProduct"
                  id="reduceProduct"
                  checked={reduceProduct}
                  onChange={() => setReduceProduct(!reduceProduct)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox Delete Product */}
            {/* {permissions.includes("DeleteProduct") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Delete Product</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="deleteProduct"
                  id="deleteProduct"
                  checked={deleteProduct}
                  onChange={() => setDeleteProduct(!deleteProduct)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox Edit Product */}
            {/* {permissions.includes("EditProduct") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Edit Product</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="editProduct"
                  id="editProduct"
                  checked={editProduct}
                  onChange={() => setEditProduct(!editProduct)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox Appointment Owner */}
            {/* {permissions.includes("AppointmentOwner") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Appointment Owner</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="AppointmentOwner"
                  id="AppointmentOwner"
                  checked={appointmentOwner}
                  onChange={() => setAppointmentOwner(!appointmentOwner)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox Appointment Manager */}
            {/* {permissions.includes("AppointmentManager") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Appointment Manager</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="appointmentManager"
                  id="appointmentManager"
                  checked={appointmentManager}
                  onChange={() => setAppointmentManager(!appointmentManager)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox Edit Manager Persmissions*/}
            {/* {permissions.includes("EditManagerPermission") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Edit Manager Persmissions</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="editManagerPersmission"
                  id="editManagerPersmission"
                  checked={editManagerPermission}
                  onChange={() =>
                    setEditManagerPermission(!editManagerPermission)
                  }
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox Remove Manager*/}
            {/* {permissions.includes("RemoveManager") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Remove Manager</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="removeManager"
                  id="removeManager"
                  checked={removeManager}
                  onChange={() => setRemoveManager(!removeManager)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox GetInfoOfficials*/}
            {/* {permissions.includes("GetInfoOfficials") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Get Info Officials</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="getInfoOfficials"
                  id="GetInfoOfficials"
                  checked={getInfoOfficials}
                  onChange={() => setGetInfoOfficials(!getInfoOfficials)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox GetInfoRequests*/}
            {/* {permissions.includes("GetInfoRequests") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Get Info Requests</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="getInfoRequests"
                  id="GetInfoRequests"
                  checked={getInfoRequests}
                  onChange={() => setGetInfoRequests(!getInfoRequests)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox GetStoreHistory*/}
            {/* {permissions.includes("GetStoreHistory") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Get Store History</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="getStoreHistory"
                  id="GetStoreHistory"
                  checked={getStoreHistory}
                  onChange={() => setGetStoreHistory(!getStoreHistory)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            {/* Checkbox ResponseRequests*/}
            {/* {permissions.includes("ResponseRequests") ? ( */}
            <div className="row">
              <div className="col span-1-of-3">
                <label htmlFor="name">Response Requests</label>
              </div>
              <div className="col span-2-of-3">
                <input
                  type="checkbox"
                  name="responseRequests"
                  id="ResponseRequests"
                  checked={responseRequests}
                  onChange={() => setResponseRequests(!responseRequests)}
                />
              </div>
            </div>
            {/* ) : (
              ""
            )} */}

            <div className="row">
              <div className="col span-1-of-3">
                <label>&nbsp;</label>
              </div>
              <div className="col span-1-of-3">
                <input type="submit" value="Update Permissions!" />
              </div>
            </div>
          </form>
        </div>
      </div>
      {popupPermissions ? (
        <MyPopup errMsg={popupMsg} onClosePopup={onClosePopup}></MyPopup>
      ) : (
        ""
      )}
    </section>
  );
}

export default EditPermissions;
