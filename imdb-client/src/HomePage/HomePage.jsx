import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import { userActions } from '../_actions';
import {movieActions} from "../_actions/movies.actions";
import {Modal, Button} from 'react-bootstrap';

class HomePage extends React.Component {
    constructor(props) {
        super(props);
        // this.state = { searchText: 'war' };
        this.state = { searchText: props.searchValue, selectedMovie: {} };
    }

    showModal(movie, event) {
        this.setState({selectedMovie: { title: movie.Title, year: movie.Year, poster: movie.Poster }});
        $("#myModal").modal("show");
    }

    componentDidMount() {
        // this.props.getMovies("love");
        // this.props.getMovies(this.state.searchText);
    }
    addMovie(movie){
        return (e) => this.props.addMovie(movie);
    }
    viewDetails(movie){
        // return (e) => this.props.viewMovieDetails(movie);
    }

    render() {
        // if (this.props.searchValue)
            // this.props.getMovies(this.state.searchText);
        // this.setState({searchText: props.searchValue});
        const { user, movies } = this.props;
        // alert(movies.length);
        return (
            <div>
                <div className="modal" id="myModal">
                    <div className="modal-dialog modal-sm">
                        <div className="modal-content">

                        <div className="modal-header">
                            <h4 className="modal-title">{this.state.selectedMovie.title} - {this.state.selectedMovie.year}</h4>
                            <button type="button" className="close" data-dismiss="modal">&times;</button>
                        </div>

                        <div className="modal-body" style={{textAlign: 'center'}}>
                            <img height="200px" src={this.state.selectedMovie.poster}></img>
                        </div>

                        <div className="modal-footer">
                            <button type="button" className="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>

                        </div>
                    </div>
                </div>
                <div className="col-md-8 col-md-offset-2">
                    {this.props.searchVal ? <h3>Search Results for '{this.props.searchVal}' </h3> : ''}
                    {movies.loading && <em>Loading List...</em>}
                    {movies.error && <span className="text-danger">ERROR: {movies.error}</span>}
                    {movies.items &&
                        <div>
                            {movies.items.map((movie, index) =>
                                <div className="movie" key={index} >
                                    <h3 onClick={this.showModal.bind(this, movie)}>{movie.Title} ({movie.Year})</h3>
                                    {
                                        movie.adding ? <em> - Adding...</em>
                                            : movie.added ? <em> - added</em>
                                            : movie.deleteError ? <span className="text-danger"> - ERROR: {movie.addError}</span>
                                            : <button onClick={this.addMovie(movie)}>Add</button>
                                    }


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
    const { authentication } = state;
    const { user } = authentication;
    return { user };
}

const actionCreators = {
    addMovie: movieActions.addMovie,
    // viewMovieDetails : movieActions.
}

const connectedHomePage = connect(mapState, actionCreators)(HomePage);
export { connectedHomePage as HomePage };