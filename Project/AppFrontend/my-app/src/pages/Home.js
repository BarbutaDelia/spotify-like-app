import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from 'react';
import jwt_decode from 'jwt-decode';
var XMLParser = require('react-xml-parser');
const Home = ({ jwt }) => {
  const navigate = useNavigate();
  const [decoded, setDecoded] = useState('');
  const [hasProfile, setHasProfile] = useState(false);
  useEffect(() => {
    // console.log("Aici am jwt ul" + jwt)
    if (!jwt) {
      navigate("/")
    }
    else {
      setDecoded(jwt_decode(jwt))
    }
    fetch('http://localhost:8081/api/profiles')
      .then((response) => response.json())
      .then((data) => {
        var userIdsWithProfiles = [];
        for (const profile of data[0]) {
          userIdsWithProfiles.push(profile.user_id);
        }
        const jwsUserId = jwt_decode(jwt).sub;
        for (const id of userIdsWithProfiles) {
          if (id.toString() === jwsUserId) {
            setHasProfile(true);
          }
        }
      })
      .catch((err) => {
        alert("An error has occured! Please try again later!")
        console.log(err.message);
      });
  }, [jwt, navigate])

  const handleSubmit = (event) => {
    //Prevent page reload
    event.preventDefault();

    logout(jwt).then(function (res) {
      // console.log(res)
      var xml = new XMLParser().parseFromString(res);
      var logoutResponse = xml.getElementsByTagName("tns:logoutResult")[0].value;
      // console.log(logoutResponse)
      if (logoutResponse.includes("Error")) {
        alert("An error occured! Please try again!")
      }
      else {
        // setJwt(autheticationResponse);
        // const decoded = jwt_decode(autheticationResponse); 
        //probabil in functie de rolul asta ar trebui directionat utilizatorul pe pagina corespunzatoare
        navigate("/");
      }
    });
  }

  const handleSubmitGetSongs = (event) => {
    event.preventDefault();
    navigate("/songs")
  }

  const handleSubmitAddArtist = (event) => {
    event.preventDefault();
    navigate("/addArtist")
  }

  const handleSubmitAddSong = (event) => {
    event.preventDefault();
    navigate("/addSong")
  }

  const handleSubmitAddProfile = (event) => {
    event.preventDefault();
    navigate("/addProfile")
  }

  const handleSubmitAddPlaylist = (event) => {
    event.preventDefault();
    navigate("/addPlaylist")
  }

  const handleSubmitViewPlaylists = (event) => {
    event.preventDefault();
    navigate("/viewPlaylists")
  }

  if (decoded !== "") {
    var role = decoded.role;
    // console.log(role);
    if (role.includes("content manager")) {
      return (
        <div className="home-container">
          <form className="logout-container" onSubmit={handleSubmitGetSongs}>
            <div >
              <input type="submit" value="View songs" />
            </div>
          </form>
          <form className="logout-container" onSubmit={handleSubmitAddArtist}>
            <div>
              <input type="submit" value="Add/Modify artist" />
            </div>
          </form>
          <form className="logout-container" onSubmit={handleSubmit}>
            <div>
              <input type="submit" value="Logout" />
            </div>
          </form>
        </div>
      );
    }
    if (role.includes("artist")) {
      return (
        <div className="home-container">
          <form className="logout-container" onSubmit={handleSubmitGetSongs}>
            <div >
              <input type="submit" value="View songs" />
            </div>
          </form>
          <form className="logout-container" onSubmit={handleSubmitAddSong}>
            <div>
              <input type="submit" value="Add song" />
            </div>
          </form>
          <form className="logout-container" onSubmit={handleSubmit}>
            <div>
              <input type="submit" value="Logout" />
            </div>
          </form>
        </div>
      );
    }
    if (role.includes("client") && !hasProfile) {
      return (
        <div className="home-container">
          <form onSubmit={handleSubmitGetSongs}>
            <div className="logout-container">
              <input type="submit" value="View songs" />
            </div>
          </form>
          <form onSubmit={handleSubmitAddProfile}>
            <div className="logout-container">
              <input type="submit" value="Add profile" />
            </div>
          </form>
          <form onSubmit={handleSubmit}>
            <div className="logout-container">
              <input type="submit" value="Logout" />
            </div>
          </form>
        </div>
      );
    }
    if (role.includes("client") && hasProfile) {
      return (
        <div className="home-container">
          <form onSubmit={handleSubmitGetSongs}>
            <div className="logout-container">
              <input type="submit" value="View songs" />
            </div>
          </form>
          <form onSubmit={handleSubmitAddPlaylist}>
            <div className="logout-container">
              <input type="submit" value="Add playlist" />
            </div>
          </form>
          <form onSubmit={handleSubmitViewPlaylists}>
            <div className="logout-container">
              <input type="submit" value="View playlists" />
            </div>
          </form>
          <form onSubmit={handleSubmit}>
            <div className="logout-container">
              <input type="submit" value="Logout" />
            </div>
          </form>
        </div>
      );
    }
    else {
      return (
        <div>
          <form onSubmit={handleSubmitGetSongs}>
            <div className="logout-container">
              <input type="submit" value="View songs" />
            </div>
          </form>
          <form onSubmit={handleSubmit}>
            <div className="logout-container">
              <input type="submit" value="Logout" />
            </div>
          </form>
        </div>
      );
    }
  }
}

function logout(jwt) {
  return new Promise(function (resolve, reject) {
    var sr = '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sample="services.dbManager.soap">' +
      '<soap11env:Body>' +
      '<sample:logout>' +
      '<sample:token>' + jwt + '</sample:token>' +
      '</sample:logout>' +
      '</soap11env:Body>' +
      '</soap11env:Envelope>';
    var xmlhttp = new XMLHttpRequest();
    var url = "http://127.0.0.1:8000"
    xmlhttp.open('POST', url);
    xmlhttp.onload = resolve;
    xmlhttp.onerror = reject;
    xmlhttp.send(sr);
    xmlhttp.onload = () => {
      if (xmlhttp.status === 200) {
        resolve(xmlhttp.response)
      }
    }
  });
}
export default Home;