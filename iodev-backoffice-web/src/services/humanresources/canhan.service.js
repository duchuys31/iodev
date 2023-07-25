import axios from 'axios';
import authHeader from '../authenticate/auth-header';

class CaNhanService
{
  getDanhSachCaNhan ()
  {
    return axios.get( "http://localhost:8082/api/canhans", { headers: authHeader() } ).then( response =>
    {
      for ( var i = 0; i < response.data.length; i++ )
      {
        response.data[ i ].fakeId = i;
      }
      return response.data;
    } );
  }
}

export default new CaNhanService();