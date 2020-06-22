package org.springultron.qrcode.util;

import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Created by yihui on 2017/7/13.
 */
public class FileWriteUtil {

    public static String TEMP_PATH = "/tmp/quickmedia/";

    public static String getTmpPath() {
        // 优先从系统配置中获取获取临时目录参数，不存在时，用兜底的目录
        String tmpPathEnvProperties = System.getProperty("ultron.tmp.path");
        if (tmpPathEnvProperties != null) {
            if (tmpPathEnvProperties.endsWith("/")) {
                TEMP_PATH = tmpPathEnvProperties;
            } else {
                TEMP_PATH = tmpPathEnvProperties + "/";
            }
        }

        return TEMP_PATH + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static <T> FileInfo saveFile(T src, String inputType) throws Exception {
        if (src instanceof String) {
            // 给的文件路径，区分三中，本地绝对路径，相对路径，网络地址
            return saveFileByPath((String) src);
        } else if (src instanceof URI) {
            // 网络资源文件时，需要下载到本地临时目录下
            return saveFileByURI((URI) src);
        } else if (src instanceof InputStream) {
            // 输入流保存在到临时目录
            return saveFileByStream((InputStream) src, inputType);
        } else {
            throw new IllegalStateException(
                    "save file parameter only support String/URI/InputStream type! but input type is: " +
                            (src == null ? null : src.getClass()));
        }
    }


    /**
     * 根据path路径 生成源文件信息
     */
    private static FileInfo saveFileByPath(String path) throws Exception {
        if (path.startsWith("http")) {
            return saveFileByURI(URI.create(path));
        }

        String tmpAbsFile;
        if (BasicFileUtils.isAbsFile(path)) {
            // 绝对路径
            tmpAbsFile = path;
        } else if (path.startsWith("~")) {
            // 绝对路径，只是前缀为用户的根据目录如 将 ~/test.temp 转换为 /home/yihui/test/temp
            tmpAbsFile = BasicFileUtils.parseHomeDir2AbsDir(path);
        } else { // 相对路径
            tmpAbsFile = FileWriteUtil.class.getClassLoader().getResource(path).getFile();
        }

        return parseAbsFileToFileInfo(tmpAbsFile);
    }


    /**
     * 下载远程文件， 保存到临时目录, 生成文件信息
     */
    private static FileInfo saveFileByURI(URI uri) throws Exception {
        String path = uri.getPath();
        if (path.endsWith("/")) {
            throw new IllegalArgumentException("a select uri should be choosed! but input path is: " + path);
        }

        int index = path.lastIndexOf("/");
        String filename = path.substring(index + 1);

        FileInfo fileInfo = new FileInfo();
        extraFileName(filename, fileInfo);
        fileInfo.setPath(getTmpPath());

        try {
            InputStream inputStream = DownloadUtils.downloadFile(uri);
            return saveFileByStream(inputStream, fileInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FileInfo saveFileByStream(InputStream inputStream, String fileType) throws Exception {
        // 临时文件生成规则  当前时间戳 + 随机数 + 后缀
        return saveFileByStream(inputStream, getTmpPath(), genTempFileName(), fileType);
    }

    /**
     * 将字节流保存到文件中
     */
    public static FileInfo saveFileByStream(InputStream stream, String path, String filename, String fileType)
            throws FileNotFoundException {
        return saveFileByStream(stream, new FileInfo(path, filename, fileType));
    }

    public static FileInfo saveFileByStream(InputStream stream, FileInfo fileInfo) throws FileNotFoundException {
        if (fileInfo.getPath() != null) {
            mkDir(new File(fileInfo.getPath()));
        }

        String tempAbsFile = fileInfo.getPath() + "/" + fileInfo.getFilename() + "." + fileInfo.getFileType();
        BufferedOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(stream);
            outputStream = new BufferedOutputStream(new FileOutputStream(tempAbsFile));
            int len = inputStream.available();
            //判断长度是否大于4k
            if (len <= 4096) {
                byte[] bytes = new byte[len];
                inputStream.read(bytes);
                outputStream.write(bytes);
            } else {
                int byteCount = 0;
                //1M逐个读取
                byte[] bytes = new byte[4096];
                while ((byteCount = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, byteCount);
                }
            }

            return fileInfo;
        } catch (Exception e) {
//            log.error("save stream into file error! filename: {} e: {}", tempAbsFile, e);
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
//                log.error("close stream error! e: {}", e);
            }
        }
    }

    /**
     * 用于生成临时文件名后缀的随机生成器
     */
    private static final Random FILENAME_GEN_RANDOM = new Random();

    /**
     * 临时文件名生成： 时间戳 + 0-1000随机数
     */
    private static String genTempFileName() {
        return System.currentTimeMillis() + "_" + FILENAME_GEN_RANDOM.nextInt(1000);
    }

    /**
     * 递归创建文件夹
     *
     * @param path 由目录创建的file对象
     */
    public static void mkDir(File path) throws FileNotFoundException {
        if (path.getParentFile() == null) {
            path = path.getAbsoluteFile();
        }

        if (!path.getParentFile().exists()) {
            mkDir(path.getParentFile());
        }
        modifyFileAuth(path);
        if (!path.exists() && !path.mkdir()) {
            throw new FileNotFoundException();
        }
    }

    /**
     * 修改文件权限，设置为可读写
     */
    private static void modifyFileAuth(File file) {
        boolean ans = file.setExecutable(true, false);
        ans = file.setReadable(true, false) && ans;
        ans = file.setWritable(true, false) && ans;
//        if (log.isDebugEnabled()) {
//            log.debug("create file auth : {}", ans);
//        }
    }

    /**
     * 根据绝对路径解析出 目录 + 文件名 + 文件后缀
     *
     * @param absFile 全路径文件名
     */
    public static FileInfo parseAbsFileToFileInfo(String absFile) {
        FileInfo fileInfo = new FileInfo();
        extraFilePath(absFile, fileInfo);
        extraFileName(fileInfo.getFilename(), fileInfo);
        return fileInfo;
    }

    /**
     * 根据绝对路径解析 目录 + 文件名（带后缀）
     */
    private static void extraFilePath(String absFilename, FileInfo fileInfo) {
        int index = absFilename.lastIndexOf("/");
        if (index < 0) {
            fileInfo.setPath(getTmpPath());
            fileInfo.setFilename(absFilename);
        } else {
            fileInfo.setPath(absFilename.substring(0, index));
            fileInfo.setFilename(index + 1 == absFilename.length() ? "" : absFilename.substring(index + 1));
        }
    }

    /**
     * 根据带后缀文件名解析 文件名 + 后缀
     */
    private static void extraFileName(String fileName, FileInfo fileInfo) {
        int index = fileName.lastIndexOf(".");
        if (index < 0) {
            fileInfo.setFilename(fileName);
            fileInfo.setFileType("");
        } else {
            fileInfo.setFilename(fileName.substring(0, index));
            fileInfo.setFileType(index + 1 == fileName.length() ? "" : fileName.substring(index + 1));
        }
    }

    public static class FileInfo {
        /**
         * 文件所在的目录
         */
        private String path;
        /**
         * 文件名 （不包含后缀）
         */
        private String filename;
        /**
         * 文件类型
         */
        private String fileType;

        public FileInfo() {
        }

        public FileInfo(String path, String filename, String fileType) {
            this.path = path;
            this.filename = filename;
            this.fileType = fileType;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getAbsFile() {
            return path + "/" + filename + "." + fileType;
        }
    }

}
