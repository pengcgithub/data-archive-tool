package com.smartlink.archive.task.generate;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.smartlink.archive.constant.ArchiveConfigConstants;
import com.smartlink.archive.enums.ArchiveModeEnum;
import com.smartlink.archive.enums.ArchiveTypeEnum;
import com.smartlink.archive.infrastructure.config.*;
import com.smartlink.archive.infrastructure.db.entity.ArchiveConfigEntity;
import com.smartlink.archive.infrastructure.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created by pengcheng on 2024/4/30.
 */
public class GeneratePtArchiverCmd {

    private static final String CMD_NAME = "templates/archiverShell-";
    private static final String TEMPLATE_NAME = "templates/mysqlJson-";

    static {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    }

    public static String generateCmd(DataArchiveProperties dataArchiveProperties, ArchiveConfigEntity archiveConfigEntity) {
        if (StringUtils.equals(archiveConfigEntity.getArchiveType(), ArchiveTypeEnum.MYSQL_DATAX.name().toLowerCase(Locale.ROOT))) {
            return String.join(" ", "python", dataArchiveProperties.getDataxPath()+"/bin/datax.py", "{0}");
        }

        VelocityContext context = buildVelocityContext(dataArchiveProperties, archiveConfigEntity);

        // 将VelocityContext写入到模版
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(CMD_NAME + archiveConfigEntity.getArchiveMode().toLowerCase(Locale.ROOT) + ".vm", "UTF-8");
        tpl.merge(context, sw);

        return sw.toString();
    }

    public static String generateTemplate(DataArchiveProperties dataArchiveProperties, ArchiveConfigEntity archiveConfigEntity) {
        if (StringUtils.equals(archiveConfigEntity.getArchiveType(), ArchiveTypeEnum.MYSQL.name().toLowerCase(Locale.ROOT))) {
            return StringUtils.EMPTY;
        }

        VelocityContext context = buildVelocityContext(dataArchiveProperties, archiveConfigEntity);

        // 将VelocityContext写入到模版
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(TEMPLATE_NAME + archiveConfigEntity.getArchiveMode().toLowerCase(Locale.ROOT) + ".vm", "UTF-8");
        tpl.merge(context, sw);

        return sw.toString();
    }

    private static VelocityContext buildVelocityContext(DataArchiveProperties dataArchiveProperties, ArchiveConfigEntity archiveConfigEntity) {

        // 将配置Bean转换为Map
        Map<String, Object> param = BeanUtil.beanToMap(dataArchiveProperties);
        param.putAll(BeanUtil.beanToMap(archiveConfigEntity));

        // 转换归档模式
        param.put("archiveModeEnum", ArchiveModeEnum.valueOf(archiveConfigEntity.getArchiveMode()).getCmd());

        // 转换时间格式
        StrSubstitutor sub = CommonExecutorOperations.getSub(archiveConfigEntity);
        String archiveCondition = sub.replace(StringUtils.trim(archiveConfigEntity.getArchiveCondition()));
        param.put("archiveCondition", archiveCondition);

        // 转换查询条件中的时间
        String querySql = sub.replace(StringUtils.trim(archiveConfigEntity.getQuerySql()));
        param.put("querySql", querySql);

        // 初始化ARCHIVE模式的归档配置
        initArchiveConfig(param, dataArchiveProperties, archiveConfigEntity);

        // 初始化ARCHIVE_TO_FILE模式的归档配置
        initArchiveToFileConfig(param, dataArchiveProperties, archiveConfigEntity);

        // 初始化DELETE模式的归档配置
        initDeleteConfig(param, dataArchiveProperties, archiveConfigEntity);

        // 数据库配置的properties优先级最高
        String extensionProperties = archiveConfigEntity.getExtensionProperties();
        if (StringUtils.isNotEmpty(extensionProperties)) {
            JSONObject jsonObject = JsonUtils.toJson(extensionProperties);
            DataArchiveProperties dataArchive = jsonObject.toJavaObject(DataArchiveProperties.class);
            Map<String, Object> extensionParam = BeanUtil.beanToMap(dataArchive, false, true);
            extensionParam.put("notFirst", true);

            initArchiveConfig(extensionParam, dataArchive, archiveConfigEntity);
            initArchiveToFileConfig(extensionParam, dataArchive, archiveConfigEntity);
            initDeleteConfig(extensionParam, dataArchive, archiveConfigEntity);

            extensionParam.entrySet().removeIf(entry -> entry.getValue() == null);
            param.putAll(extensionParam);
        }

        // 填充到VelocityContext，提供给模版使用
        VelocityContext context = new VelocityContext(param);
        return context;
    }

    private static void initDeleteConfig(Map<String, Object> param, DataArchiveProperties dataArchiveProperties, ArchiveConfigEntity archiveConfigEntity) {

        DeleteConfig deleteConfig = dataArchiveProperties.getDeleteConfig();
        if (Objects.isNull(deleteConfig) || !StringUtils.equalsIgnoreCase(ArchiveModeEnum.DELETE.name(), archiveConfigEntity.getArchiveMode())) {
//            defaultConfig(param);
            return;
        }

        param.putAll(BeanUtil.beanToMap(deleteConfig));

        // 如果非首次初始化，则不需要实例化默认值
        if (ObjectUtil.isNotNull(param.getOrDefault("notFirst", null))) {
            return;
        }

        // 填充默认的批量每次归档的数据数量
        if (StringUtils.isBlank(deleteConfig.getBatchSize())) {
            param.put("batchSize", ArchiveConfigConstants.DEFAULT_BATCHSIZE);
        }

        // 填充默认的指定每个事务的大小（行数）
        if (StringUtils.isBlank(deleteConfig.getTxnSize())) {
            param.put("txnSize", ArchiveConfigConstants.DEFAULT_TXNSIZE);
        }

    }

    private static void initArchiveToFileConfig(Map<String, Object> param, DataArchiveProperties dataArchiveProperties, ArchiveConfigEntity archiveConfigEntity) {

        ArchiveToFileConfig archiveToFileConfig = dataArchiveProperties.getArchiveToFileConfig();
        if (Objects.isNull(archiveToFileConfig) || !StringUtils.equalsIgnoreCase(ArchiveModeEnum.ARCHIVE_TO_FILE.name(), archiveConfigEntity.getArchiveMode())) {
//            defaultConfig(param);
            return;
        }

        param.putAll(BeanUtil.beanToMap(archiveToFileConfig));

        // 如果非首次初始化，则不需要实例化默认值
        if (ObjectUtil.isNotNull(param.getOrDefault("notFirst", null))) {
            return;
        }

        // 导出的文件名称
        String nowTime = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
        String writeFileName = String.join("-", archiveConfigEntity.getSourceDb(),
                StringUtils.defaultString(archiveConfigEntity.getSourceTable(), "table"),
                String.valueOf(archiveConfigEntity.getId()), nowTime);
        param.put("writeFileName", writeFileName);

        // TxtFileWriter写入前数据清理处理模式
        if (StringUtils.isBlank(archiveToFileConfig.getWriteMode())) {
            param.put("writeMode", "truncate");
        }

        // 读取的字段分隔符
        if (StringUtils.isBlank(archiveToFileConfig.getFieldDelimiter())) {
            param.put("fieldDelimiter", ",");
        }

        if (StringUtils.isBlank(archiveToFileConfig.getEncoding())) {
            param.put("encoding", "utf-8");
        }

        if (StringUtils.isBlank(archiveToFileConfig.getFileFormat())) {
            param.put("fileFormat", "csv");
        }

        // 填充默认的归档文件路径
        if (StringUtils.isBlank(archiveToFileConfig.getArchiveFilePath())) {
            param.put("archiveFilePath", ArchiveConfigConstants.DEFAULT_ARCHIVE_FILE_PATH);
        }

        // 填充默认的批量每次归档的数据数量
        if (StringUtils.isBlank(archiveToFileConfig.getBatchSize())) {
            param.put("batchSize", ArchiveConfigConstants.DEFAULT_BATCHSIZE);
        }

        // 填充默认的指定每个事务的大小（行数）
        if (StringUtils.isBlank(archiveToFileConfig.getTxnSize())) {
            param.put("txnSize", ArchiveConfigConstants.DEFAULT_TXNSIZE);
        }

    }

    private static void initArchiveConfig(Map<String, Object> param, DataArchiveProperties dataArchiveProperties, ArchiveConfigEntity archiveConfigEntity) {

        ArchiveConfig archiveConfig = dataArchiveProperties.getArchiveConfig();
        if (Objects.isNull(archiveConfig) || !StringUtils.equalsIgnoreCase(ArchiveModeEnum.ARCHIVE.name(), archiveConfigEntity.getArchiveMode())) {
            return;
        }

        param.putAll(BeanUtil.beanToMap(archiveConfig));

        // 如果非首次初始化，则不需要实例化默认值
        if (ObjectUtil.isNotNull(param.getOrDefault("notFirst", null))) {
            return;
        }

        // 填充默认的批量每次归档的数据数量
        if (StringUtils.isBlank(archiveConfig.getBatchSize())) {
            param.put("batchSize", ArchiveConfigConstants.DEFAULT_BATCHSIZE);
        }

        // 填充默认的指定每个事务的大小（行数）
        if (StringUtils.isBlank(archiveConfig.getTxnSize())) {
            param.put("txnSize", ArchiveConfigConstants.DEFAULT_TXNSIZE);
        }

    }

    private static void defaultConfig(Map<String, Object> param) {
        param.put("archiveFilePath", ArchiveConfigConstants.DEFAULT_ARCHIVE_FILE_PATH);
        param.put("batchSize", ArchiveConfigConstants.DEFAULT_BATCHSIZE);
        param.put("txnSize", ArchiveConfigConstants.DEFAULT_TXNSIZE);
    }

}
