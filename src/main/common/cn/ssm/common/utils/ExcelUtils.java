package cn.ssm.common.utils;

import cn.ssm.common.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import cn.common.framework.IConvertable;

import javax.servlet.http.HttpServletResponse;


/**
 * @Auther: ycl
 * @Date: 2018/9/27 09:20
 * @Description:
 * 使用步骤:
 * 1,创建Setting对象,进行设置,setting
 * 2,数据源为 List<Object> 类型,指定数据源 dataSource
 * 3,使用ExcelUtils.create(setting,dataSource,dataClass)生成File对象的zip包或者excel文件(程序自己判断)
 *
 *  需要详细看Setting的参数(下面选项可能会用的比较多)
 *      可以设置指定要导出哪些列
 *      可以设置时间的输出格式
 *      可以设置指定列,将数字转化成文字(比如我的数据是一个枚举类的id,可以转化成对应的文字)
 *
 *  可以看main方法中的例子
 *  别的可以不用看
 *
 *  下载excel表格的功能,可以先使用该工具类生成File对象,然后打开file的输出流,写入到response的输入流
 */
public class ExcelUtils{
    //使用的测试类
    public static void main(String[] args){
        //2条数据
        List<Object> dataList = new ArrayList<>();
        dataList.add(new User("燕成龙",22));
        dataList.add(new User("焦燕飞",22));
        dataList.add(new User("燕依晨",7));

        //创建setting对象,具体设置请看参数详情
        ExcelUtils.Setting setting = new ExcelUtils().new Setting(true);
        //导出指定列
        setting.setFilterField(new String[]{"name","age"});
        setting.setFieldName(new String[]{"姓名","年龄"});
        setting.setColumnWidth(new int[]{2000,3000});

        //导出所有列
      /*  setting.setColumnWidth(new int[]{2000,3000,5000,3000,3000,5000});//列宽
        setting.setFieldName(new String[]{"ID","创建时间","修改时间","状态","角色ID","权限ID"});*/

        setting.setFileName("测试文件");//生成的文件名
        setting.setSheetName("sheet1");//sheet名
        setting.setTitle("测试生成excel表格");//表内第一行标题名

        //将数字变成文字,可以不设置
        /*Map<Integer, IConvertable[]> convertableMap = new HashMap<>();
        convertableMap.put(3, StatusEnum.values());//3就表示是第4列要转化,StatusEnum 就是数字到文字转化的枚举类,需要传入StatusEnum.values()
        setting.setConvertableMap(convertableMap);//设置到setting中*/

        //设置每一个excel的的最大数据行数,可以不设置,默认10000
        setting.setMaxSizeOneSheet(1);//比如有2条数据,最大设置成1的话就会产生两个excel表格,压缩成一个zip包,返回zip的File对象,如果只有1条数据,那么返回的是一个表格的File对象
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File("C:\\Users\\ZJCA\\Desktop\\w.zip"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //调用create 方法,dataList
            ExcelUtils.create(setting, dataList, User.class,outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("完毕");
        }

    }

    public class Setting{
        private boolean debug;//是否进行参数校验，测试时传true,正式传false不进行校验提高效率
        public Setting(boolean debug){
            this.debug = debug;
        }

        public boolean getDebug() {return debug;}
        private String fileName;//生成的文件名,必填
        private String sheetName;//sheet名,必填
        private String title;//大标题,第一行,必填
        private String[] filterField;//过滤字段，如果输入，会按照这个过滤字段的顺序导出字段，如果不填写默认导出所有字段
        private String[] fieldName;//小标题，第二行，列名,必填，根据定义的类中字段的顺序来,如果过滤列没设置，这个列名必须写全（类中所有字段），如果定义了过滤列，需要和过滤列一一对应
        private int[] columnWidth;//每一列的宽度,必填
        private Integer maxSizeOneSheet;//每一个sheet的最大数据条数,大于这个条数就会生成zip包,默认10000,不必填
        private Map<Integer, IConvertable[]> convertableMap;//转化的值，Integer是列名,不必填
        private SimpleDateFormat dateFormat;//时间格式,不必填,默认yyyy-MM-dd HH:mm:ss
        private Integer rowHeight;//行高,不必填
        //set get
        public String getTitle() {return title; }
        public void setTitle(String title) {this.title = title; }
        public String[] getFilterField() {return filterField;}
        public void setFilterField(String[] filterField) {this.filterField = filterField;}
        public String[] getFieldName() {return fieldName; }
        public void setFieldName(String[] fieldName) {this.fieldName = fieldName; }
        public int[] getColumnWidth() {return columnWidth;}
        public void setColumnWidth(int[] columnWidth) {this.columnWidth = columnWidth;}
        public Map<Integer, IConvertable[]> getConvertableMap() {return convertableMap;}
        public void setConvertableMap(Map<Integer, IConvertable[]> convertableMap) {this.convertableMap = convertableMap;}
        public String getSheetName() {return sheetName;}
        public void setSheetName(String sheetName) {this.sheetName = sheetName;}
        public String getFileName() {return fileName;}
        public void setFileName(String fileName) {this.fileName = fileName;}
        public Integer getMaxSizeOneSheet() {return maxSizeOneSheet;}
        public void setMaxSizeOneSheet(Integer maxSizeOneSheet) {this.maxSizeOneSheet = maxSizeOneSheet;}
        public SimpleDateFormat getDateFormat() {return dateFormat;}
        public void setDateFormat(SimpleDateFormat dateFormat) {this.dateFormat = dateFormat;}
        public Integer getRowHeight() {return rowHeight; }
        public void setRowHeight(Integer rowHeight) {this.rowHeight = rowHeight;}
    }

    public static void create(Setting setting, List<Object> dataList, Class<?> dataListClz, HttpServletResponse response) throws Exception {
        OutputStream outputStream = response.getOutputStream();
        String checkResult = check(setting,dataList,dataListClz);
        if(checkResult != null){
            throw new Exception(checkResult);
        }
        response.setCharacterEncoding("utf-8");
        //计算数据大小
        int pageNum = 0;
        if(dataList.size() % setting.getMaxSizeOneSheet() == 0){
            pageNum = dataList.size() / setting.getMaxSizeOneSheet();
        }else{
            pageNum = dataList.size() / setting.getMaxSizeOneSheet() + 1;
        }
        if(pageNum > 1){
            //创建zip包
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(setting.getFileName() + ".zip","UTF-8"));
            createZip(setting,pageNum,dataList,dataListClz,outputStream);
        }else {
            //创建单个excel表格
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(setting.getFileName() + ".xls","UTF-8"));
            createExcel(setting,dataList,dataListClz,outputStream);
        }
    }
    /**
     * @param setting 生成表格的相关设置
     * @param dataList //数据集合,集合里每一个对象的类型必须一致
     * @param dataListClz //数据的 class类型
     * @return File  根据数据条数和每页最大数据量自动生成excel表格或者zip包的对象
     * @throws Exception 抛出异常就是生成失败
     */
    public static void create(Setting setting,List<Object> dataList, Class<?> dataListClz,OutputStream outputStream) throws Exception {
        String checkResult = check(setting,dataList,dataListClz);
        if(checkResult != null){
            throw new Exception(checkResult);
        }
        //计算数据大小
        int pageNum = 0;
        if(dataList.size() % setting.getMaxSizeOneSheet() == 0){
            pageNum = dataList.size() / setting.getMaxSizeOneSheet();
        }else{
            pageNum = dataList.size() / setting.getMaxSizeOneSheet() + 1;
        }
        if(pageNum > 1){
            //创建zip包
            createZip(setting,pageNum,dataList,dataListClz,outputStream);
        }else {
            //创建单个excel表格
           createExcel(setting,dataList,dataListClz,outputStream);
        }
    }


    private static String check(Setting setting,List<Object> dataList,Class<?> dataClz){
        if(setting == null){
            return "设置不能为空";
        }
        if(!setting.getDebug()){
            return null;
        }
        if(dataClz == null){
            return "传参错误,dataClz,数据类不能为空";
        }
        if(!isValidFileName(setting.getFileName())){
            return "文件名为空，或者命名不合法（文件名中不能包含\\/:*?\"<>|中的任意字符）";
        }
        if(StringUtils.isBlank(setting.getTitle())){
            return "大标题不能为空";
        }
        if(StringUtils.isBlank(setting.getSheetName())){
            return  "sheet名不能为空";
        }
        if(setting.getFieldName() == null || setting.getFieldName().length == 0){
            return "列名不能为空";
        }
        if(setting.getColumnWidth() == null || setting.getColumnWidth().length == 0){
            return "列宽不能为空";
        }
        if(dataList == null || dataList.size() == 0){
            return "数据源不能为空";
        }

        if(dataList.get(0).getClass() != dataClz){
            return "数据源类的类型和数据源数据的类型不一致";
        }

        if(setting.getFieldName().length != setting.getColumnWidth().length) {
            return "列宽或列名个数不对应";
        }
        if(setting.getFilterField() != null && setting.getFilterField().length != 0){
            if(setting.getFieldName().length != setting.getFilterField().length){
                return "指定导出的列数和设置的列名列数不一致";
            }
            //校验指定的列名是否和class的字段名能对应
            StringBuilder sb = new StringBuilder("字段名：");
            int filedErrNum = 0;
            for(int i= 0;i< setting.getFilterField().length;i++){
                try {
                    dataClz.getDeclaredField(setting.getFilterField()[i]);
                } catch (NoSuchFieldException e) {
                    filedErrNum++;
                    sb.append(setting.getFilterField()[i]).append("、");
                }
            }
            if(filedErrNum >0){
                sb.append("在" + dataClz.getName() + "类中无法找到");
                return sb.toString();
            }
        }else {
            if(setting.getFieldName().length != dataClz.getDeclaredFields().length){
                return "数据的列数和设置的列数不对应";
            }
        }

        if(setting.getMaxSizeOneSheet() == null){
            setting.setMaxSizeOneSheet(10000);
        }
        if(setting.getMaxSizeOneSheet() > 65536){
            return  "每一个xls表格的行数最大不能超过65536";
        }
        if(setting.getMaxSizeOneSheet() < 1){
            return "每页的数量不能小于1";
        }
        if(setting.getDateFormat() == null){
            setting.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
        if(setting.getRowHeight() == null){
            setting.setRowHeight(18);
        }
        if(setting.getRowHeight() < 1){
            return "单元格行高不能小于1";
        }

        //返回null表示设置参数合理
        return null;
    }
    //创建excel模板
    private static Workbook createMode(Setting setting){
        Row row;
        Cell cell;
        Workbook workBook = new HSSFWorkbook();
        Sheet sheet = workBook.createSheet();
        workBook.setSheetName(0, setting.getSheetName());
        setSheetColumnWidth(sheet,setting);
        row = sheet.createRow(0);
        row.setHeightInPoints(40);
        cell = row.createCell(0);
        cell.setCellValue(setting.getTitle());
        cell.setCellStyle(getTitleStyle(workBook));
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short)(setting.getFieldName().length-1)));
        row = sheet.createRow(1);
        row.setHeightInPoints(setting.getRowHeight());
        String[] fieldName =  setting.getFieldName();
        for (int i = 0; i < fieldName.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(getHeaderStyle(workBook));
            cell.setCellValue(fieldName[i]);
        }
        return  workBook;
    }


    //创建zip包
    private static void createZip(Setting setting,int pageNum,List<Object> dataList, Class<?> dataClz,OutputStream outputStream) throws Exception {
        ByteArrayOutputStream[] excelOutPutStream = new ByteArrayOutputStream[pageNum];
        String fileName = setting.getFileName();
        int pageSize = setting.getMaxSizeOneSheet();
        for(int i= 0; i<pageNum; i++){
            setting.setFileName(fileName + i + ".xls");
            List<Object> pageList;
            if(pageNum -i > 1){
                pageList = dataList.subList(i * pageSize, (i+1) * pageSize);
            }else{
                pageList = dataList.subList(i * pageSize, dataList.size());
            }
            excelOutPutStream[i] = new ByteArrayOutputStream();
           createExcel(setting,pageList,dataClz,excelOutPutStream[i]);
        }
        try {
            outputStream = new ZipOutputStream(outputStream);
            for (int i=0;i<excelOutPutStream.length;i++) {
                ((ZipOutputStream)outputStream).putNextEntry(new ZipEntry("temp" + i + ".xls"));
                //将流写入Entry
                ((ZipOutputStream)outputStream).write(excelOutPutStream[i].toByteArray());
                ((ZipOutputStream)outputStream).flush();
                ((ZipOutputStream)outputStream).closeEntry();
            }
        }catch (Exception e){
            throw  e;
        }
        ((ZipOutputStream)outputStream).finish();
    }

    private static void createExcel(Setting setting,List<Object> dataList, Class<?> dataClz,OutputStream outputStream) throws Exception{
        Workbook excel = createMode(setting);
        Sheet sheet = excel.getSheetAt(0);
        for(int i=0;i<dataList.size();i++){
            createRow(setting,sheet,getContextStyle(excel),dataList.get(i),i+2,dataClz);
        }
        File file = new File(setting.getFileName() + getRandomName() + ".xls");
        try {
            excel.write(outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //将一行数据写入表格
    private static  void createRow(Setting setting, Sheet sheet,CellStyle cellStyle,Object data,int rowNum,Class<?> dataClz) throws Exception {
        Field[] fields = null;
        String[] fieldFilter = setting.getFilterField();
        if(fieldFilter != null && fieldFilter.length > 0){
            fields = new Field[fieldFilter.length];
            for(int i=0; i< fieldFilter.length;i++){
                fields[i] = dataClz.getDeclaredField(fieldFilter[i]);
            }
        }else{
            fields = dataClz.getDeclaredFields();
        }

        Row row = sheet.createRow(rowNum);
        Cell cell;
        for(int i = 0;i<fields.length;i++){
            Field field = fields[i];
            field.setAccessible(true);
            Class classType = field.getType();
            Object cellValue = field.get(data);
            cell = row.createCell(i);
            if(cellValue == null){
                cell.setCellValue("");
            }else {
                if(classType == Date.class){
                    cell.setCellValue(setting.getDateFormat().format(cellValue));
                }else if(setting.getConvertableMap() != null && classType == Integer.class){
                    //判断是否要使用枚举类转化
                    IConvertable[] convert = setting.getConvertableMap().get(i);
                    if(convert != null){
                        boolean var1 = true;
                        for(IConvertable temp:convert){
                            if(cellValue.equals(temp.getId())){
                                cell.setCellValue(temp.getName());
                                var1 = false;
                                break;
                            }
                        }
                        if(var1) {
                            cell.setCellValue("无对应类型,id:" + cellValue + "," + convert.getClass());
                        }
                    }
                }else {
                    cell.setCellValue(String.valueOf(cellValue));
                }
            }
           cell.setCellStyle(cellStyle);
        }
    }




    //设置列宽
    private static void setSheetColumnWidth(Sheet sheet,Setting setting) {
        int[] widths = setting.getColumnWidth();
        for(int i = 0;i< widths.length;i++){
            sheet.setColumnWidth(i, widths[i]);
        }
    }

    //默认的第二行的样式
    private static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        // 边框颜色
        style.setTopBorderColor(IndexedColors.VIOLET.getIndex());
        style.setBottomBorderColor(IndexedColors.VIOLET.getIndex());
        style.setLeftBorderColor(IndexedColors.VIOLET.getIndex());
        style.setRightBorderColor(IndexedColors.VIOLET.getIndex());
        return style;
    }

    /**
     * 标题样式
     * @param workbook
     * @return
     */
    private static CellStyle getTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 字体居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);// 加粗
        style.setFont(font);
        return style;
    }

    /**
     * 正文样式
     * @param workbook
     * @return
     */
    private static CellStyle getContextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        return style;

    }
    //随机生成文件名字，防止重名
    private static synchronized Long getRandomName(){
        LockSupport.parkNanos(1L);
        return System.nanoTime();
    }

    public static boolean isValidFileName(String fileName) {
        if ( StringUtils.isBlank(fileName) || fileName.length() > 255){
            return false;
        }
        else {
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
        }
    }
}