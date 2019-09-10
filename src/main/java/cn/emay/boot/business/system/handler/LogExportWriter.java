package cn.emay.boot.business.system.handler;

import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import cn.emay.boot.base.constant.WebConstant;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.excel.utils.ExcelWriteUtils;
import cn.emay.excel.write.writer.SheetWriter;

public class LogExportWriter implements SheetWriter{

	private List<UserOperLog> datas;
	private List<String> titles;
	private String sheetName;
	private UserOperLog curr;
	
	public LogExportWriter(){
		
	}
	
	public LogExportWriter(List<String> titles,String sheetName,List<UserOperLog> datas){
		this.titles = titles;
		this.datas = datas;
		this.sheetName = sheetName;
	}
	
	@Override
	public String getSheetName() {
		return sheetName;
	}

	@Override
	public int getMaxColumnIndex() {
		return titles.size() - 1;
	}

	@Override
	public void begin(int sheetIndex) {
		
	}

	@Override
	public boolean hasRow(int rowIndex) {
		return rowIndex - 1 < datas.size();
	}

	@Override
	public void beginRow(int rowIndex) {
		if (rowIndex > 0) {
			curr = datas.get(rowIndex - 1);
			String operType = handleOperType(curr.getOperType());
			curr.setOperType(operType);
		}
	}

	@Override
	public void writeCell(Cell cell, int rowIndex, int columnIndex) {
		CellStyle style = cell.getCellStyle();
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		cell.setCellStyle(style);
		if (rowIndex == 0) {
			ExcelWriteUtils.writeString(cell, titles.get(columnIndex));
		} else {
			switch (columnIndex) {
			case 0:
				ExcelWriteUtils.writeString(cell, curr.getContent());
				break;
			case 1:
				ExcelWriteUtils.writeString(cell, curr.getModule());
				break;
			case 2:
				ExcelWriteUtils.writeString(cell, curr.getOperType());
				break;
			case 3:
				ExcelWriteUtils.writeString(cell, curr.getUsername());
				break;
			case 4:
				ExcelWriteUtils.writeString(cell, curr.getRealname());
				break;
			case 5:
				ExcelWriteUtils.writeDate(cell, curr.getOperTime(),WebConstant.PARAMETER_DATE_FORMAT);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void endRow(int rowIndex) {
		curr = null;
	}

	@Override
	public void end(int sheetIndex) {
		
	}
	
	private String handleOperType(String operType) {
		String type= "";
		if(operType.equals(UserOperLog.OPERATE_ADD)) {
			type = "新增";
		} else if(operType.equals(UserOperLog.OPERATE_MODIFY)) {
			type = "修改";
		} else if(operType.equals(UserOperLog.OPERATE_DELETE)) {
			type = "删除";
		} else if(operType.equals(UserOperLog.OPERATE_SELECT)) {
			type = "查询";
		}
		return type;
	}

}
