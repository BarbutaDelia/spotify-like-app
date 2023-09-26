import { useNavigate } from "react-router-dom";
import React, { useEffect } from 'react';

const AddSong = ({ jwt }) => {
  const navigate = useNavigate();
  useEffect(() => {
    if (!jwt) {
      navigate("/")
    }
  }, [jwt, navigate])
  const handleSubmit = (event) => {
    event.preventDefault();
    var { name, genre, year_of_release, type, album_id } = document.forms[0];
    name = name.value;
    genre = genre.value;
    year_of_release = year_of_release.value;
    type = type.value;
    album_id = album_id.value;
    if (album_id === "") {
      album_id = null;
    }
    // console.log(name + genre + year_of_release + type + album_id);
    const requestOptions = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': jwt
      },
      body: JSON.stringify({
        name: name,
        music_genre: genre,
        year_of_release: year_of_release,
        music_type: type,
        album_id: album_id
      })
    };
    fetch("http://localhost:8080/api/songs", requestOptions)
      .then((response) => {
        if (response.status === 201) {
          alert("Succesfully added song!")
        }
        if (response.status === 401) {
          navigate("/");
        }
        if (response.status === 403) {
          navigate("/error");
        }
        if (response.status === 406) {
          alert("Wrong representation!")
        }
        if (response.status === 409) {
          response.text().then(text => alert(text));
        }
        if (response.status === 422) {
          response.text().then(text => alert(text));
        }
      })
  }
  return (
    <div className="login-form">
      <form onSubmit={handleSubmit}>
        <div className="input-container">
          <label>Name</label>
          <input type="text" name="name" required />
          {/* {renderErrorMessage("credentials")} */}
        </div>
        <div className="input-container">
          <label>Choose a genre:</label>
          <select className="custom-select" id="genre" name="genre">
            <option value="rock">rock</option>
            <option value="pop">pop</option>
            <option value="metal">metal</option>
            <option value="house">house</option>
          </select>
        </div>
        <div className="input-container">
          <label>Year Of Release</label>
          <input type="number" name="year_of_release" />
          {/* {renderErrorMessage("pass")} */}
        </div>
        <div className="input-container">
          <label>Choose a type:</label>
          <select className="custom-select" id="type" name="type">
            <option value="album">album</option>
            <option value="song">song</option>
            <option value="single">single</option>
          </select>
          {/* {renderErrorMessage("pass")} */}
        </div>
        <div className="input-container">
          <label>Album Id</label>
          <input type="number" name="album_id" />
          {/* {renderErrorMessage("pass")} */}
        </div>
        <div className="button-container">
          <input type="submit" />
        </div>
      </form>
    </div>
  );
}
export default AddSong;