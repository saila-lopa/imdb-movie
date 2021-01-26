import { movieConstants } from '../_constants/movie.constant';

export function userMovies(state = {}, action) {
    switch (action.type) {
        case movieConstants.GET_USER_MOVIE_REQUEST:
            return {
                loading: true
            };
        case movieConstants.GET_USER_MOVIE_SUCCESS:
            return {
                items: action.userMovies
            };
        case movieConstants.GET_USER_MOVIE_FAILURE:
            return {
                error: action.error
            };
        default:
            return state
    }
}