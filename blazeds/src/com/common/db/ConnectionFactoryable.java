package com.common.db;

import java.sql.Connection;

public interface ConnectionFactoryable {
	Connection getConnection(String key);
}
