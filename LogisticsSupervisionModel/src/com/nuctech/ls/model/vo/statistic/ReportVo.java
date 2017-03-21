package com.nuctech.ls.model.vo.statistic;

import java.util.ArrayList;

import net.sf.json.JSONObject;

public abstract class ReportVo<T> extends ArrayList<T> {

    private JSONObject data;
    private JSONObject column;

    public JSONObject getData() {
        return data;
    }

    public JSONObject getColumn() {
        return column;
    }

    public abstract void setColumn(JSONObject column);

}
