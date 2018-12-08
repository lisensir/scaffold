package org.scaffold.sorm.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scaffold.sorm.utils.ResultUtil;

public class MapConverProcessor implements IORMProcessor {
	
	private MapConverProcessor() {}
	
	private static MapConverProcessor mapConverProcessor;
	
	public static synchronized MapConverProcessor getInstance() {
		if(mapConverProcessor == null) mapConverProcessor = new MapConverProcessor();
		return mapConverProcessor;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> convertToObject(ResultSet rs, Class<T> targetClazz, IORMConfig<T> config) throws SQLException {

		List<Map<String, Object>> datas = new ArrayList<>();

		Map<String, Object> data;
		String[][] map = ResultUtil.getColumnStructure(rs);
		int columnCount = rs.getMetaData().getColumnCount();

		while (rs.next()) {
			data = new HashMap<>();

			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {

				if (config != null && config.convert(map[ResultUtil.COLUMN_NAME][columnIndex],
						ResultUtil.getColVal(rs, columnIndex, ResultUtil.DATE_MODEL_DATE), (T)data)) {
					data.put(map[ResultUtil.PROP_NAME][columnIndex],
							ResultUtil.getColVal(rs, columnIndex, ResultUtil.DATE_MODEL_DATE));
					continue;
				}

				data.put(map[ResultUtil.PROP_NAME][columnIndex],
						ResultUtil.getColVal(rs, columnIndex, ResultUtil.DATE_MODEL_DATE));
			}

			datas.add(data);
		}

		return (List<T>)datas;
	}

}
