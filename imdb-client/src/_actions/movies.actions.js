import { userConstants } from '../_constants';
import { history } from '../_helpers';
import {movieService} from "../_services/movie.service";
import {movieConstants} from "../_constants/movie.constant";

export const movieActions = {
    getAll,
    getById,
    addMovie,
    getUserMovies
};

function getUserMovies() {
    return dispatch => {
        dispatch(request());

        movieService.getUserMovie()
            .then(
                userMovies => dispatch(success(userMovies)),
                error => dispatch(failure(error.toString()))
            );
    };

    function request() { return { type: movieConstants.GET_USER_MOVIE_REQUEST } }
    function success(userMovies) { return { type: movieConstants.GET_USER_MOVIE_SUCCESS, userMovies } }
    function failure(error) { return { type: movieConstants.GET_USER_MOVIE_FAILURE, error } }
}
function getAll(searchText) {
    return dispatch => {
        dispatch(request());

        movieService.getAll(searchText)
            .then(
                movies => dispatch(success(movies)),
                error => dispatch(failure(error.toString()))
            );
    };

    function request() { return { type: movieConstants.GETALL_MOVIE_REQUEST } }
    function success(movies) { return { type: movieConstants.GETALL_MOVIE_SUCCESS, movies } }
    function failure(error) { return { type: movieConstants.GETALL_MOVIE_FAILURE, error } }
}

function addMovie(movie) {
    return dispatch => {
        dispatch(request(movie));

        movieService.addMovie(movie)
            .then(
                movie => dispatch(success(movie)),
                error => dispatch(failure(error.toString(), movie))
            );
    };

    function request(movie) { return { type: movieConstants.ADD_MOVIE_REQUEST, movie: movie } }
    function success(movie) { return { type: movieConstants.ADD_MOVIE_SUCCESS, movie: movie } }
    function failure(error, movie) { return { type: movieConstants.ADD_MOVIE_FAILURE, error, movie: movie } }
}


function getById(id) {
    return dispatch => {
        dispatch(request(id));

        movieService.getById(id)
            .then(
                movie => dispatch(success(movie)),
                error => dispatch(failure(id, error.toString()))
            );
    };

    function request(id) { return { type: movieConstants.DELETE_REQUEST, id } }
    function success(movie) { return { type: movieConstants.DELETE_SUCCESS, movie } }
    function failure(id, error) { return { type: movieConstants.DELETE_FAILURE, id, error } }
}