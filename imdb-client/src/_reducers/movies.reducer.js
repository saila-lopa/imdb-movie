import { movieConstants } from '../_constants/movie.constant';

export function movies(state = {}, action) {
    switch (action.type) {
        case movieConstants.GETALL_MOVIE_REQUEST:
            return {
                loading: true
            };
        case movieConstants.GETALL_MOVIE_SUCCESS:
            return {
                items: action.movies
            };
        case movieConstants.GETALL_MOVIE_FAILURE:
            return {
                error: action.error
            };
        case movieConstants.ADD_MOVIE_REQUEST:
            return {
                items: state.items.map(movie => {
                    if(movie.imdbID === action.movie.imdbID) {
                        return { ...movie, adding: true };
                    }
                    return movie;
                })
            };
        case movieConstants.ADD_MOVIE_SUCCESS:
            return {
                items: state.items.map(movie => {
                    if(movie.imdbID === action.movie.imdbID) {
                        const { adding, ...movieCopy } = movie;
                        return { ...movieCopy, added: true };
                    }
                    return movie;
                })
            };

        case movieConstants.ADD_MOVIE_FAILURE:
            return {
                ...state,
                items: state.items.map(movie => {
                    if (movie.imdbID === action.movie.imdbID) {
                        // make copy of user without 'deleting:true' property
                        const { adding, ...movieCopy } = movie;
                        // return copy of user with 'deleteError:[error]' property
                        return { ...movieCopy, addError: action.error };
                    }

                    return movie;
                })
            };
        default:
            return state
    }
}