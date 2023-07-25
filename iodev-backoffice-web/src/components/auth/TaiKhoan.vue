<template>
    <v-container class="pa-6">
      <h1>Giao diện quản lý tài khoản</h1>
      <p>Giao diện này được phát triển để quản lý danh sách tài khoản hệ thống</p>

      <v-data-table
        :headers="headers"
        :items="taiKhoans"
        sort-by="email"
        class="elevation-1"
        :search="search"
        >
        <template v-slot:top>
        <v-toolbar
            flat
          >
            <v-toolbar-title>IO tài khoản</v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical
              ></v-divider>
              <v-text-field dense label="Search" v-model="search"></v-text-field>
              <v-spacer></v-spacer>
              <v-dialog
              v-model="dialog"
              max-width="500px"
              >
              <template v-slot:activator="{ on, attrs }">
                  <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  v-bind="attrs"
                  v-on="on"
                  >
                  Cấp tài khoản
                  </v-btn>
              </template>
            <v-card>
                <v-card-title>
                <span class="text-h5">{{ formTitle }}</span>
                </v-card-title>

                <v-card-text>
                <v-container>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                          <v-text-field
                          v-model="editedItem.email"
                          label="Email"
                          ></v-text-field>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                          <v-text-field
                          v-model="editedItem.matKhau"
                          label="Mật khẩu"
                          type="password"
                          ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                        >
                        <v-select
                          :items="loaiTaiKhoans"
                          item-text="text"
                          item-value="value"
                          label="Loại tài khoản"
                          v-model="editedItem.loaiTaiKhoan"
                          @change="(selection) => cboLoaiTaiKhoanChanged(selection)"
                        ></v-select>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                        >
                        <v-select
                          :items="caNhans"
                          item-text="hoTen"
                          item-value="id"
                          label="Chọn cá nhân"
                          v-model="editedItem.id"
                          v-if="editedItem.loaiTaiKhoan == 1"
                        ></v-select>
                        <v-select
                          :items="toChucs"
                          item-text="tenGoi"
                          item-value="id"
                          label="Chọn tổ chức"
                          v-model="editedItem.id"
                          v-if="editedItem.loaiTaiKhoan == 0"
                        ></v-select>                       
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col
                            cols="12"
                            sm="12"
                            md="12"
                          >                       
                          <v-combobox
                            v-model="editedItem.vaiTros"
                            item-text="tenVaiTro"
                            item-value="maVaiTro"
                            :items="vaiTros"
                            label="Vai trò"
                            multiple
                            outlined
                            dense
                          ></v-combobox>
                        </v-col>
                    </v-row>
                </v-container>
                </v-card-text>

                <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                    color="blue darken-1"
                    text
                    @click="close"
                >
                    Huỷ bỏ
                </v-btn>
                <v-btn
                    color="blue darken-1"
                    text
                    @click="save"
                >
                    Lưu lại
                </v-btn>
                </v-card-actions>
            </v-card>
            </v-dialog>
            <v-dialog v-model="dialogDelete" max-width="640px">
            <v-card>
                <v-card-title class="text-h5">Bạn có chắc chắn muốn xoá tài khoản?</v-card-title>
                <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete">Huỷ bỏ</v-btn>
                <v-btn color="blue darken-1" text @click="deleteItemConfirm">Đồng ý</v-btn>
                <v-spacer></v-spacer>
                </v-card-actions>
            </v-card>
            </v-dialog>
            <v-dialog v-model="dialogActive" max-width="640px">
            <v-card>
                <v-card-title class="text-h5">Bạn có chắc chắn muốn kích hoạt tài khoản?</v-card-title>
                <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeActive">Huỷ bỏ</v-btn>
                <v-btn color="blue darken-1" text @click="activeUserConfirm">Đồng ý</v-btn>
                <v-spacer></v-spacer>
                </v-card-actions>
            </v-card>
            </v-dialog>
            <v-dialog v-model="dialogDeActive" max-width="640px">
            <v-card>
                <v-card-title class="text-h5">Bạn có chắc chắn muốn đóng tài khoản?</v-card-title>
                <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDeActive">Huỷ bỏ</v-btn>
                <v-btn color="blue darken-1" text @click="deActiveUserConfirm">Đồng ý</v-btn>
                <v-spacer></v-spacer>
                </v-card-actions>
            </v-card>
            </v-dialog>
            <v-dialog v-model="dialogEdit" max-width="640px">
              <v-card>
                <v-card-title class="text-h5">Thay đổi mật khẩu của tài khoản</v-card-title>
                <v-card-text>
                  <v-row>
                    <v-col>
                      <label>Email:</label>
                      <v-text-field dense outlined v-model="editedItem.email"></v-text-field>
                    </v-col>
                    <v-col>
                      <label>Mật Khẩu:</label>
                      <v-text-field dense outlined v-model="editedItem.matKhau"></v-text-field>
                    </v-col>
                  </v-row>
                </v-card-text>
                <v-card-actions>
                  <v-btn color="error" @click="closeEdit">Hủy</v-btn>
                  <v-btn color="success" @click="editConfirm">Lưu lại</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
        </v-toolbar>
        </template>
        <template v-slot:[`item.actions`]="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="deActiveItem(item)"
        >
            mdi-account-lock
        </v-icon>
        <v-icon
            small
            class="mr-2"
            @click="activeItem(item)"
        >
            mdi-account-lock-open
        </v-icon>
        <v-icon
            small
            @click="deleteItem(item)"
        >
            mdi-delete
        </v-icon>
        <v-icon
          small
          class="mr-2"
          @click="editTaiKhoan(item)"
        >
          mdi-pencil
        </v-icon>
        </template>
        <template v-slot:no-data>
        <v-btn
            color="primary"
            @click="readTaiKhoans"
        >
            Làm mới
        </v-btn>
        </template>
    </v-data-table>
    </v-container>
</template>
<script>
import TaiKhoanService from '../../services/authenticate/taikhoan.service';
import CaNhanService from '../../services/humanresources/canhan.service';
import ToChucService from '../../services/humanresources/tochuc.service';
import moment from 'moment';

    export default {
        name: "TaiKhoan",
        data() {
            return {
              search: '',
                dialog: false,
                dialogDelete: false,
                dialogDeActive: false,
                dialogActive: false,
                dialogEdit: false,
                headers: [
                  {
                    text: 'Email',
                    align: 'start',
                    sortable: false,
                    value: 'email',
                  },
                  { text: 'ID', value: 'id' },
                  { text: 'Loại tài khoản', value: 'loaiTaiKhoanStr' },
                  { text: 'Mã kích hoạt', value: 'maKichHoat' },
                  { text: 'Thời hạn kích hoạt', value: 'thoiHanKichHoatStr' },
                  { text: 'Thời gian tạo', value: 'thoiGianTaoStr' },
                  { text: 'Tình trạng', value: 'tinhTrangStr' },
                  { text: 'Hành động', value: 'actions', sortable: false },
                ],
                loaiTaiKhoans: [
                  { value: 0, text: 'TK tổ chức' },
                  { value: 1, text: 'TK cá nhân' }
                ],
                taiKhoans: [],
                caNhans: [],
                toChucs: [],
                vaiTros: [
                  { tenVaiTro: "Quản trị hệ thống", maVaiTro: "VAITRO_QUANTRIHETHONG" },
                  { tenVaiTro: "Quản trị tổ chức", maVaiTro: "VAITRO_QUANTRITOCHUC" },
                  { tenVaiTro: "Người dùng", maVaiTro: "VAITRO_NGUOIDUNG" },
                ],
                editedIndex: -1,
                editedItem: {
                  email: '',
                  id: '',
                  loaiTaiKhoan: '',
                  matKhau: ''
                },
                defaultItem: {
                  email: '',
                  id: '',
                  loaiTaiKhoan: '',
                  matKhau: ''
                },
            };
        },
        methods: {
            deleteItem (item) {
              this.editedIndex = this.taiKhoans.indexOf(item)
              this.editedItem = Object.assign({}, item)
              this.dialogDelete = true
            },

            deleteItemConfirm () {
              this.taiKhoans.splice(this.editedIndex, 1)
              this.xoaTaiKhoan(this.editedItem);
              this.closeDelete()
            },

            deActiveItem (item) {
              this.editedIndex = this.taiKhoans.indexOf(item)
              this.editedItem = Object.assign({}, item)
              this.dialogDeActive = true
            },

            deActiveUserConfirm () {
              this.taiKhoans.splice(this.editedIndex, 1)
              this.closeDeActive()
            },

            activeItem (item) {
              this.editedIndex = this.taiKhoans.indexOf(item)
              this.editedItem = Object.assign({}, item)
              this.dialogActive = true
            },

            activeUserConfirm () {
              this.kichHoatTaiKhoan(this.editedItem);
              this.readTaiKhoans();
              this.closeActive()
            },

            editTaiKhoan(item) {
              this.editedIndex = this.taiKhoans.indexOf(item)
              this.editedItem = Object.assign({}, item)
              this.dialogEdit = true
            },

            closeEdit() {
              this.dialogEdit = false
              this.$nextTick(() => {
                this.editedItem = Object.asign({}, this.defaultItem)
                this.editedIndex = -1
              })
            },

            close () {
              this.dialog = false
              this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
              })
            },

            closeDelete () {
              this.dialogDelete = false
              this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
              })
            },

            closeDeActive () {
              this.dialogDeActive = false
              this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
              })
            },

            closeActive () {
              this.dialogActive = false
              this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
              })
            },

            editConfirm: async function () {
              this.taikhoans.splice(this.editedIndex, 1)
              let reset = {
                matKhau : this.editedItem.matKhau,
                id : this.editedItem.id
              }
              await TaiKhoanService.suaTaiKhoan(reset)
              this.closeEdit = true
            }, 

            save: async function () {
              if (this.editedIndex > -1) {
                Object.assign(this.taiKhoans[this.editedIndex], this.editedItem)
              } else {
                this.taiKhoans.push(this.editedItem)
              }
              var savedVaiTros = [];
              if (this.editedItem.vaiTros) {
                for (var vt of this.editedItem.vaiTros) {
                  savedVaiTros.push({ ten: vt.maVaiTro });
                }
              }
              var taiKhoan = {
                email: this.editedItem.email,
                matKhau: this.editedItem.matKhau,
                loaiTaiKhoan: this.editedItem.loaiTaiKhoan,
                id: this.editedItem.id,
                vaiTros: savedVaiTros
              }
              await TaiKhoanService.themTaiKhoan(taiKhoan);

              this.close()
            },
            readTaiKhoans: async function() {
              const data = await TaiKhoanService.getDanhSachTaiKhoan(0);
              this.taiKhoans = this.convertTaiKhoanTable(data);
            },
            kichHoatTaiKhoan: async function(tk) {
              await TaiKhoanService.kichHoatTaiKhoan(tk.email);
            },
            suaTaiKhoan: async function(item) {
              await TaiKhoanService.suaTaiKhoan(item.email);
            }, 
            xoaTaiKhoan: async function(tk) {
              await TaiKhoanService.xoaTaiKhoan(tk.email);
            },
            readCaNhans: async function() {
              const data = await CaNhanService.getDanhSachCaNhan();
              this.caNhans = data;
            },
            readDanhSachToChuc: async function() {
              const data = await ToChucService.getDanhSachToChuc();
              this.toChucs = data
            },
            convertTaiKhoanTable (lstTaiKhoan) {
              var convertArr = [];
              for (var i = 0; i < lstTaiKhoan.length; i++) {
                var taiKhoan = {
                  email: lstTaiKhoan[i].email,
                  id: lstTaiKhoan[i].id,
                  loaiTaiKhoanStr: lstTaiKhoan[i].loaiTaiKhoan == 0 ? "TK tổ chức" : "TK cá nhân",
                  tinhTrangStr: lstTaiKhoan[i].tinhTrang == 0 ? "Chờ kích hoạt" : (lstTaiKhoan[i].tinhTrang == 1 ? "Đã kích hoạt" : "Đóng tài khoản"),
                  thoiHanKichHoatStr: moment(lstTaiKhoan[i].thoiHanKichHoat).format('DD-MM-yyyy'),
                  thoiHanKichHoat: lstTaiKhoan[i].thoiHanKichHoat,
                  thoiGianTao: lstTaiKhoan[i].thoiGianTao,
                  vaiTrosStr: '',
                  thoiGianTaoStr: moment(lstTaiKhoan[i].thoiGianTao).format('DD-MM-yyyy'),
                  loaiTaiKhoan: lstTaiKhoan[i].loaiTaiKhoan,
                  tinhTrang: lstTaiKhoan[i].tinhTrang,
                  maKichHoat: lstTaiKhoan[i].maKichHoat
                };
                convertArr.push(taiKhoan);
              }
              return convertArr;
            },
            cboLoaiTaiKhoanChanged(selection) {
              if (selection == 0) {
                this.readToChucs();
              }
              else {
                this.readCaNhans();
              }
            }
          },
          mounted() {
            if (!this.currentUser) {
                this.$router.push('/dangnhap');
            }    
            this.readTaiKhoans();
            this.readDanhSachToChuc();
          },
          computed: {
            formTitle () {
              return this.editedIndex === -1 ? 'Cấp mới tài khoản' : 'Sửa tài khoản'
            },
            currentUser() {
              return this.$store.state.auth.user;
            }
          },
          watch: {
            dialog (val) {
              val || this.close()
            },
            dialogDelete (val) {
              val || this.closeDelete()
            },
            dialogEdit (val) {
              val || this.closeEdit()
            }
          },
      created () {
      
      },
   };
</script>