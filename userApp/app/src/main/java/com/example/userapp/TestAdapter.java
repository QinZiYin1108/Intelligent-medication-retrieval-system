package com.example.userapp;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import androidx.annotation.Nullable;

public class TestAdapter extends BaseQuickAdapter<record, BaseViewHolder> {
    public TestAdapter(@Nullable List<record> data) {
        super(R.layout.item_drug_record,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, record item) {
        helper.addOnClickListener(R.id.itemView);
        helper.setText(R.id.itemDcName,AllData.docuterData.get(item.getDcCode()).getDcName());
        String drugName = "订单药品：";
        for (recordDrug entry : item.getAllDrug()){
            drugName += AllData.drugData.get(entry.getDgCode()).getDgName()+"  ";
        }
        helper.setText(R.id.itemDrugName,drugName);
        Timestamp reTime = item.getReTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用 SimpleDateFormat 格式化 Timestamp 对象为字符串
        String formattedString = dateFormat.format(reTime);
        helper.setText(R.id.itemDrugTime,formattedString);

    }
}
