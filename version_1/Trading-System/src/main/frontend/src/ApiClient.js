import axios from 'axios';
import URLS from '../src/Configuration';

// var ApiClient = {
//     getTest: (path) => String;
// }

export const createApiClient = () => {
    //API CLIENT OBJECT
    return {
        getTest: () => {   
            return axios.get(`http://localhost:8080/api/test`).then((res) => {
                console.log(res);
                return res.data;
            });
        },
    }
};

export default createApiClient;