package cn.emay.boot.business.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.emay.boot.business.system.service.ExcelReaderService;


@Service
public class ExcelReaderServiceImpl implements ExcelReaderService {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public int saveBatchPreparedStatement(String sql, List<Object[]> batchArgs) {
		int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);
		int count = 0;
		if (null != batchUpdate && batchUpdate.length > 0) {
			for (int s : batchUpdate) {
				if (1 == s) {
					++count;
				}
			}
		}
		return count;
	}

	@Override
	public int[] saveBatchPreparedStatement2(String sql, List<Object[]> batchArgs) {
		int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);
		return batchUpdate;
	}
}
