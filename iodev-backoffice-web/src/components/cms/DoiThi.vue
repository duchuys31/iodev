<template>
  <div>
    <v-container>
      <v-card>
        <v-card>
          <v-card-title><h1>Giao diện quản lý đội thi</h1></v-card-title>
          <v-card-text>
            <v-data-table
            :headers="headers"
            :items="doiThis"
            no-data-context="Không có"></v-data-table>
          </v-card-text>
        </v-card>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import DoiThiService from "../../services/cms/doithi.service";

export default {
  data:() => ({
    doiThis: [],
  }),
  methods: {
    readDoiThis: async function (cuocThiId) {
      const data = await DoiThiService.getDanhSachDoiThi(cuocThiId);
      this.doiThis = data
    }
  }
}
</script>

<style>

</style>