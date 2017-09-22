package com.ycm.kata.datacollection.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;
import com.ycm.kata.datacollection.utils.CommonUtil;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public class DataListActivity extends Activity implements GetDataListener, OnItemClickListener, View.OnClickListener, ExportListener {
    private Button btnAdd;
    private Button btnExport;
    private Button btnPrevious;
    private Button btnNext;
    private ListView listView;
    private MyAdapter myAdapter;
    private static List<ProjectEntity> dataSource;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private GetData getData;
    private int index = 0;
    private static final int VIEW_COUNT = 6;
    private UpdateViewHandler handler;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private String imageFile;
    private String decFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        btnAdd = findViewById(R.id.add_new_btn);
        btnAdd.setOnClickListener(this);
        btnExport = findViewById(R.id.export_btn);
        btnExport.setOnClickListener(this);
        btnPrevious = findViewById(R.id.previous_btn);
        btnPrevious.setOnClickListener(this);
        btnNext = findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        listView = findViewById(R.id.list);
        dataSource = new ArrayList<>();
        handler = new UpdateViewHandler(this);
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
        myAdapter = new MyAdapter(this, this);
        listView.setAdapter(myAdapter);
        View empty = LayoutInflater.from(this).inflate(R.layout.empy_layout, null);
        listView.setEmptyView(empty);
//        listView.setOnItemClickListener(this);
//        getData = new GetData(projectEntityDao, this);
//        getData.start();
    }

    @Override
    public void loadSuccess(List<ProjectEntity> dataSource) {
//        myAdapter.setDataSource(dataSource);
//        myAdapter.notifyDataSetChanged();
        handler.sendEmptyMessage(123);

    }

    @Override
    public int progress(int x) {
        return 0;
    }

    private static class UpdateViewHandler extends Handler {
        private WeakReference<DataListActivity> weakReference;

        UpdateViewHandler(DataListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123) {
                weakReference.get().myAdapter.setDataSource(dataSource);
                weakReference.get().myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataSource();
    }

    ExportData exportData;

    private void getDataSource() {
//        exportData = new ExportData()
        getData = new GetData(projectEntityDao, this);
        getData.start();
    }

    @Override
    public void textOnclickListener(int position) {
        Intent intent = new Intent();
        intent.setClass(this, EditActivity.class);
        intent.putExtra("projectEntity", dataSource.get(position));
        intent.putExtra("tag", "edit");
        startActivity(intent);
    }

    @Override
    public void buttonOnclickListener(int position) {
        deleteById(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_btn:
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.export_btn:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST);
                } else {
                    String ss = Environment.getExternalStorageDirectory().getPath() + File.separator + "data_collection" + File.separator + "file";
                    /*try {
//                        writeImage(ss, dataSource.get(0));

                    }*/ /*catch (IOException | InvalidFormatException e) {
                        e.printStackTrace();
                    }*/
//                    printer();
                    showDialg("正在导出数据");
                    exportData = new ExportData(dataSource, this);
                    dataSuccessHandler = new ExportDataSuccessHandler(this);
                    exportData.start();
                }
                break;
            case R.id.previous_btn:
                nextView();
                break;
            case R.id.next_btn:
                preView();
                break;
        }
    }

    ProgressDialog pDialog;

    private void showDialg(String message) {
        if (null == pDialog) {
            pDialog = new ProgressDialog(this);
        }
        pDialog.setMessage(message);//设置对话框中的提示信息
        // pDialog.setIndeterminate(indeterminate);//设置对话框里的进度条不显示进度值
//pDialog.setProgress(value);//设置对话框进度条的值
//pDialog.setProgressStyle(style);//设置对话框里进度条的进度值
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    private static class GetData extends Thread {
        private WeakReference<ProjectEntityDao> weakReference;
        private WeakReference<GetDataListener> listenerWeakReference;

        GetData(ProjectEntityDao projectEntityDao, GetDataListener listener) {
            weakReference = new WeakReference<>(projectEntityDao);
            listenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void run() {
            super.run();
            dataSource = weakReference.get().loadAll();
            listenerWeakReference.get().loadSuccess(dataSource);
        }
    }

    private void deleteById(int position) {
        if (dataSource == null || dataSource.isEmpty()) {
            return;
        }

        ProjectEntity pro = dataSource.get(position);
        projectEntityDao.delete(pro);
        getDataSource();
    }

    // 点击左边的Button，表示向前翻页，索引值要减1.
    public void preView() {
        myAdapter.setIndex(index--);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();
        // 检查Button是否可用。
        checkButton();
    }

    // 点击右边的Button，表示向后翻页，索引值要加1.
    public void nextView() {
        myAdapter.setIndex(index++);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();

        // 检查Button是否可用。
        checkButton();
    }

    public void checkButton() {
        // 索引值小于等于0，表示不能向前翻页了，以经到了第一页了。
        // 将向前翻页的按钮设为不可用。
        if (index <= 0) {
            btnNext.setEnabled(false);
        } else {
            btnPrevious.setEnabled(true);
        }
        // 值的长度减去前几页的长度，剩下的就是这一页的长度，如果这一页的长度比View_Count小，表示这是最后的一页了，后面在没有了。
        // 将向后翻页的按钮设为不可用。
        if (dataSource.size() - index * VIEW_COUNT <= VIEW_COUNT) {
            btnNext.setEnabled(false);
        }
        // 否则将2个按钮都设为可用的。
        else {
            btnNext.setEnabled(true);
        }

    }

    /**
     * 为了保证模板的可用，最好在现有的模板上复制后修改
     */
    private void printer() {
        try {
            saveFile("template.docx", this, R.raw.template);//文件目录res/raw
        } catch (IOException e) {
            e.printStackTrace();
        }
        //现场检查记录
        String aafileurl = Environment.getExternalStorageDirectory() + "/data_collection/file/template.docx";
        final String bbfileurl = Environment.getExternalStorageDirectory() + "/data_collection/file/template_printer.docx";
        //获取模板文件
        File demoFile = new File(aafileurl);
        //创建生成的文件
        File newFile = new File(bbfileurl);
        if (newFile.exists()) {
            newFile.delete();
        }
        for (int i = 0; i < dataSource.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("$project_name$", dataSource.get(i).getProjectName());
            map.put("$check_date$", CommonUtil.formatDate(dataSource.get(i).getCheckDate()));
            map.put("$unit_engineering$", dataSource.get(i).getUnitEngineering());
            map.put("$block_pile$", dataSource.get(i).getBlock() + " " + dataSource.get(i).getPilNo());
            map.put("$defects$", dataSource.get(i).getDefects());
            map.put("$remark$", dataSource.get(i).getRemark());
            map.put("$image$", dataSource.get(i).getImagePath());
//            writeDoc(demoFile, newFile, map);
        }

    }

    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     */
    public boolean writeDoc(File demoFile, File newFile, Map<String, String> map) {
        try {
            String imagePath = "";
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // 读取word文本内容
            Range range = hdt.getRange();
            // 替换文本内容
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }

            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getImageType(String imgFile) {
        byte format;
        if (imgFile.endsWith(".emf")) {
            format = 2;
        } else if (imgFile.endsWith(".wmf")) {
            format = 3;
        } else if (imgFile.endsWith(".pict")) {
            format = 4;
        } else if (!imgFile.endsWith(".jpeg") && !imgFile.endsWith(".jpg")) {
            if (imgFile.endsWith(".png")) {
                format = 6;
            } else if (imgFile.endsWith(".dib")) {
                format = 7;
            } else if (imgFile.endsWith(".gif")) {
                format = 8;
            } else if (imgFile.endsWith(".tiff")) {
                format = 9;
            } else if (imgFile.endsWith(".eps")) {
                format = 10;
            } else if (imgFile.endsWith(".bmp")) {
                format = 11;
            } else {
                format = 12;
            }
        } else {
            format = 5;
        }
        return format;
    }

    private void writeImage(String decFile, ProjectEntity pje) throws IOException, InvalidFormatException {
        File rf = new File(decFile);
        if (!rf.exists()) {
            rf.mkdir();
        }

        String name = System.currentTimeMillis() + ".doc";
        File f = new File(decFile, name);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileInputStream fileInputStream1 = new FileInputStream(f);
//        HWPFDocument document = new HWPFDocument(fileInputStream1);

        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        XWPFTable xwpfTable = r.getDocument().createTable(4, 4);
        xwpfTable.getRow(1).getCell(0).setText("项目名称");
        xwpfTable.getRow(1).getCell(1).setText(pje.getProjectName());
        xwpfTable.getRow(1).getCell(2).setText("检测日期");
        xwpfTable.getRow(1).getCell(3).setText(formatDate(pje.getCheckDate()));


        xwpfTable.getRow(2).getCell(0).setText("单位工程");
        xwpfTable.getRow(2).getCell(1).setText(pje.getUnitEngineering());
        xwpfTable.getRow(2).getCell(2).setText("标段及桩号");
        xwpfTable.getRow(2).getCell(3).setText(pje.getBlock() + " " + pje.getPilNo());

        File imageFilePath = new File(pje.getImagePath());
        FileInputStream fileInputStream = new FileInputStream(imageFilePath);
        xwpfTable.getRow(3).getCell(0).addParagraph().createRun().addPicture(fileInputStream, getImageType(pje.getImagePath()), pje.getImagePath(), Units.toEMU(200.0D), Units.toEMU(200.0D));
//        xwpfTable.getRow(3).getCell(1).;
        xwpfTable.getRow(3).getCell(2).setText("缺陷描述");
        xwpfTable.getRow(3).getCell(3).setText(pje.getDefects());

        xwpfTable.getRow(4).getCell(0).setText("备注");
        xwpfTable.getRow(4).getCell(1).setText(pje.getRemark());
        r.addBreak();
        FileOutputStream out = new FileOutputStream(decFile + File.separator + name);
        doc.write(out);
        out.close();
//        doc.close();
//        r.setText(imgFile);
//        r.addBreak();
//        FileOutputStream out = null;
//        try {
//            r.addPicture(new FileInputStream(imgFile), getImageType(imgFile), imgFile, Units.toEMU(200.0D), Units.toEMU(200.0D));
//            out = new FileOutputStream(decFile);
//        } catch (InvalidFormatException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            r.addBreak(BreakType.PAGE);
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                doc.write(out);
//                doc.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }


    }

    private String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }

    /**
     * 将文件复制到SD卡，并返回该文件对应的数据库对象
     *
     * @return
     * @throws IOException
     */
    public void saveFile(String fileName, Context context, int rawid) throws IOException {

        // 首先判断该目录下的文件夹是否存在
        File dir = new File(Environment.getExternalStorageDirectory() + "/data_collection/file/");
        if (!dir.exists()) {
            // 文件夹不存在 ， 则创建文件夹
            dir.mkdirs();
        }

        // 判断目标文件是否存在
        File file1 = new File(dir, fileName);

        if (!file1.exists()) {
            file1.createNewFile(); // 创建文件

        }
        InputStream input = context.getResources().openRawResource(rawid); // 获取资源文件raw
        try {

            FileOutputStream out = new FileOutputStream(file1); // 文件输出流、用于将文件写到SD卡中
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = (input.read(buffer))) != -1) { // 读取文件，-- 进到内存

                out.write(buffer, 0, len); // 写入数据 ，-- 从内存出
            }
            input.close();
            out.close(); // 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismiss() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    ExportDataSuccessHandler dataSuccessHandler;

    private class ExportDataSuccessHandler extends Handler {
        private WeakReference<DataListActivity> weakReference;

        ExportDataSuccessHandler(DataListActivity activity) {
            weakReference = new WeakReference<DataListActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
            Toast.makeText(weakReference.get(), "成功", Toast.LENGTH_SHORT).show();
        }
    }

    private class ExportData extends Thread {
        private WeakReference<ExportListener> weakReference;
        private List<ProjectEntity> dataSource;

        ExportData(List<ProjectEntity> dataSource, ExportListener exportListener) {
            this.dataSource = dataSource;
            weakReference = new WeakReference<>(exportListener);

        }

        @Override
        public void run() {
            super.run();
            writeExcel(dataSource, weakReference.get());
            dataSuccessHandler.sendEmptyMessage(123);
        }
    }

    private List<ProjectEntity> filterDataSource(List<ProjectEntity> projectEntityList) {
        String currentDate = formatDate(System.currentTimeMillis());
        List<ProjectEntity>

    }

    private void writeExcel(List<ProjectEntity> dataSource, ExportListener exportListener) {
        FileOutputStream fileOut = null;
        String rootPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "data_collection" + File.separator + "excel";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("项目数据" + System.currentTimeMillis());
        sheet1.setDefaultColumnWidth(30);
        // 生成一个样式
//            HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet1.createRow(0);
        //样式字体居中
//            style.setAlignment(HorizontalAlignment.CENTER);
        //给表头第一行一次创建单元格
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellValue("检测日期");
//            cell.setCellStyle(style);
        HSSFCell cell2 = row.createCell(1);
        cell2.setCellValue("项目名称");
//            cell.setCellStyle(style);
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellValue("工程单位");

        HSSFCell cell4 = row.createCell(3);
        cell4.setCellValue("标段及桩号");

        HSSFCell cell5 = row.createCell(4);
        cell5.setCellValue("备注");

        HSSFCell cell6 = row.createCell(5);
        cell6.setCellValue("缺陷描述");

        HSSFCell cell7 = row.createCell(6);
        CellAddress cell7Address = cell7.getAddress();
        CellRangeAddress newCra = new CellRangeAddress(cell7Address.getRow(), cell7Address.getRow(), cell7Address.getColumn(), cell7Address.getColumn() + 2);
        sheet1.addMergedRegion(newCra);
        cell7.setCellValue("图片");
        try {
            File rootFile = new File(rootPath);
            if (!rootFile.exists() && !rootFile.mkdir()) {
                return;
            }

            String p = Environment.getExternalStorageDirectory().getPath() + File.separator + "data_collection" + File.separator + "excel" + File.separator + "项目数据" + formatDate(System.currentTimeMillis()) + ".xls";
            File dexFile = new File(p);
            if (!dexFile.exists() && !dexFile.createNewFile()) {
                return;
            }
            fileOut = new FileOutputStream(p);
            for (int i = 0; i < dataSource.size() * 14; i += 14) {
                int index;
                if (i == 0) {
                    index = 0;
                } else {
                    index = i / 14;
                }
                row = sheet1.createRow(i + 1);
                HSSFCell c0 = row.createCell(0);
                CellAddress c0Address = c0.getAddress();
                CellRangeAddress newC0Ad = new CellRangeAddress(c0Address.getRow(), c0Address.getRow() + 13, c0Address.getColumn(), c0Address.getColumn());
                sheet1.addMergedRegion(newC0Ad);
                c0.setCellValue(formatDate(dataSource.get(index).getCheckDate()));

//                row.createCell(1)/*.setCellValue(dataSource.get(i).getProjectName())*/;
                HSSFCell c1 = row.createCell(1);
                CellAddress c1Address = c1.getAddress();
                CellRangeAddress newC1Ad = new CellRangeAddress(c1Address.getRow(), c1Address.getRow() + 13, c1Address.getColumn(), c1Address.getColumn());
                sheet1.addMergedRegion(newC1Ad);
                c1.setCellValue(dataSource.get(index).getProjectName());

                HSSFCell c2 = row.createCell(2);
                CellAddress c2Address = c2.getAddress();
                CellRangeAddress newC2Ad = new CellRangeAddress(c2Address.getRow(), c2Address.getRow() + 13, c2Address.getColumn(), c2Address.getColumn());
                sheet1.addMergedRegion(newC2Ad);
                c2.setCellValue(dataSource.get(index).getUnitEngineering());


//                row.createCell(2).setCellValue(dataSource.get(i).getUnitEngineering());
//                row.createCell(3).setCellValue(dataSource.get(i).getBlock() + " " + dataSource.get(i).getPilNo());
                HSSFCell c3 = row.createCell(3);
                CellAddress c3Address = c3.getAddress();
                CellRangeAddress newC3Ad = new CellRangeAddress(c3Address.getRow(), c3Address.getRow() + 13, c3Address.getColumn(), c3Address.getColumn());
                sheet1.addMergedRegion(newC3Ad);
                c3.setCellValue(dataSource.get(index).getBlock() + " " + dataSource.get(index).getPilNo());

//                row.createCell(4).setCellValue(dataSource.get(i).getRemark());
                HSSFCell c4 = row.createCell(4);
                CellAddress c4Address = c4.getAddress();
                CellRangeAddress newC4Ad = new CellRangeAddress(c4Address.getRow(), c4Address.getRow() + 13, c4Address.getColumn(), c4Address.getColumn());
                sheet1.addMergedRegion(newC4Ad);
                c4.setCellValue(dataSource.get(index).getRemark());

//                row.createCell(5).setCellValue(dataSource.get(i).getDefects());
                HSSFCell c5 = row.createCell(5);
                CellAddress c5Address = c5.getAddress();
                CellRangeAddress newC5Ad = new CellRangeAddress(c5Address.getRow(), c5Address.getRow() + 13, c5Address.getColumn(), c5Address.getColumn());
                sheet1.addMergedRegion(newC5Ad);
                c5.setCellValue(dataSource.get(index).getDefects());

//                HSSFCell hssfCell = row.createCell(6);
                HSSFCell c6 = row.createCell(6);
                CellAddress c6Address = c6.getAddress();
                CellRangeAddress newCr6Ad = new CellRangeAddress(c6Address.getRow(), c6Address.getRow() + 13, c6Address.getColumn(), c6Address.getColumn() + 3);
                sheet1.addMergedRegion(newCr6Ad);
                c6.setCellValue(dataSource.get(index).getDefects());
                CellAddress newC6Ad = c6.getAddress();

                byte[] buffer;
                File file = new File(dataSource.get(index).getImagePath());
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                buffer = bos.toByteArray();
                //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
                HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
                //anchor主要用于设置图片的属性
//                hssfCell.getRow().getRowNum();

                HSSFClientAnchor anchor =
                        new HSSFClientAnchor(0, 0, 255, 255, (short)c6Address.getColumn(),  c6Address.getRow(), (short)(c6Address.getColumn() + 2),  c6Address.getRow() + 13 /*(short) newC6Ad.getColumn(), newC6Ad.getRow(), (short) newC6Ad.getColumn(), newC6Ad.getRow()*/);

                anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                //插入图片
                patriarch.createPicture(anchor, wb.addPicture(buffer, HSSFWorkbook.PICTURE_TYPE_JPEG));
                // 写入excel文件
                exportListener.progress(index);
            }
            wb.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileOut != null) {
            try {
                fileOut.flush();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        try {
//
//
//
//
//
//
//
//
////            sheet1.setDefaultColumnWidth((short)15);
////            // 生成一个样式
//////            HSSFCellStyle style = wb.createCellStyle();
////            //创建第一行（也可以称为表头）
////            HSSFRow row = sheet1.createRow(0);
////            //样式字体居中
//////            style.setAlignment(HorizontalAlignment.CENTER);
////            //给表头第一行一次创建单元格
////            HSSFCell cell = row.createCell((short) 0);
////            cell.setCellValue("学生编号");
//////            cell.setCellStyle(style);
////            cell = row.createCell( (short) 1);
////            cell.setCellValue("学生姓名");
//////            cell.setCellStyle(style);
////            cell = row.createCell((short) 2);
////            cell.setCellValue("学生性别");
//////            cell.setCellStyle(style);
//
//            //添加一些数据，这里先写死，大家可以换成自己的集合数据
////            List<S> list = new ArrayList<student>();
////            list.add(new Student(111,张三,男));
////            list.add(new Student(111,李四,男));
////            list.add(new Student(111,王五,女));
//
//            //向单元格里填充数据
//            for (short i = 0; i < 1; i++) {
//                row = sheet1.createRow(i + 1);
//                row.createCell(0).setCellValue("hhh");
//                row.createCell(1).setCellValue("aaa");
//                row.createCell(2).setCellValue("ddd");
//            }
//
//
//
//
//
//
//
//
//
//
//            System.out.println("----Excle文件已生成------");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fileOut != null) {
//                try {
//                    fileOut.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}