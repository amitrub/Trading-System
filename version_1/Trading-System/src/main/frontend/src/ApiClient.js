import axios from 'axios';

export const createApiClient = () => {
    //API CLIENT OBJECT
    return {
        getTest: () => {   
            return axios.get(`http://localhost:8080/api/test`).then((res) => {
                console.log(res);
                return res.data;
            });
        },

        getConnectSystem: () => {   
            return axios.get(`http://localhost:8080/api/home`).then((res) => {
                console.log(res);
                return res.data;
            });
        },

        postRegister: (registerData, connID) => {
            const headers = {
                'Content-Type': 'application/json; utf-8',
                'Accept': 'application/json',
                'connID': connID
              }

            return axios.post(`http://localhost:8080/api/register`, 
                            registerData, {headers: headers}).then((res) => {
                console.log(res);
                return res.data;
            })
        }
    }
};

export default createApiClient;