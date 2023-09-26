import { useNavigate } from "react-router-dom";
import React, { useState, useEffect } from 'react';
import jwt_decode from 'jwt-decode';
// import jwt_decode from 'jwt-decode';


const ViewPlaylists = ({ jwt }) => {
    const [playlists, setPlaylists] = useState([]);
    const [selectedPlaylistId, setSelectedPlaylistId] = useState(null);
    const [selectedSongs, setSelectedSongs] = useState([]);
    const [selectedSongInfo, setSelectedSongInfo] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {
        if (!jwt) {
            navigate("/")
        }
        fetch('http://localhost:8081/api/profiles')
            .then((response) => response.json())
            .then((data) => {
                // console.log(data[0]);
                const decoded = jwt_decode(jwt);
                for (let elem of data[0]) {
                    if (elem.user_id === parseInt(decoded.sub)) {
                        var profileId = elem.id;
                        // console.log(profileId);
                    }
                }
                fetch('http://localhost:8081/api/profiles/' + profileId)
                    .then((response) => response.json())
                    .then((data) => {
                        // console.log(data);
                        setPlaylists(data.playlists);
                    })
                    .catch((err) => {
                        console.log(err.message);
                    });
            })
            .catch((err) => {
                console.log(err.message);
            });
    }, [jwt, navigate]);

    const handleSubmitViewPlaylists = async (playlistId, event) => {
        event.preventDefault();
        setSelectedPlaylistId(playlistId);
        try {
            const response = await fetch(`http://localhost:8081/api/playlists/` + playlistId);
            const data = await response.json();
            setSelectedSongs(data.songs);
        } catch (err) {
            console.error(err);
        }
    }
    
    const handleShowMoreInfo = async (selfLink) => {
        // console.log(selfLink);
        try {
            const response = await fetch(selfLink);
            const data = await response.json();
            // console.log(data);
            setSelectedSongInfo(data);
        } catch (err) {
            console.error(err);
        }
    }

    return (
        <div className="posts-container">
            {playlists.map((playlist) => {
                return (
                    <div className="post-card" key={playlist.id}>
                        <h2 className="post-title">{playlist.name}</h2>
                        <form onSubmit={(event) => handleSubmitViewPlaylists(playlist.id, event)}>
                            <div className="logout-container">
                                <input type="submit" value="Show more info" />
                            </div>
                        </form>
                        {playlist.id === selectedPlaylistId && (
                            <div>
                                {selectedSongs.map((song) => {
                                    return (
                                        <div key={song.id}>
                                            <div className="song-name">{song.name}</div>
                                            <button className="show-more-info-button" onClick={() => handleShowMoreInfo(song.selfLink)}>Show More Info</button>
                                            {selectedSongInfo && selectedSongInfo.id === song.id && (
                                                <div className="song-info-container">
                                                    <div className="song-info-music-genre">{selectedSongInfo.music_genre}</div>
                                                    <div className="song-info-year-of-release">{selectedSongInfo.year_of_release}</div>
                                                    <div className="song-info-music-type">{selectedSongInfo.music_type}</div>
                                                </div>
                                            )}
                                        </div>
                                    );
                                })}
                            </div>
                        )}
                    </div>
                );
            })}
        </div>
    );
    
}
export default ViewPlaylists;
