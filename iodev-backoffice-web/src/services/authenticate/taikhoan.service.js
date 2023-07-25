import axios from 'axios';
import authHeader from '../authenticate/auth-header';

class TaiKhoanService
{
  getDanhSachTaiKhoan ( tinhTrang )
  {
    return axios
      .get( 'http://localhost:8081/api/taikhoans', { headers: authHeader() }, {
        tinhTrang: tinhTrang
      } )
      .then( response =>
      {
        return response.data;
      } );
  }

  themTaiKhoan ( taiKhoan )
  {
    return axios
      .post( 'http://localhost:8081/taikhoans', taiKhoan, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }

  kichHoatTaiKhoan ( email )
  {
    return axios
      .put( 'http://localhost:8081/taikhoans/' + email + "/kichhoat", { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }

  xoaTaiKhoan ( email )
  {
    return axios
      .delete( 'http://localhost:8081/taikhoans/' + email, { headers: authHeader() } )
      .then( response =>
      {
        return response.data;
      } );
  }

  suaTaiKhoan ( reset )
  {
    return axios
      .post( 'taikhoans' + reset.id, )
  }
}

export default new TaiKhoanService();