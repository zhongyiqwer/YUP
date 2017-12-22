package com.owo.utils;

import android.content.Context;

import com.wao.dogcat.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ppssyyy on 2017-06-04.
 */
public class TagMap {

    public static Map<Integer, String> readFile(Context context,int raw) {
        BufferedReader br = null;
        Map<Integer, String> result = new HashMap<Integer, String>();
        boolean flag = false;
        try {
            //获取文件中的字节
            InputStream inputStream=context.getResources().openRawResource(raw);
            //将字节转换为字符
            InputStreamReader isReader=new InputStreamReader(inputStream,"UTF-8");
            br = new BufferedReader(isReader);
            String s = br.readLine();
            int id = 0;
            String tag = "";
            String[] data = s.split("\\s+");
            while(s!=null){
                if (!flag) {
                    data[0] = "1";
                    flag = true;
                }
                id =   Integer.parseInt(data[0]);
                tag = data[1].trim();
                result.put(id, tag);
                //读取下一行
                s = br.readLine();
                if (s!=null) {
                    data = s.split("\\s+");
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
