import { useNavigate } from "react-router-dom";
import React, { useState, useEffect } from 'react';
import jwt_decode from 'jwt-decode';

// function makeAddPlaylistToProfileRequest(idProfile, idPlaylist, userId, token) {
//   return new Promise(function (resolve, reject) {
//     let xmlHttp = new XMLHttpRequest();
//     let message = JSON.stringify({
//       user_id: userId,
//       playlist_ids: [idPlaylist]
//     });
//     console.log(message);
//     let string = "http://localhost:8081/api/profiles/" + idProfile + "?operation=add"
//     console.log(string);
//     xmlHttp.open("PATCH", "http://localhost:8081/api/profiles/" + idProfile + "?operation=add", true);

//     xmlHttp.onreadystatechange = function () {
//       if (xmlHttp.readyState === 4) {
//         if (xmlHttp.status === 204) {
//           alert("Adaugarea playlist-ului la profile s-a efectuat cu succes!");
//           resolve(204);
//         }
//         else if (xmlHttp.status === 403) {
//           reject("Forbidden");
//         }
//         else if (xmlHttp.status === 401) {
//           console.log(xmlHttp.responseText);
//           // token-ul de login a expirat - se va face un relogin
//           if (xmlHttp.responseText.includes("token login")) {
//             reject("Expired");
//           }
//         }
//         else {
//           alert(xmlHttp.status + " - " + xmlHttp.responseText);
//         }
//       }
//     }
//     xmlHttp.setRequestHeader('Content-Type', 'application/json');
//     xmlHttp.setRequestHeader('Access-Control-Allow-Origin', '*');
//     xmlHttp.setRequestHeader('Authorization', token);
//     xmlHttp.send(message);
//   });
// }

const AddPlaylist = ({ jwt }) => {
  const navigate = useNavigate();
  const [profileId, setProfileId] = useState([]);
  useEffect(() => {
    if (!jwt) {
      navigate("/")
    }
    fetch('http://localhost:8081/api/profiles')
      .then((response) => response.json())
      .then((data) => {
        const decoded = jwt_decode(jwt);
        for (let elem of data[0]) {
          // console.log(elem)
          if (elem.user_id === parseInt(decoded.sub)) {
            setProfileId(elem.id);
          }
        }
      })
      .catch((err) => {
        console.log(err.message);
      });
  }, [jwt, navigate])
  const handleSubmit = (event) => {
    event.preventDefault();
    var { name, song_ids } = document.forms[0];
    name = name.value;
    song_ids = song_ids.value;

    song_ids = song_ids.split(",").map(x => parseInt(x));

    // console.log(name, song_ids);

    const decoded = jwt_decode(jwt);

    const requestOptions = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': jwt
      },
      body: JSON.stringify({
        user_id: parseInt(decoded.sub),
        name: name,
        song_ids: song_ids
      })
    };
    fetch("http://localhost:8081/api/playlists", requestOptions)
      .then((response) => {
        if (response.status === 409) {
          response.text().then(text => alert(text));
        }
        if (response.status === 401) {
          navigate("/");
        }
        if (response.status === 403) {
          navigate("/error");
        }
        else return response;
      })
      .then(async (data) => {
        var response = await (data.json());
        var playlistId = response.id;

        // makeAddPlaylistToProfileRequest(profileId, playlistId, parseInt(decoded.sub), jwt)
        //   .then(
        //     (res) => {
        //       console.log(res);
        //     }
        //   )
        //   .catch((err) => {
        //     console.log(err);
        //   });

        const requestOptions = {
          method: 'POST', // :(
          mode: 'cors',
          headers: { 
              'Content-Type': 'application/json',
              'Authorization': jwt,
              'Access-Control-Allow-Origin': '*'
          },
          body: JSON.stringify({ 
            user_id: parseInt(decoded.sub),
            playlist_ids: [playlistId]
          })
        };
        fetch("http://localhost:8081/api/profiles/" + profileId + "?operation=add", requestOptions)
        .then((response) => {
          if (response.status === 204) {
            alert("Playlist successfully created!")
          }
          if (response.status === 404) {
            alert("Profile not found!")
          }
          if (response.status === 401) {
            navigate("/");
          }
          if (response.status === 409) {
            response.text().then(text => alert(text));
          }
          if (response.status === 403) {
            navigate("/error");
          }
        })
      })
      .catch((error) => {
        console.log('error: ' + error);
      });
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
          <label>Song ids (separated by , )</label>
          <input type="text" name="song_ids" required />
          {/* {renderErrorMessage("credentials")} */}
        </div>
        <div className="button-container">
          <input type="submit" />
        </div>
      </form>
    </div>
  );
}
export default AddPlaylist;