import React from 'react';
import {movieActions} from "../_actions/movies.actions";
import {connect} from "react-redux";

class Profile extends React.Component {
    constructor(props) {
        super(props);
    }
    componentDidMount() {
        this.props.getUserMovie();
    }
    // getUserMovie(){
    //     return (e) => this.props.getUserMovie();
    // }
    render() {
        const { userMovies } = this.props;
        return (
            <div className="col-md-8 col-md-offset-2">
                <h3>Your Movies:</h3>
                <div className="col-md-8 col-md-offset-2">
                    {userMovies.loading && <em>Loading List...</em>}
                    {userMovies.error && <span className="text-danger">ERROR: {userMovies.error}</span>}
                    {userMovies.items &&
                    <div>
                        {userMovies.items.map((movie, index) =>
                            <div className="movie" key={index} >
                                <h3>{movie.title} ({movie.year})</h3>
                            </div>
                        )}
                    </div>
                    }
                </div>
            </div>
        );
    }
}

function mapState(state) {
    const { userMovies, authentication } = state;
    const { user } = authentication;
    return { user, userMovies };
}

const actionCreators = {
    getUserMovie: movieActions.getUserMovies,
}

const connectedProfile = connect(mapState, actionCreators)(Profile);
export { connectedProfile as Profile };