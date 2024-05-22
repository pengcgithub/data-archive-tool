package com.smartlink.archive.task.generate;

import com.smartlink.archive.enums.ArchiveModeEnum;
import com.smartlink.archive.infrastructure.config.ArchiveConfig;
import com.smartlink.archive.infrastructure.config.DataArchiveProperties;
import com.smartlink.archive.infrastructure.db.entity.ArchiveConfigEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by alan on 2022/8/25.
 */
class GeneratePtArchiverCmdTest {

    @Test
    void generateCmd() {

        DataArchiveProperties dataArchiveProperties = new DataArchiveProperties();

        ArchiveConfig archiveConfig = new ArchiveConfig();
        archiveConfig.setBatchSize("1000");
        archiveConfig.setTxnSize("1000");
        dataArchiveProperties.setArchiveConfig(archiveConfig);

        dataArchiveProperties.setArchiveUser("root");
        dataArchiveProperties.setArchivePwd("123qweASD");
        dataArchiveProperties.setPtArchiverPath("/opt/homebrew/bin");

        ArchiveConfigEntity archiveConfigEntity = ArchiveConfigEntity
                .builder()
                .sourceHost("192.168.3.15")
                .sourcePort(3306)
                .sourceDb("itsmboot-ht")
                .sourceTable("itsm_service_work_list")

                .destHost("192.168.3.15")
                .destPort(3306)
                .destDb("itsmboot_archive")
                .destTable("itsm_service_work_list")

                .archiveMode(ArchiveModeEnum.ARCHIVE.name())
                .charset("UTF8")
                .archiveCondition("end_date < '2022-08-27'")
                .build();

        String cmd = GeneratePtArchiverCmd.generateCmd(dataArchiveProperties, archiveConfigEntity);
        assertThat(cmd).isNotEmpty();
    }

}