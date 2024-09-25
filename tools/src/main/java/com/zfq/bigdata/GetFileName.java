package com.zfq.bigdata;

import java.io.File;

/**
 * @ClassName GetFileName
 * @Description TODO 获取文件指定目录的文件,远程修改，本地回拉
 * @Author ZFQ
 * @Date 2024/9/25 下午 02:21
 * @Version 1.0
 */
public class GetFileName {
    public static void main(String[] args) {
        //获取文件路径文件夹下的全部文件列表
        System.out.println("文件有如下：");
        //表示一个文件路径
        File file = new File("D:\\桌面\\深圳展厅-数据同步\\深圳展厅-数据隔离-8张表");
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();
        //foreach遍历数组
        for (File file2 : files) {
            //打印文件列表：只读取名称使用getName();
            //System.out.println("路径："+file2.getPath());
            //
            System.out.println("/data/program/datax/bin/datax.py /data/program/sync_datas_scripts/szzt_isolate_8/"+file2.getName());
        }

    }
}
