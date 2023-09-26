import { useNavigate } from "react-router-dom";
import React, {useEffect } from 'react';

const AddArtist = ({ jwt }) => {
   const navigate = useNavigate();
   useEffect(() => {
      // console.log(jwt)
      if (!jwt) {
         navigate("/")
      }
   }, [jwt, navigate])
   const handleSubmit = (event) => {
      event.preventDefault();
      var { uuid, name, isActive } = document.forms[0];

      uuid = uuid.value;
      name = name.value;
      isActive = isActive.checked;

      // console.log(uuid + name + isActive);
      const requestOptions = {
         method: 'PUT',
         headers: {
            'Content-Type': 'application/json',
            'Authorization': jwt 
         },
         body: JSON.stringify({
            name: name,
            isActive: isActive
         })
      };
      fetch('http://localhost:8080/api/artists/' + uuid, requestOptions)
         .then((response) => {
            if (response.status === 204) {
               alert("Succesfully modified artist!")
            }
            if (response.status === 201) {
               alert("Succesfully added artist!")
            }
            if (response.status === 401) {
               navigate("/");
            }
            if (response.status === 403) {
               navigate("/error");
            }
         })
   }
   return (
      <div className="login-form">
         <form onSubmit={handleSubmit}>
            <div className="input-container">
               <label>Uuid</label>
               <input type="text" name="uuid" required />
               {/* {renderErrorMessage("credentials")} */}
            </div>
            <div className="input-container">
               <label>Name</label>
               <input type="text" name="name" required />
               {/* {renderErrorMessage("pass")} */}
            </div>
            <div className="input-container">
               <label> Is Active</label>
               <input type="checkbox" id="checkbox" name="isActive" value="isActive" />
               {/* {renderErrorMessage("pass")} */}
            </div>
            <div className="button-container">
               <input type="submit" />
            </div>
         </form>
      </div>
   );
}
export default AddArtist;