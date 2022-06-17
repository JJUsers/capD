package com.example.capd.CoordinatesCalculate;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// 특정 위치 좌표계산 response data DTO

public class Coordinates {
    @SerializedName("documents")
    public List<Data> documentsList;

    @SerializedName("meta")
    public Meta meta;

    public List<Data> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Data> documentsList) {
        this.documentsList = documentsList;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static class Meta{
        @SerializedName("is_end")
        private boolean is_end;
        @SerializedName("pageable_count")
        private int pageable_count;
        @SerializedName("total_count")
        private int total_count;

        public boolean isIs_end() {
            return is_end;
        }

        public void setIs_end(boolean is_end) {
            this.is_end = is_end;
        }

        public int getPageable_count() {
            return pageable_count;
        }

        public void setPageable_count(int pageable_count) {
            this.pageable_count = pageable_count;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }
    }

}
