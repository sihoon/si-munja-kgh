package com.m.point;

import java.sql.Connection;
import java.util.List;

public interface PointHistoryAble {

	public List<PointHistoryVO> getPointHistoryList(Connection conn, String user_id, String whenMonth);
}
