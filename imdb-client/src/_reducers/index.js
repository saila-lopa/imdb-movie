import { combineReducers } from 'redux';

import { authentication } from './authentication.reducer';
import { registration } from './registration.reducer';
import { users } from './users.reducer';
import { alert } from './alert.reducer';
import { movies } from './movies.reducer';
import {userMovies} from "./usermovie.reducer";

const rootReducer = combineReducers({
  authentication,
  registration,
  users,
  movies,
  userMovies,
  alert
});

export default rootReducer;