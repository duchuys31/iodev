<template>
  <div>
    <v-container class="pa-6">
      <v-card>
        <v-card-title><h1>Giao diện quản lý thí sinh</h1></v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="4">
              <v-card-subtitle>Import danh sách thí sinh</v-card-subtitle>
              <v-file-input
                dense
                prepend-icon="mdi-file-excel"
                label="Import File Excel"
                outlined
                @change="fileUpload"
                ref="file"></v-file-input>
              <v-btn color="primary" @click="sendFile" class="ml-1">Import</v-btn>
              <v-divider class="mt-4 ml-1"></v-divider>
              <v-select
                :items="cuocThis"
                item-text="tenGoi"
                item-value="id"
                label="Lọc danh sách thi theo cuộc thi"
                v-model="filterBy.cuocThiId"
                @change="(selection) => filterDanhSachThiSinhTheoCuocThi(selection)"></v-select>
            </v-col>
            <v-divider vertical></v-divider>
            <v-col cols="8">
              <v-card-subtitle>Danh sách thí sinh</v-card-subtitle>
              <v-data-table
              :headers="headers"
              :items="thiSinhs"
              no-data-text="Không có"></v-data-table>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import ThiSinhService from "../../services/cms/thisinh.service";
import CuocThiService from "../../services/cms/cuocthi.service";
import ThiSinh from "../../models/cms/thisinh.js"

export default {
  data: () => ({
    cuocThis: [],
    thiSinhs: [],
    filterBy: [],
    thisinh: new ThiSinh('','','','','','',''),
    headers: [
      { text: 'Họ tên', value: 'hoTen' },
      { text: 'Ngày sinh', value: 'ngaySinh' },
      { text: 'Email', value: 'email' },
      { text: 'Số điện thoại', value: 'soDienThoai' }
    ],
  }),
  methods: {
    fileUpload(file) {
      this.File = file
    },
    sendFile: async function() {
      let formData = new FormData();
      formData.append('file', this.File)
      formData.append('fileType', 'xlsx')
      await ThiSinhService.uploadThiSinh(formData)
    },
    readCuocThis: async function() {
      const data = await CuocThiService.getDanhSachCuocThi();
      this.cuocThis = data;
    },
    readDanhSachThiCuaCuocThi: async function(cuocThiId) {
      const data = await ThiSinhService.getDanhSachThiSinhCuaCuocThi(cuocThiId);
      this.thiSinhs = data;
    },
    filterDanhSachThiSinhTheoCuocThi: async function(cuocThiId) {
      await this.readDanhSachThiCuaCuocThi(cuocThiId);
    },
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/dangnhap');
    }     
    this.readCuocThis();
    this.readDanhSachThiCuaCuocThi();   
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
};
</script>

<style></style>
