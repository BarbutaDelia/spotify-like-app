import { useNavigate } from "react-router-dom";
import React, { useEffect } from 'react';
import jwt_decode from 'jwt-decode';

const AddProfile = ({ jwt }) => {
  const navigate = useNavigate();
  useEffect(() => {
    if (!jwt) {
      navigate("/")
    }
  }, [jwt, navigate])
  const handleSubmit = (event) => {
    event.preventDefault();
    var { first_name, last_name, email } = document.forms[0];
    first_name = first_name.value;
    last_name = last_name.value;
    email = email.value;

    const decoded = jwt_decode(jwt);
    // console.log(first_name + last_name + email);
    const requestOptions = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': jwt
      },
      body: JSON.stringify({
        user_id: parseInt(decoded.sub),
        first_name: first_name,
        last_name: last_name,
        email: email,
        likedMusic_ids: [],
        playlist_ids: []
      })
    };
    fetch("http://localhost:8081/api/profiles", requestOptions)
      .then((response) => {
        if (response.status === 201) {
          alert("Profile successfully created!")
        }
        if (response.status === 406) {
          alert("The user id is invalid!")
        }
        if (response.status === 422) {
          alert("Liked music and playlists must not contain any elements!")
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
          <label>First Name</label>
          <input type="text" name="first_name" required />
          {/* {renderErrorMessage("credentials")} */}
        </div>
        <div className="input-container">
          <label>Last Name</label>
          <input type="text" name="last_name" required />
          {/* {renderErrorMessage("credentials")} */}
        </div>
        <div className="input-container">
          <label>Email</label>
          <input type="email" name="email" required />
          {/* {renderErrorMessage("credentials")} */}
        </div>
        <div className="button-container">
          <input type="submit" />
        </div>
      </form>
    </div>
  );
}
export default AddProfile;