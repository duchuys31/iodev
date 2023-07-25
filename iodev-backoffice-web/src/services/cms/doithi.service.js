import axios from 'axios';
import authHeader from '../authenticate/auth-header'

class DoiThiService
{
  getDanhSachDoiThi ()
  {
    return axios
      .get( 'http://localhost:8083/api/doithis', { params: { size: '10000' } }, { headers: authHeader() } )
      .then( ( response ) =>
      {
        return response.data;
      } );
  }
}

export default new DoiThiService()