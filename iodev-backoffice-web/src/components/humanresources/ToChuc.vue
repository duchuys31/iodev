<template>
  <div>
    <v-container>
      <h1>Giao diện quản lý tổ chức</h1>
      <v-card>
        <v-data-table
        :headers="headerToChuc"
        :items="toChucs"
        no-data-text="Không có"
        :items-per-page="15">
        <template v-slot:top>
          <v-toolbar flat>
            <v-toolbar-title>IO Tổ Chức</v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical
              ></v-divider>
              <v-spacer></v-spacer>
              <v-file-input
              class="mt-5"
                dense
                prepend-icon="mdi-file-excel"
                label="Import File Excel"
                outlined
                @change="fileImport"
                ref="file"></v-file-input>
              <v-btn color="primary" @click="sendFile" class="ml-1">Import tổ chức</v-btn>
          </v-toolbar>
        </template>
      </v-data-table>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import ToChucService from '../../services/humanresources/tochuc.service'
import ToChuc from '../../models/humanresources/tochuc'

export default {
  data:() => ({
    toChucs: [],
    tochuc: new ToChuc ('',''),
    headerToChuc: [
      { text: 'Tên tổ chức', value: 'tenGoi' },
      { text: 'Tên viết tắt', value: 'tenVietTat' },
      { text: 'Tên tiếng anh', value: 'tenTiengAnh' },
      { text: 'Email', value: 'email' },
      { text: 'Địa chỉ hoạt động', value:'diaChiHoatDong' },
      { text: 'Loại tổ chức', value: 'loaiToChuc' },
    ]
  }),
  methods: {
    fileImport(file) {
      this.File = file
    },
    sendFile: async function() {
      let formData = new FormData();
      formData.append('file', this.File)
      formData.append('fileType', 'xlsx')
      await ToChucService.uploadToChuc(formData)
    },
    readDanhSachToChuc: async function() {
      const data = await ToChucService.getDanhSachToChuc();
      this.toChucs = data
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/dangnhap');
    }     
    this.readDanhSachToChuc();
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
}
</script>

<style>

</style>