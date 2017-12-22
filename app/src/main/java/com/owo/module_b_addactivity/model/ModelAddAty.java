package com.owo.module_b_addactivity.model;

import java.util.HashMap;

/**
 * Created by XQF on 2017/5/26.
 */
public interface ModelAddAty {
    /**
     * 发布任务
     *
     * @param map  参数
     * @param map1 文件
     */

    int sendTask(HashMap<String, String> map, HashMap<String, String> map1);
}
