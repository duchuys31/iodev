<template>
    <v-container class="pa-6">
      <h1>Giao diện quản lý các khối thi</h1>
      <p>Giao diện này được phát triển để quản lý danh sách các khối thi</p>
      <v-row>
        <v-col
            ols="12"
            sm="12"
            md="12"
            >
            <v-select
                :items="cuocThis"
                item-text="tenGoi"
                item-value="id"
                label="Lọc theo cuộc thi"
                v-model="filterBy.cuocThiId"
                @change="(selection) => filterKhoiThiByCuocThi(selection)"
                ></v-select>
        </v-col>
      </v-row>
      <v-data-table
        :headers="headers"
        :items="khoiThis"
        sort-by="tenGoi"
        class="elevation-1"
        >
        <template v-slot:top>
        <v-toolbar
            flat
          >
            <v-toolbar-title>IO khối thi</v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical
              ></v-divider>
              <v-spacer></v-spacer>
              <v-dialog
              v-model="dialog"
              max-width="680px"
              >
              <template v-slot:activator="{ on, attrs }">
                  <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  v-bind="attrs"
                  v-on="on"
                  >
                  Mở khối thi mới
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
                          v-model="editedItem.tenGoi"
                          label="Tên gọi"
                          ></v-text-field>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                      <v-select
                          :items="cuocThis"
                          item-text="tenGoi"
                          item-value="id"
                          label="Chọn cuộc thi"
                          v-model="editedItem.cuocThiId"
                        ></v-select>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                      <v-checkbox
                        v-model="editedItem.thiTapThe"
                        label="Thi tập thể"
                        ></v-checkbox>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                      <v-checkbox
                        v-model="editedItem.thiSangTao"
                        label="Thi sáng tạo"
                        ></v-checkbox>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                      <v-text-field
                          v-model="editedItem.maxDangKi"
                          label="Số đăng ký tối đa"
                          type="number"
                          ></v-text-field>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                      <v-text-field
                          v-model="editedItem.maxThiSinh"
                          label="Số thí sinh tối đa"
                          type="number"
                          ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="12"
                          md="12"
                      >
                      <v-select
                          :items="tinhTrangs"
                          item-text="text"
                          item-value="value"
                          label="Tình trạng"
                          v-model="editedItem.tinhTrang"
                        ></v-select>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="12"
                          md="12"
                      >
                      <v-textarea
                        outlined
                        name="input-7-4"
                        label="Nội dung thi"
                        v-model="editedItem.noiDungThi"
                        ></v-textarea>
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
                <v-card-title class="text-h5">Bạn có chắc chắn muốn xoá thông tin khối thi?</v-card-title>
                <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete">Huỷ bỏ</v-btn>
                <v-btn color="blue darken-1" text @click="deleteItemConfirm">Đồng ý</v-btn>
                <v-spacer></v-spacer>
                </v-card-actions>
            </v-card>
            </v-dialog>
        </v-toolbar>
        </template>
        <template v-slot:[`item.actions`]="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="editItem(item)"
        >
            mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="deleteItem(item)"
        >
            mdi-delete
        </v-icon>
        </template>
        <template v-slot:no-data>
        <v-btn
            color="primary"
            @click="readKhoiThis"
        >
            Làm mới
        </v-btn>
        </template>
    </v-data-table>
    </v-container>
</template>
<script>
import KhoiThiService from '../../services/cms/khoithi.service';
import KhoiThi from '../../models/cms/khoithi';
import CuocThiService from '../../services/cms/cuocthi.service';

    export default {
        name: "KhoiThi",
        data() {
            return {
                khoiThi: new KhoiThi('', '', '', '', '', '', '', '', ''),
                cuocThis: [],
                khoiThis: [],
                filterBy: [],
                responseSuccess: false,
                dialog: false,
                dialogDelete: false,
                headers: [
                  {
                    text: 'Tên khối thi',
                    align: 'start',
                    sortable: false,
                    value: 'tenGoi',
                  },
                  { text: 'Vòng sơ loại', value: 'vongSoLoaiStr' },
                  { text: 'Thi tập thể', value: 'thiTapTheStr' },
                  { text: 'Thi sáng tạo', value: 'thiSangTaoStr' },
                  { text: 'Đăng ký tối đa', value: 'maxDangKi' },
                  { text: 'Tình trạng', value: 'tinhTrangStr' },
                  { text: 'Hành động', value: 'actions', sortable: false },
                ],
                tinhTrangs: [
                    { value: 1, text: "Kết thúc sơ loại" },
                    { value: 2, text: "Kết thúc xếp hạng" }
                ],
                editedIndex: -1,
                editedItem: {
                    tenGoi: '',
                    cuocThiId: '',
                    noiDungThi: '',
                    thiTapThe: null,
                    thiSangTao: null,
                    maxDangKi: null,
                    maxThiSinh: null,
                    tinhTrang: ''
                },
                defaultItem: {
                    tenGoi: '',
                    cuocThiId: '',
                    noiDungThi: '',
                    thiTapThe: null,
                    thiSangTao: null,
                    maxDangKi: null,
                    maxThiSinh: null,
                    tinhTrang: ''
                },
            };
        },
        methods: {
            deleteItem (item) {
              this.editedIndex = this.cuocThis.indexOf(item)
              this.editedItem = Object.assign({}, item)
              this.dialogDelete = true
            },

            deleteItemConfirm () {
              KhoiThiService.xoaKhoiThi(this.editedItem.id);
              this.khoiThis.splice(this.editedIndex, 1)
              this.closeDelete()
            },

            editItem (item) {
              this.editedIndex = this.cuocThis.indexOf(item)
              this.editedItem = Object.assign({}, item)
              this.dialog = true
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

            save: async function () {
                await KhoiThiService.themKhoiThi(this.editedItem.cuocThiId, this.editedItem);
                await this.readKhoiThis();
                this.close()
            },
            readCuocThis: async function() {
                const data = await CuocThiService.getDanhSachCuocThi();
                this.cuocThis = data;
            },
            readKhoiThis: async function() {
                const data = await KhoiThiService.getDanhSachKhoiThi();
                for (var i = 0; i < data.length; i++) {
                    data[i].tinhTrangStr = data[i].tinhTrang == 1 ? "Kết thúc sơ loại" : "Kết thúc xếp hạng";
                    data[i].vongSoLoaiStr = data[i].vongSoLoai ? "Có" : "Không";
                    data[i].thiTapTheStr = data[i].thiTapThe ? "Có" : "Không";
                    data[i].thiSangTaoStr = data[i].thiSangTao ? "Có" : "Không";
                }
                this.khoiThis = data;
            },
            readKhoiThisCuaCuocThi: async function(cuocThiId) {
                const data = await KhoiThiService.getDanhSachKhoiThiCuaCuocThi(cuocThiId);
                for (var i = 0; i < data.length; i++) {
                    data[i].tinhTrangStr = data[i].tinhTrang == 1 ? "Kết thúc sơ loại" : "Kết thúc xếp hạng";
                    data[i].vongSoLoaiStr = data[i].vongSoLoai ? "Có" : "Không";
                    data[i].thiTapTheStr = data[i].thiTapThe ? "Có" : "Không";
                    data[i].thiSangTaoStr = data[i].thiSangTao ? "Có" : "Không";
                    data[i].cuocThiId = cuocThiId;
                }
                this.khoiThis = data;
            },
            filterKhoiThiByCuocThi: async function(cuocThiId) {
                await this.readKhoiThisCuaCuocThi(cuocThiId);
            }
        },
        mounted() {
            if (!this.currentUser) {
                this.$router.push('/dangnhap');
            }     
            this.readCuocThis();
            this.readKhoiThis();   
        },
        computed: {
            formTitle () {
              return this.editedIndex === -1 ? 'Tạo thông tin khối thi' : 'Sửa thông tin khối thi'
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
            }
        },
   };
</script>