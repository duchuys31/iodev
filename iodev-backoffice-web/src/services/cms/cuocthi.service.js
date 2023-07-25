import axios from 'axios';
import authHeader from '../authenticate/auth-header';

class CuocThiService
{
  getDanhSachCuocThi ()
  {
    return axios.get( "http://localhost:8083/api/cuocthis", { params: { size: '10000' } }, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }
  themCuocThi ( cuocThi )
  {
    return axios
      .post( 'http://localhost:8083/cuocthis', cuocThi, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }
  xoaCuocThi ( id )
  {
    return axios
      .delete( 'http://localhost:8083/cuocthis/' + id, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }
  getDanhSachHinhAnhs ( id )
  {
    return axios
      .get( 'http://localhost:8083/cuocthis/' + id + "/hinhanhs", { params: { size: '10000' } }, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }
  uploadHinhAnhs ( id, formData )
  {
    return axios
      .put( 'http://localhost:8083/cuocthis/' + id + "/hinhanhs", formData, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }
  xoaHinhAnhCuocThi ( cuocThiId, hinhAnhId )
  {
    return axios
      .delete( 'http://localhost:8083/cuocthis/' + cuocThiId + "/hinhanhs/" + hinhAnhId, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }
}

export default new CuocThiService();