import React from 'react';
import { HomePage } from '../HomePage/HomePage';
import {movieActions} from "../_actions/movies.actions";
import {connect} from "react-redux";
import {Profile} from "../Profile/Profile";

class CustomNavBar extends React.Component {
    constructor(props) {
        super(props);
        this.text = '';
        this.state = {currentPage: 'home'};
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);

    }

    handleChange(event) {

        this.text = event.target.value;
        // alert(this.text);
    }

    handleSubmit(event) {
        // alert('A name was submitted: ' + this.state.value);
        this.props.getMovies(this.text);
        event.preventDefault();
    }

    setPage(page, event) {
        this.setState({currentPage: page});
    }

    render() {
        const { movies } = this.props;
        const searchedFor = this.text;
        let page;
        // alert(this.state.currentPage);
        if (this.state.currentPage == 'home') {
            page = <HomePage movies={movies} searchVal={searchedFor}></HomePage>;
        } else {
            page = <Profile></Profile>
        }
        return (
            <div>
                <div className="topnav">
                    <a className="active" onClick={this.setPage.bind(this, 'home')}>Home</a>
                    <a onClick={this.setPage.bind(this, 'profile')}>Profile</a>
                    <a href="/login">Logout</a>
                    <div className="search-container">
                        <form onSubmit={this.handleSubmit}>
                            <input type="text" placeholder="Search.." onChange={this.handleChange} value={this.props.text} />
                            <button type="submit">Search</button>
                        </form>
                    </div>
                </div>
                {page}
            </div>
        );
    }
}

function mapState(state) {
    const { movies } = state;
    return { movies };
}

const actionCreators = {
    getMovies: movieActions.getAll
}

const connectedNavBar = connect(mapState, actionCreators)(CustomNavBar);
export { connectedNavBar as CustomNavBar };