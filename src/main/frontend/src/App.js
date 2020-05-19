import React, { useState, useEffect, useCallback } from 'react';
// import logo from './logo.svg';
import './App.css';
import axios from "axios";
import { useDropzone } from 'react-dropzone'

const UserProfiles = () => {

  const [userProfiles, setUserProfiles] = useState([]);

  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/userprofiles").then(response => {
      console.log(response);
      setUserProfiles(response.data);
    });
  };

  useEffect(() => {
    fetchUserProfiles();
  }, []);


  return userProfiles.map((userProfile, index) => {
    return (
      <div key={index} id={userProfile.userProfileId}>
        <div id={userProfile.userProfileId + 'img'}>
          {userProfile.userProfileId ? <img src={`http://localhost:8080/userprofiles/${userProfile.userProfileId}/download/image`} /> : null}
        </div>
        <br />
        <h1>{userProfile.username}</h1>
        <p>{userProfile.userProfileId}</p>
        <Dropzone {...userProfile} />

        <br />

      </div>
    )
  });
};

function Dropzone({ userProfileId, username }) {
  const onDrop = useCallback(acceptedFiles => {
    // Do something with the files
    const file = acceptedFiles[0];
    console.log(file);

    const formData = new FormData();
    formData.append('file', file);

    axios.post(`http://localhost:8080/userprofiles/${userProfileId}/upload/image`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      })
      .then(response => {
        console.log("File uploaded successfully");
      })
      .catch(err => {
        console.log(err);
      });

  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop })

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here ...</p> :
          <p>Drag 'n' drop profile image here, or click to select image</p>
      }
    </div>
  )
}

function App() {
  return (
    <div className="App">
      {/* <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header> */}
      <UserProfiles />
    </div>
  );
}

export default App;
