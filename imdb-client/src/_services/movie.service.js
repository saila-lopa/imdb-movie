import config from 'config';
import { authHeader } from '../_helpers';

export const movieService = {
    getAll,
    getById,
    addMovie,
    getUserMovie
};

function getUserMovie() {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };
    return fetch(`${config.apiUrl}/movie`,
        requestOptions).then(handleResponse);
}

function getAll(searchText) {
    const requestOptions = {
        method: 'GET',
    };
    return fetch('http://www.omdbapi.com/?s=' + searchText + '&apikey=557b7100',
        requestOptions).then(handleExtrnalResponse);
}
function addMovie(movie) {
    let user = JSON.parse(localStorage.getItem('user'));
    let headers = {'Content-Type':'application/json','accessToken': user.accessToken};
    const requestOptions = {
        method: 'POST',
        body: JSON.stringify({
            title: movie.Title,
            year: movie.Year,
            imbdId: movie.imdbID,
            type: movie.Type
        }),
        headers:  headers,
    };
    return fetch(`${config.apiUrl}/movie`,
        requestOptions).then(text => {
        return movie;
    });
}

function getById(id) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${config.apiUrl}/user/${id}`, requestOptions).then(handleResponse);
}

function handleResponse(response) {
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        console.log(data);
        if (!response.ok) {
            if (response.status === 401) {
                // auto logout if 401 response returned from api
                logout();
                location.reload(true);
            }

            const error = (data && data["error"]["message"]) || response.statusText;
            return Promise.reject(error);
        }

        return data["data"];
    });
}

function handleExtrnalResponse(response) {
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        console.log(data);
        if (!response.ok) {
            if (response.status === 401) {
                // auto logout if 401 response returned from api
                logout();
                location.reload(true);
            }

            const error = (data && data["error"]["Search"]) || response.statusText;
            return Promise.reject(error);
        }

        console.log(data);
        return data["Search"];
    });
}