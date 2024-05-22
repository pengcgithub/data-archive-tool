package com.smartlink.archive.infrastructure.config;


public class DeleteConfig {

    /**
     * 批量每次归档的数据数量
     */
    private String batchSize;

    /**
     * 指定每个事务的大小（行数）
     */
    private String txnSize;

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getTxnSize() {
        return txnSize;
    }

    public void setTxnSize(String txnSize) {
        this.txnSize = txnSize;
    }
}
