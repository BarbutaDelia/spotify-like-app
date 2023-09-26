import { useNavigate } from "react-router-dom";
import React, { useState, useEffect } from 'react';


const Songs = ({ jwt }) => {
   const [songs, setSongs] = useState([]); //stochez datele preluate de la serviciu, pentru a fi consumate mai tarziu
   const navigate = useNavigate();
   useEffect(() => {
      if (!jwt) {
         navigate("/")
      }
      fetch('http://localhost:8080/api/songs')
         .then((response) => response.json())
         .then((data) => {
            // console.log(data[0]);
            setSongs(data[0]);
         })
         .catch((err) => {
            console.log(err.message);
         });
   }, [jwt, navigate]);

   return (
      <div className="posts-container">
         {songs.map((song) => {
            return (
               <div className="post-card" key={song.id}>
                  <h2 className="post-title">{song.name}</h2>
                  <div className="post-body">{song.music_genre}</div>
                  <div className="post-body">{song.year_of_release}</div>
                  <div className="post-body">{song.music_type}</div>
               </div>
            );
         })}
      </div>
   );
}
export default Songs;