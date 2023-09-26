import React, { useState } from "react";
import "./styles.css";
import Login from './pages/Login';
import Home from './pages/Home';
import Songs from './pages/Songs';
import AddArtist from "./pages/AddArtist";
import AddSong from "./pages/AddSong";
import AddProfile from "./pages/AddProfile";
import AddPlaylist from "./pages/AddPlaylist";
import ViewPlaylists from "./pages/ViewPlaylists";
import Error from "./pages/Error";
import { Routes, Route } from "react-router-dom";

// var XMLParser = require('react-xml-parser');

function App() {
  const [jwt, setJwt] = useState("");
  console.log(jwt);
  return (
    // <Login setJwt={setJwt}/>
    <Routes>
      <Route index element={<Login setJwt={setJwt}/>} />
      <Route path="home" element={<Home jwt={jwt}/>} />
      <Route path="songs" element={<Songs jwt={jwt}/>} />
      <Route path="addArtist" element={<AddArtist jwt={jwt}/>} />
      <Route path="addSong" element={<AddSong jwt={jwt}/>} />
      <Route path="addProfile" element={<AddProfile jwt={jwt}/>} />
      <Route path="addPlaylist" element={<AddPlaylist jwt={jwt}/>} />
      <Route path="viewPlaylists" element={<ViewPlaylists jwt={jwt}/>} />
      <Route path="error" element={<Error jwt={jwt}/>} />
    </Routes>
    
  );
}
  // React States
  // const [errorMessages, setErrorMessages] = useState({});
  // const [isSubmitted, setIsSubmitted] = useState(false);

  // // User Login info
  // const database = [
  //   {
  //     username: "user1",
  //     password: "pass1"
  //   },
  //   {
  //     username: "user2",
  //     password: "pass2"
  //   }
  // ];

  // const errors = {
  //   uname: "invalid username",
  //   pass: "invalid password"
  // };

//   const handleSubmit = (event) => {
//     //Prevent page reload
//     event.preventDefault();

//     var { uname, pass } = document.forms[0];

//     uname = uname.value;
//     pass = pass.value;

//     console.log(uname + pass);

//     login(uname,pass).then(function(res){
//       // console.log(res)
//       var xml = new XMLParser().parseFromString(res);
//       var autheticationResponse = xml.getElementsByTagName("tns:verifyCredentialsResult")[0].value;
//       if(autheticationResponse == "Authentication failed!"){
//         setErrorMessages({ name: "credentials", message: errors.credentials });
//       }
//       });

//     // Find user login info
//     // const userData = database.find((user) => user.username === uname.value);

//     // Compare user info
//     // if (userData) {
//     //   if (userData.password !== pass.value) {
//     //     // Invalid password
//     //     setErrorMessages({ name: "pass", message: errors.pass });
//     //   } else {
//     //     setIsSubmitted(true);
//     //   }
//     // } else {
//     //   // Username not found
//     //   setErrorMessages({ name: "uname", message: errors.uname });
//     // }
//   };

//   function login(username,password){
//     return new Promise( function (resolve,reject){
//     var sr = '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sample="services.dbManager.soap">' +
//                                 '<soap11env:Body>' +
//                                     '<sample:verifyCredentials>'+
//                                         '<sample:username>'+username+'</sample:username>' +
//                                         '<sample:password>'+password+'</sample:password>' +
//                                     '</sample:verifyCredentials>' +
//                                 '</soap11env:Body>' +
//                             '</soap11env:Envelope>';
//                   var xmlhttp = new XMLHttpRequest();
//                   var url = "http://127.0.0.1:8000"
//                   xmlhttp.open('POST',url);
//                   xmlhttp.onload = resolve;
//                   xmlhttp.onerror = reject;
//                   xmlhttp.send(sr);
//                   xmlhttp.onload = () => {
//                           if(xmlhttp.status===200) {
//                             resolve(xmlhttp.response)
//                           }
//                         }
//     });
// }

  // Generate JSX code for error message
  // const renderErrorMessage = (name) =>
  //   name === errorMessages.name && (
  //     <div className="error">{errorMessages.message}</div>
  //   );

  // // JSX code for login form
  // const renderForm = (
  //   <div className="form">
  //     <form onSubmit={handleSubmit}>
  //       <div className="input-container">
  //         <label>Username </label>
  //         <input type="text" name="uname" required />
  //         {renderErrorMessage("uname")}
  //       </div>
  //       <div className="input-container">
  //         <label>Password </label>
  //         <input type="password" name="pass" required />
  //         {renderErrorMessage("pass")}
  //       </div>
  //       <div className="button-container">
  //         <input type="submit" />
  //       </div>
  //     </form>
  //   </div>
  // );

  // return (
  //   <div className="app">
  //     <div className="login-form">
  //       <div className="title">Sign In</div>
  //       {isSubmitted ? <div>User is successfully logged in</div> : renderForm}
  //     </div>
  //   </div>
  // );

export default App;