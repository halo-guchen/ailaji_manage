package com.ailaji.manage.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@SuppressWarnings("all")
public class BaseExcelUtil<T> {

    /**
     * 
     * 读取文件中数据 <功能详细描述>
     * 
     * @param is 输入流
     * @param cls bean类型
     * @param field 对应字段
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @see [类、类#方法、类#成员]
     */
    public List<T> readGoodsItemFromXls(InputStream is, Class<T> cls, String[] field) throws IOException,
            InstantiationException, IllegalAccessException {

        XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
        is.close();
        List<T> list = new ArrayList<T>();
        // 循环工作表Sheet
        XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        if (hssfSheet == null) {
            return null;
        }
        // 循环行Row-从数据行开始
        for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            T t = cls.newInstance();
            XSSFRow hssfRow = hssfSheet.getRow(rowNum);
            HashMap<String, Object> map = new HashMap<String, Object>();
            // 循环row中的每一个单元格
            if (hssfRow == null)
                continue;
            for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                XSSFCell cell = hssfRow.getCell(i);
                // 格式转换
                String val = "";
                if (cell != null) {
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        val = cell.getStringCellValue();
                    } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                        val = cell.getBooleanCellValue() == true ? "true" : "false";
                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        BigDecimal valtemplete = new BigDecimal(cell.getNumericCellValue() + "");
                        if (new BigDecimal(valtemplete.longValue()).compareTo(valtemplete) == 0) {
                            val = valtemplete.longValue() + "";
                        } else {
                            val = valtemplete.toString();
                        }

                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        val = cell.getStringCellValue();
                    }
                } else {
                    val = "";
                }
                for (int n = 0; n < field.length; n++) {
                    if (i == n && !field[n].contains("&")) {
                        map.put(field[n], cell == null ? "" : val);
                    } else if (i == n) {
                        map.put(field[n].split("&")[0], cell == null ? "" : val.split("-")[0]);
                    }
                }
            }
            transMap2Bean(map, t);
            list.add(t);

        }
        return list;
    }

    /**
     * 
     * 对象转换称map
     * 
     * @param obj
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }

    /**
     * 
     * 设置样式
     * @param wb
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static CellStyle createGreenStyle(SXSSFWorkbook wb) {
        // 设置字体
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 11); // 字体高度
        font.setFontName("宋体"); // 字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

        CellStyle greenStyle = wb.createCellStyle();
        greenStyle.setFillBackgroundColor(HSSFCellStyle.LEAST_DOTS);
        greenStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
        greenStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        greenStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        greenStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        greenStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setRightBorderColor(HSSFColor.BLACK.index);
        greenStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setTopBorderColor(HSSFColor.BLACK.index);
        greenStyle.setFont(font);
        greenStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        greenStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
        greenStyle.setWrapText(true);

        return greenStyle;
    }

    /**
     * 
     * 设置Excel样式2007
     * @param wb
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unused")
    private static XSSFCellStyle createGreenStyle(XSSFWorkbook wb) {
        // 设置字体
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11); // 字体高度
        font.setFontName("宋体"); // 字体
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

        XSSFCellStyle greenStyle = wb.createCellStyle();
        greenStyle.setFillBackgroundColor(XSSFCellStyle.LEAST_DOTS);
        greenStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
        greenStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        greenStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        greenStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        greenStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setRightBorderColor(HSSFColor.BLACK.index);
        greenStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
        greenStyle.setTopBorderColor(HSSFColor.BLACK.index);
        greenStyle.setFont(font);
        greenStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        greenStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
        greenStyle.setWrapText(true);

        return greenStyle;
    }

    /**
     * 
     * map转对象
     * 
     * @param map
     * @param obj
     * @see [类、类#方法、类#成员]
     */
    private static void transMap2Bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法

                    Method setter = property.getWriteMethod();
                    String type = property.getPropertyType().toString();

                    if (type.contains("String")) {
                        setter.invoke(obj, value.toString());
                    } else if (type.contains("BigDecimal")) {
                        setter.invoke(obj, new BigDecimal(value.toString()));
                    } else if (type.contains("Integer")) {
                        setter.invoke(obj, (int) Double.parseDouble(value.toString()));
                    } else if (type.contains("long") || type.contains("Long")) {
                        BigDecimal bd;
                        try {
                            bd = new BigDecimal(value.toString());
                            setter.invoke(obj, Long.parseLong(bd.toPlainString()));
                        } catch (Exception e) {
                            setter.invoke(obj, Long.MIN_VALUE);
                        }

                    } else if (type.contains("int")) {
                        if (value instanceof String) {
                            try {
                                String val = ((String) value).split("\\.")[0];
                                setter.invoke(obj, Integer.parseInt(val));
                            } catch (Exception e) {
                                setter.invoke(obj, Long.MIN_VALUE);
                            }
                        }
                    } else {
                        setter.invoke(obj, value);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;

    }

    /**
     * 
     * 数据转ArrayList
     * 
     * @param strs
     * @return
     * @see [类、类#方法、类#成员]
     */
    private ArrayList<String> convertStrs2ArrayList(String[] strs) {
        if (strs == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(strs));
        return list;
    }

    /**
     * 构建excel模板
     * 
     * @param tableName 表头名
     * @param tableFild 表头对应的属性名
     * @param headerCheckList 表头格式属性
     * @param fileName excel文件名
     * @return
     */
    public static SXSSFWorkbook templateBuild(String[] tableName, String fileName) {

        SXSSFWorkbook wb = new SXSSFWorkbook(10000);
        SXSSFSheet sheet = wb.createSheet(fileName);
        XSSFCellStyle greenStyle = (XSSFCellStyle) createGreenStyle(wb);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        SXSSFRow row = null;
        // 添加excel头
        row = sheet.createRow(0);

        if (!ArrayUtils.isEmpty(tableName)) {
            Cell cell = null;
            for (int i = 0; i < tableName.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(tableName[i] == null ? "" : tableName[i]);
                cell.setCellStyle(greenStyle);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                sheet.setColumnWidth(i, 4000);
            }
        }
        return wb;
    }

    /**
     * 在已存在的excel上追加数据
     * 
     * @param data
     * @param tableField
     * @param wb
     * @param fileName
     * @return
     */
    public SXSSFWorkbook addToBuild(List<T> data, String[] tableField, SXSSFWorkbook wb) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if (!CollectionUtils.isEmpty(data)) {
            for (T t : data) {
                Map<String, Object> map = new HashMap<String, Object>();
                map = BaseExcelUtil.transBean2Map(t);
                list.add(map);
            }
        }
        SXSSFSheet sheet = wb.getSheetAt(0);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        int lastRowNum = sheet.getLastRowNum() + 1;
        // SXSSFRow row = null;
        // 添加excel内容
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            SXSSFRow row = sheet.createRow(i + lastRowNum);
            row.setRowStyle(style);
            Cell cell = null;

            for (int j = 0; j < tableField.length; j++) {
                String value = "";
                try {
                    String key = tableField[j];
                    if (key.contains("@")) {
                        cell = row.createCell(j, Cell.CELL_TYPE_NUMERIC);
                        value = map.get(key.replace("@", "")).toString();
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.valueOf(value));
                        cell.setCellStyle(style);
                    } else {
                        cell = row.createCell(j, Cell.CELL_TYPE_STRING);
                        value = map.get(key).toString();
                        cell.setCellValue(value);
                        cell.setCellStyle(style);
                    }
                } catch (Exception e) {
                    value = "";
                }
            }
        }
        return wb;
    }

    public void downloadDataModel(HttpServletResponse res, SXSSFWorkbook wb, String fileName) {
        if (wb == null) {
            return;
        }
        ServletOutputStream os;
        try {
            res.setContentType("application/vnd.ms-excel");
            // 以保存或者直接打开的方式把Excel返回到页面
            res.setHeader("Content-disposition",
                    "attachment; filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
            os = res.getOutputStream();

            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {

        }

    }

    public SXSSFWorkbook addToBuildByThread(List<T> data, String[] tableField, SXSSFWorkbook wb) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if (!CollectionUtils.isEmpty(data)) {
            for (T t : data) {
                Map<String, Object> map = new HashMap<String, Object>();
                map = BaseExcelUtil.transBean2Map(t);
                list.add(map);
            }
        }
        int size = list.size();
        SXSSFSheet sheet = wb.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum() + 1;
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        if (size > 4) {
            try {
                int length = size / 4;
                /**
                 * 使用线程池进行线程管理。
                 */
                ExecutorService es = Executors.newCachedThreadPool();
                /**
                 * 使用计数栅栏
                 */
                CountDownLatch doneSignal = new CountDownLatch(4);
                es.submit(new PoiWriter(doneSignal, sheet, lastRowNum, list.subList(0, length), style,
                        tableField));
                es.submit(new PoiWriter(doneSignal, sheet, lastRowNum + length, list.subList(length,
                        length * 2), style, tableField));
                es.submit(new PoiWriter(doneSignal, sheet, lastRowNum + length * 2, list.subList(length * 2,
                        length * 3), style, tableField));
                es.submit(new PoiWriter(doneSignal, sheet, lastRowNum + length * 3, list.subList(length * 3,
                        size), style, tableField));
                doneSignal.await();
                es.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // SXSSFRow row = null;
            // 添加excel内容
            for (int i = 0; i < size; i++) {
                Map<String, Object> map = list.get(i);
                SXSSFRow row = sheet.createRow(i + lastRowNum);
                row.setRowStyle(style);
                Cell cell = null;

                for (int j = 0; j < tableField.length; j++) {
                    String value = "";
                    try {
                        String key = tableField[j];
                        if (key.contains("@")) {
                            cell = row.createCell(j, Cell.CELL_TYPE_NUMERIC);
                            value = map.get(key.replace("@", "")).toString();
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(Double.valueOf(value));
                            cell.setCellStyle(style);
                        } else {
                            cell = row.createCell(j, Cell.CELL_TYPE_STRING);
                            value = map.get(key).toString();
                            cell.setCellValue(value);
                            cell.setCellStyle(style);
                        }
                    } catch (Exception e) {
                        value = "";
                    }
                }
            }
        }
        return wb;
    }

    /**
     * 进行sheet写操作的sheet。
     * 
     * @author guchen
     * 
     */
    protected static class PoiWriter implements Runnable {

        private final CountDownLatch doneSignal;
        private SXSSFSheet sheet;
        private List<Map<String, Object>> list;
        private int lastRowNum;
        private CellStyle style;
        private String[] tableField;

        public PoiWriter(CountDownLatch doneSignal, SXSSFSheet sheet, int lastRowNum,
                List<Map<String, Object>> list, CellStyle style, String[] tableField) {
            this.doneSignal = doneSignal;
            this.sheet = sheet;
            this.lastRowNum = lastRowNum;
            this.list = list;
            this.style = style;
            this.tableField = tableField;
        }

        public void run() {
            try {
                if (CollectionUtils.isNotEmpty(list)) {
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        // SXSSFRow row=sheet.createRow(i + lastRowNum);
                        SXSSFRow row = getRow(sheet, i + lastRowNum);
                        row.setRowStyle(style);
                        Cell cell = null;
                        for (int j = 0; j < tableField.length; j++) {
                            String value = "";
                            try {
                                String key = tableField[j];
                                if (key.contains("@")) {
                                    cell = row.createCell(j, Cell.CELL_TYPE_NUMERIC);
                                    value = map.get(key.replace("@", "")).toString();
                                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    cell.setCellValue(Double.valueOf(value));
                                    cell.setCellStyle(style);
                                } else {
                                    cell = row.createCell(j, Cell.CELL_TYPE_STRING);
                                    value = map.get(key).toString();
                                    cell.setCellValue(value);
                                    cell.setCellStyle(style);
                                }
                            } catch (Exception e) {
                                value = "";
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                doneSignal.countDown();
                System.out.println(doneSignal.getCount());
            }
        }

    }

    private static synchronized SXSSFRow getRow(SXSSFSheet sheet, int rownum) {
        return sheet.createRow(rownum);
    }

}
