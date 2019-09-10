package cn.emay.boot.business.system.service;

import java.util.List;

public interface ExcelReaderService {

	public int saveBatchPreparedStatement(String sql, List<Object[]> batchArgs);

	public int[] saveBatchPreparedStatement2(String sql, List<Object[]> batchArgs);

}
