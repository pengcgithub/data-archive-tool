package com.smartlink.archive.task.execute;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * shell 命令执行器<br/>
 *
 * Created by pengcheng on 2024/5/6.
 */
@Slf4j
public class ShellCommandActuator {

    public static final String SHELL_PATH = "../../shell";
    public static final String TEMPLATE_PATH = "../../template";

    /**
     * 执行Shell 命令
     *
     * @param shellCmd shell命令字符串
     * @return
     */
    public static String exec(String shellCmd) {

        ShellFileSource shellFileSource = null;
        Process process = null;
        try {

            // 生成脚本文件
            shellFileSource = createShellFile(shellCmd);

            // 执行命令
            process = execCmd(shellFileSource.generateCmd());

            // 获取日志
            return getLogs(process);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {

            // 脚本执行完成后，删除脚本
            FileUtil.del(shellFileSource.getFile());
        }
    }

    private static ShellFileSource createShellFile(String shellCmd) {

        String fileName = UUID.randomUUID().toString() + RandomUtils.nextInt(1, 100) + ".sh";

        // 如果文件夹不存在创建文件夹
        File directory = FileUtil.mkdir(SHELL_PATH);
        directory.setExecutable(true);
        directory.setReadable(true);
        directory.setWritable(true);

        // 创建文件
        File shellFile = FileUtil.newFile(directory.getPath() + "/" + fileName);
        try {
            shellFile.createNewFile();
            shellFile.setExecutable(true);
            shellFile.setReadable(true);
            shellFile.setWritable(true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 添加脚本内容
        FileUtil.appendString(shellCmd, shellFile, Charset.forName("utf-8"));

        return new ShellFileSource(fileName, shellFile.getPath(), shellFile);
    }

    public static ShellFileSource createTemplateFile(String templateStr) {

        String fileName = UUID.randomUUID().toString() + RandomUtils.nextInt(1, 100) + ".json";

        // 如果文件夹不存在创建文件夹
        File directory = FileUtil.mkdir(TEMPLATE_PATH);
        directory.setExecutable(true);
        directory.setReadable(true);
        directory.setWritable(true);

        // 创建文件
        File shellFile = FileUtil.newFile(directory.getPath() + "/" + fileName);
        try {
            shellFile.createNewFile();
            shellFile.setExecutable(true);
            shellFile.setReadable(true);
            shellFile.setWritable(true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 添加脚本内容
        FileUtil.appendString(templateStr, shellFile, Charset.forName("utf-8"));

        return new ShellFileSource(fileName, shellFile.getPath(), shellFile);
    }

    private static String getLogs(Process process) {
        InputStream in = null;

        StringBuilder sb = new StringBuilder();

        try {

            try {
                in = process.getInputStream();
                sb.append(IoUtil.read(in, Charset.forName("UTF-8")));
            } finally {
                IoUtil.close(in);
            }

            try {
                // 如果有error日志，直接抛出异常
                in = process.getErrorStream();
                String errorLog = IoUtil.read(in, Charset.forName("UTF-8"));
                if (StringUtils.isNotBlank(errorLog)) {
                    throw new IORuntimeException(errorLog);
                }
            } finally {
                IoUtil.close(in);
            }
        } finally {
            process.destroy();
        }

        return sb.toString();
    }

    private static Process execCmd(String shellCmd) {
        Process process = null;
        try {

            log.info("[BIZ] 开始执行Shell脚本：shellCmd:{}", shellCmd);

            process = Runtime.getRuntime().exec(shellCmd);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        // 等待脚本执行完毕
        wait(process);

        return process;
    }

    private static void wait(Process process) {
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    static class ShellFileSource {

        private String fileName;

        private String filePath;

        private File file;

        public ShellFileSource(String fileName, String filePath, File file) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.file = file;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public File getFile() {
            return file;
        }

        public String generateCmd() {
            return StringUtils.join("sh ", "-c ", filePath);
        }

    }

}
