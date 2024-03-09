package com.example.userapp;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class TestAdapter2 extends BaseQuickAdapter<recordDrug, BaseViewHolder> {
    public TestAdapter2(@Nullable List<recordDrug> data) {
        super(R.layout.item_record_thing,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, recordDrug item) {
        helper.setText(R.id.item_drugName,"药品名称："+AllData.drugData.get(item.getDgCode()).getDgName());
        helper.setText(R.id.item_drugType,"药品类型："+AllData.drugData.get(item.getDgCode()).getDgType());
        helper.setText(R.id.item_drugPirce,"药品单价："+ AllData.drugData.get(item.getDgCode()).getDgPrive());
        helper.setText(R.id.item_drugNumber,"购买数量："+ item.getRdNumber());
    }
}
