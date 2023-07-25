import axios from "axios";
import authHeader from "../authenticate/auth-header";

class KhoiThiService
{
  getDanhSachKhoiThi ()
  {
    return axios
      .get( "http://localhost:8083/api/khoithis", { params: { size: '10000' } }, { headers: authHeader() } )
      .then( ( response ) =>
      {
        return response.data;
      } );
  }
  getDanhSachKhoiThiCuaCuocThi ( cuocThiId )
  {
    return axios
      .get( "http://localhost:8083/cuocthis/" + cuocThiId + "/khoithis", { params: { size: '10000' } }, { headers: authHeader() } )
      .then( ( response ) =>
      {
        return response.data;
      } );
  }
  themKhoiThi ( cuocThiId, khoiThi )
  {
    return axios
      .post( "http://localhost:8083/cuocthis/" + cuocThiId + "/khoithis", khoiThi, {
        headers: authHeader(),
      } )
      .then( ( response ) =>
      {
        return response.data;
      } );
  }
  suaKhoiThi ( khoiThi )
  {
    return axios
      .put( "http://localhost:8083/khoithis/" + khoiThi.id, khoiThi, { headers: authHeader() } )
      .then( ( response ) =>
      {
        return response.data;
      } );
  }
  xoaKhoiThi ( id )
  {
    return axios
      .delete( "http://localhost:8083/khoithis/" + id, { headers: authHeader() } )
      .then( ( response ) =>
      {
        return response.data;
      } );
  }
}

export default new KhoiThiService();
