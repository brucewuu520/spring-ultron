package org.springultron.qrcode.util;


import org.springultron.qrcode.constants.MediaType;

/**
 * Created by @author yihui in 19:53 18/9/10.
 */
public class BasicFileUtils {

    /**
     * 是否windows系统
     */
    public static boolean isWinOS() {
        boolean isWinOS = false;
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String sharpOsName = osName.replaceAll("windows", "{windows}")
                    .replaceAll("^win([^a-z])", "{windows}$1").replaceAll("([^a-z])win([^a-z])", "$1{windows}$2");
            isWinOS = sharpOsName.contains("{windows}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isWinOS;
    }

    public static boolean isAbsFile(String fileName) {
        if (isWinOS()) {
            // windows 操作系统时，绝对地址形如  c:\descktop
            return fileName.contains(":") || fileName.startsWith("\\");
        } else {
            // mac or linux
            return fileName.startsWith("/");
        }
    }

    /**
     * 将用户目录下地址~/xxx 转换为绝对地址
     */
    public static String parseHomeDir2AbsDir(String path) {
        String homeDir = System.getProperties().getProperty("user.home");
        return path.replace("~", homeDir);
    }

    /**
     * 根据文件的mime获取文件类型
     */
    public static MediaType getMediaType(String path) {
        String magicNum = FileReadUtils.getMagicNum(path);
        if (magicNum == null) {
            return null;
        }

        return MediaType.typeOfMagicNum(magicNum);
    }
}
