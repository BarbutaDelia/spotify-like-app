import { useNavigate } from "react-router-dom";
var XMLParser = require('react-xml-parser');

function login(username, password) {
  return new Promise(function (resolve, reject) {
    var sr = '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sample="services.dbManager.soap">' +
      '<soap11env:Body>' +
      '<sample:verifyCredentials>' +
      '<sample:username>' + username + '</sample:username>' +
      '<sample:password>' + password + '</sample:password>' +
      '</sample:verifyCredentials>' +
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
const Login = ({ setJwt }) => {
  const navigate = useNavigate();
  const handleSubmit = (event) => {
    //Prevent page reload
    event.preventDefault();

    var { uname, pass } = document.forms[0];

    uname = uname.value;
    pass = pass.value;

    login(uname, pass).then(function (res) {
      var xml = new XMLParser().parseFromString(res);
      var autheticationResponse = xml.getElementsByTagName("tns:verifyCredentialsResult")[0].value;
      if (autheticationResponse === "Authentication failed!") {
        alert("Incorrect username or password!")
      }
      else {
        setJwt(autheticationResponse);
        navigate("/home");
      }
    });
  }
  return (
    <div className="login-form">
      <form onSubmit={handleSubmit}>
        <div className="input-container">
          <label>Username </label>
          <input type="text" name="uname" required />
        </div>
        <div className="input-container">
          <label>Password </label>
          <input type="password" name="pass" required />
        </div>
        <div className="button-container">
          <input type="submit" />
        </div>
      </form>
    </div>
  );
};
export default Login;