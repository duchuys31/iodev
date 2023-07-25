import axios from 'axios';

class AuthService
{
  login ( user )
  {
    // console.log( "http://localhost:8081/api/passssssssss" )
    return axios
      .post( 'http://localhost:8081/authenticate', {
        username: user.username,
        password: user.password
      } )
      .then( response =>
      {
        if ( response.data.accessToken )
        {
          localStorage.setItem( 'user', JSON.stringify( response.data ) );
        }
        console.log( response )

        return response.data;
      } );
  }

  logout ()
  {
    localStorage.removeItem( 'user' );
  }

  register ( user )
  {
    return axios.post( 'http://localhost:8081/signup', {
      username: user.username,
      email: user.email,
      password: user.password
    } );
  }
}

export default new AuthService();