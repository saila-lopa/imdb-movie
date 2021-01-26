export function authHeader() {
    // return authorization header with jwt token
    let user = JSON.parse(localStorage.getItem('user'));

    if (user !=null && user.accessToken != null) {
        return { 'accessToken':  user.accessToken };
    } else {
        return {};
    }
}