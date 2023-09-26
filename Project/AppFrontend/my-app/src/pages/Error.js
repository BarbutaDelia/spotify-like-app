import React, { useEffect } from 'react';
import { useNavigate } from "react-router-dom";
var XMLParser = require('react-xml-parser');
const AddSong = ({ jwt }) => {
    const navigate = useNavigate();
    useEffect(() => {
        localStorage.removeItem("jwt");
    }, [])
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
                navigate("/");
            }
        });
    }
    return (
        <div className="login-form">
            <h2>FORBIDDEN!</h2>
            <form className="logout-container" onSubmit={handleSubmit}>
                <div>
                    <input type="submit" value="Logout" />
                </div>
            </form>
        </div>
    );
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
}
export default AddSong;